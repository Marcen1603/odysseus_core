/**********************************************************************************
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.server.intervalapproach.threaded;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.server.intervalapproach.AggregateTIPO;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * Physical operator for aggregation which supports multithreading. Works only
 * if the operator has grouping attributes
 *
 * @author ChrisToenjesDeye
 */
public class ThreadedAggregateTIPO<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>>
		extends AggregateTIPO<Q, R, W> implements IThreadedPO {

	private int degree;
	private int maxBufferSize;
	private ThreadGroup workerThreadGroup;
	private int counter = 0;
	// concurrent map which contains threads for execution
	private Map<Integer, ThreadedAggregateTIPOWorker<Q, R, W>> threadMap = new ConcurrentHashMap<Integer, ThreadedAggregateTIPOWorker<Q, R, W>>();

	// concurrent map which contains blocking queues for each worker thread
	private Map<Integer, ArrayBlockingQueue<Pair<Object, R>>> queueMap = new ConcurrentHashMap<Integer, ArrayBlockingQueue<Pair<Object, R>>>();

	private boolean useRoundRobinAllocation;

	// worker allocation is needed for roundRobin because we want to assign the
	// groups and not the elements in roundRobin
	private Map<Object, Integer> workerAllocation = new ConcurrentHashMap<>();

	// monitor to synchronize process_done and process_close
	private Object monitor = new Object();

	/**
	 * Constructor for physical aggregate operator (threaded)
	 *
	 * @param inputSchema
	 * @param outputSchema
	 * @param groupingAttributes
	 * @param aggregations
	 * @param fastGrouping
	 * @param metadataMerge
	 * @param degree
	 * @param buffersize
	 * @param useRoundRobinAllocation
	 */
	public ThreadedAggregateTIPO(SDFSchema inputSchema, SDFSchema outputSchema, List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations, boolean fastGrouping,
			IMetadataMergeFunction<Q> metadataMerge, int degree, int buffersize, boolean useRoundRobinAllocation) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations, fastGrouping, metadataMerge);
		this.maxBufferSize = buffersize;
		this.degree = degree;
		this.useRoundRobinAllocation = useRoundRobinAllocation;
		// create threadgroup for grouping of worker threads
		workerThreadGroup = new ThreadGroup("ThreadedAggregateTIPO worker threads " + UUID.randomUUID().toString());

	}

	/**
	 * called on process open, starts all worker threads
	 */
	@Override
	protected void process_open() throws OpenFailedException {
		// for each degree, create blocking queue and worker thread
		initWorker(degree);

		IGroupProcessor<R, W> g = getGroupProcessor();
		synchronized (g) {
			g.init();
			// the transfer area has number of threads as input ports, this
			// allows it that the parallel processed elements are in order
			transferArea.init(this, degree);
			groups.clear();
			createOutputCounter = 0;
		}

		// start worker threads
		for (ThreadedAggregateTIPOWorker<Q, R, W> worker : threadMap.values()) {
			worker.start();
		}
	}

	/**
	 * Initializes the given number of workers for this aggregate operator
	 *
	 * @param degree
	 */
	private void initWorker(int degree) {
		for (int i = 0; i < degree; i++) {
			ArrayBlockingQueue<Pair<Object, R>> blockingQueue = new ArrayBlockingQueue<Pair<Object, R>>(maxBufferSize);
			ThreadedAggregateTIPOWorker<Q, R, W> worker = new ThreadedAggregateTIPOWorker<Q, R, W>(workerThreadGroup,
					this, i, blockingQueue);
			queueMap.put(i, blockingQueue);
			threadMap.put(i, worker);
		}
	}

	/**
	 * processing of data stream elements. Puts every element into a blocking
	 * queue in round robin mode
	 */
	@Override
	protected void process_next(R object, int port) {
		if (debug) {
			System.err.println(this + " READ " + object);
		}

		IGroupProcessor<R, W> g = getGroupProcessor();
		ITimeIntervalSweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa;

		Object groupID = null;
		synchronized (groups) {
			// Determine group ID from input object
			groupID = g.getGroupID(object);
			// Find or create sweep area for group
			synchronized (groups) {
				sa = groups.get(groupID);
				if (sa == null) {
					// TODO: Make flexible
					sa = new DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>();
					groups.put(groupID, sa);
				}
			}

			// get thread number and get the worker with this number
			int threadNumber = getThreadNumber(groupID);
			ThreadedAggregateTIPOWorker<Q, R, W> worker = threadMap.get(threadNumber);

			// each worker has a subset of the groups
			if (!worker.getGroupsToProcess().containsKey(groupID)) {
				Map<Object, ITimeIntervalSweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> groupsToProcess = worker
						.getGroupsToProcess();
				synchronized (groupsToProcess) {
					groupsToProcess.put(groupID, sa);
				}
			}

			// put element into queue, if queue is full this thread need to wait
			// until worker thread takes elements out of it
			ArrayBlockingQueue<Pair<Object, R>> queue = queueMap.get(threadNumber);
			try {
				// put object and groupId into queue
				Pair<Object, R> pair = new Pair<>(groupID, object);
				queue.put(pair);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * calculates the thread number for the given groupId. Calculation is done
	 * by RoundRobin or Hash
	 *
	 * @param groupID
	 * @return
	 */
	private int getThreadNumber(Object groupID) {
		// if (useRoundRobinAllocation) {
		if (workerAllocation.containsKey(groupID)) {
			return workerAllocation.get(groupID);
		} else {
			int threadNumber = counter;
			workerAllocation.put(groupID, threadNumber);

			counter++;
			if (counter == degree) {
				counter = 0;
			}
			return threadNumber;
		}

		// // if we use roundRobin allocation, we are counting until the
		// // maximum value is reached and start with 0
		// } else {
		// // if no round robin is used, the thread number is the hash value of
		// // the group
		// return (int) (groupID % degree);
		// }
	}

	/**
	 * called if no elements are processed anymore, stops all worker threads
	 */
	@Override
	protected void process_done(int port) {
		// done need to be synchronized with close
		synchronized (monitor) {

			// interrupt worker threads to finish work
			for (ThreadedAggregateTIPOWorker<Q, R, W> worker : threadMap.values()) {
				worker.interruptOnDone();
			}

			// wait until all worker stopped
			for (ThreadedAggregateTIPOWorker<Q, R, W> thread : threadMap.values()) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// Send information to transfer area that no more elements will be
			// delivered on port 0, so all data can be written
			if (debug) {
				System.err.println(this + " done");
			}

			for (int i = 0; i < degree; i++) {
				transferArea.done(i);
			}
		}
	}

	/**
	 * called if query is removed. interrupts all existing worker threads
	 */
	@Override
	protected void process_close() {
		// close need to be synchronized with done
		synchronized (monitor) {
			// interrupt worker threads to finish work
			for (ThreadedAggregateTIPOWorker<Q, R, W> worker : threadMap.values()) {
				worker.interruptOnClose();
			}

			// wait until all worker stopped
			for (ThreadedAggregateTIPOWorker<Q, R, W> thread : threadMap.values()) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			super.process_close();
		}
	}

	@Override
	public void createOutput(List<PairMap<SDFSchema, AggregateFunction, W, Q>> existingResults, Object groupID,
			PointInTime timestamp, int inPort,
			Map<Object, ITimeIntervalSweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> groupsToProcess,
			IGroupProcessor<R, W> g) {
		super.createOutput(existingResults, groupID, timestamp, inPort, groupsToProcess, g);
	}

	public int getDegree() {
		return degree;
	}

	@Override
	public void setDegree(int degree) {
		this.degree = degree;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO punctuations are not processed
	}

	/**
	 * add additional informations for multithreading
	 */
	@Override
	public Map<String, String> getKeyValues() {
		Map<String, String> map = new HashMap<>();
		map.putAll(super.getKeyValues());
		map.put("Number of threads", String.valueOf(degree));
		map.put("Buffersize", String.valueOf(maxBufferSize));
		map.put("Use RoundRobin allocation", String.valueOf(useRoundRobinAllocation));
		return map;

	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (ipo instanceof ThreadedAggregateTIPO) {
			return super.isSemanticallyEqual(ipo);
		} else {
			return false;
		}
	}
}
