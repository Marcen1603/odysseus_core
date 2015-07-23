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
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.AggregateTISweepArea;
import de.uniol.inf.is.odysseus.server.intervalapproach.AggregateTIPO;

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
	private Map<Integer, ArrayBlockingQueue<Pair<Long, R>>> queueMap = new ConcurrentHashMap<Integer, ArrayBlockingQueue<Pair<Long, R>>>();

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
	 */
	public ThreadedAggregateTIPO(SDFSchema inputSchema, SDFSchema outputSchema,
			List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations,
			boolean fastGrouping, IMetadataMergeFunction<Q> metadataMerge,
			int degree, int buffersize) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations,
				fastGrouping, metadataMerge);
		this.maxBufferSize = buffersize;
		this.degree = degree;
		// create threadgroup for grouping of worker threads
		workerThreadGroup = new ThreadGroup(
				"Threaded Aggregate worker threads "
						+ UUID.randomUUID().toString());

		// for each degree, create blocking queue and worker thread
		for (int i = 0; i < degree; i++) {
			ArrayBlockingQueue<Pair<Long, R>> blockingQueue = new ArrayBlockingQueue<Pair<Long, R>>(
					maxBufferSize);
			ThreadedAggregateTIPOWorker<Q, R, W> worker = new ThreadedAggregateTIPOWorker<Q, R, W>(
					workerThreadGroup, this, i, blockingQueue);
			queueMap.put(i, blockingQueue);
			threadMap.put(i, worker);
		}
	}

	/**
	 * called on process open, starts all worker threads
	 */
	@Override
	protected void process_open() throws OpenFailedException {

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
	 * processing of data stream elements. Puts every element into a blocking
	 * queue in round robin mode
	 */
	@Override
	protected void process_next(R object, int port) {
		if (debug) {
			System.err.println(this + " READ " + object);
		}

		IGroupProcessor<R, W> g = getGroupProcessor();
		AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa;

		Long groupID = null;
		synchronized (g) {
			// Determine group ID from input object
			groupID = g.getGroupID(object);
			// Find or create sweep area for group
			synchronized (groups) {
				sa = groups.get(groupID);
				if (sa == null) {
					sa = new AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>();
					groups.put(groupID, sa);
				}
			}
		}

		// we need to use the threads in round robin mode, to do a identically
		// load on the different cores
		int threadNumber = counter;
		counter++;
		if (counter == degree) {
			counter = 0;
		}

		// put element into queue, if queue is full this thread need to wait
		// until worker thread takes elements out of it
		ArrayBlockingQueue<Pair<Long, R>> queue = queueMap.get(threadNumber);
		try {
			// put object and groupId into queue
			Pair<Long, R> pair = new Pair<Long, R>(groupID, object);
			queue.put(pair);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * called if no elements are processed anymore, stops all worker threads
	 */
	@Override
	protected void process_done(int port) {
		// interrupt worker threads to finish work
		workerThreadGroup.interrupt();

		// has only one port, so process_done can be called when first input
		// port calls done
		IGroupProcessor<R, W> g = getGroupProcessor();
		synchronized (g) {
			if (drainAtDone) {
				// Drain all groups
				drainGroups();
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

	/**
	 * called if query is removed. interrupts all existing worker threads
	 */
	@Override
	protected void process_close() {
		// interrupt worker threads to finish work
		workerThreadGroup.interrupt();
		super.process_close();
	}

	@Override
	public void createOutput(
			List<PairMap<SDFSchema, AggregateFunction, W, Q>> existingResults,
			Long groupID, PointInTime timestamp, int inPort) {
		super.createOutput(existingResults, groupID, timestamp, inPort);
	}

	@Override
	public List<PairMap<SDFSchema, AggregateFunction, W, Q>> updateSA(
			AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			R elemToAdd, boolean outputPA) {
		return super.updateSA(sa, elemToAdd, outputPA);
	}

	public int getDegree() {
		return degree;
	}

	@Override
	public void setDegree(int degree) {
		this.degree = degree;
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
		return map;

	}
}
