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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.server.intervalapproach.AggregateTIPO;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * Worker thread for processing data stream objects, gets elements from blocking
 * queue and executes them
 * 
 * @author ChrisToenjesDeye
 *
 */
public class ThreadedAggregateTIPOWorker<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>>
		extends Thread {
	// number of this thread, needed for identification and for createOutput
	private int threadNumber;

	private AggregateTIPO<Q, R, W> tipo;
	// queue for elements, needed for decoupling
	private ArrayBlockingQueue<Pair<Object, R>> queue;
	// queue for last elements after done or close is called
	private LinkedList<Pair<Object, R>> lastElementsQueue;

	// flags if the tipo is done or closed
	private boolean tipoIsDone = false;
	private boolean tipoIsClosed = false;

	// every worker need own groups and an own groupProcessor (no
	// synchronization needed)
	private Map<Object, ITimeIntervalSweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> groupsToProcess = new ConcurrentHashMap<>();
	private IGroupProcessor<R, W> g = null;

	public ThreadedAggregateTIPOWorker(ThreadGroup threadGroup,
			AggregateTIPO<Q, R, W> threadedAggregateTIPO,
			int threadNumber, ArrayBlockingQueue<Pair<Object, R>> queue) {
		super(threadGroup, "Threaded aggregate worker thread " + threadNumber);
		super.setDaemon(true);
		this.threadNumber = threadNumber;
		this.tipo = threadedAggregateTIPO;
		this.queue = queue;
	}

	@Override
	public void run() {
		// create a single group processor for every worker
		this.g = tipo.getGroupProcessor().clone();
		this.g.init();

		// process every element
		while (!Thread.currentThread().isInterrupted()) {
			Pair<Object, R> pair = null;

			// get values from queue
			try {
				pair = queue.take();
			} catch (InterruptedException e) {
				interrupt();
			}
			if (pair != null) {
				processElement(pair);
			}
		}

		// draining the buffer is only needed if elements exists
		if (!queue.isEmpty()) {
			lastElementsQueue = new LinkedList<Pair<Object, R>>(queue);
			queue.clear();
			for (Pair<Object, R> pair : lastElementsQueue) {
				processElement(pair);
			}
		}

		// process last elements of queue only if process_close or process_done
		// is called
		if (tipoIsDone || tipoIsClosed) {
			// only drain the buffer if it is set in configuration
			if ((tipoIsDone && tipo.isDrainAtDone())
					|| (tipoIsClosed && tipo.isDrainAtClose())) {
				tipo.drainGroups(g, groupsToProcess, threadNumber);
			}
		}
	}

	private void processElement(Pair<Object, R> pair) {
		R object = pair.getE2();
		Object groupId = pair.getE1();

		if (groupId != null && object != null) {
			// the grouping processor needs to know this object and group
			this.g.setGroup(groupId, object);

			// get sweep area from groupId
			ITimeIntervalSweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa;
			synchronized (groupsToProcess) {
				sa = groupsToProcess.get(groupId);

				// if sweep area is found, process aggregation
				if (sa != null) {
					List<PairMap<SDFSchema, AggregateFunction, W, Q>> results = tipo
							.updateSA(sa, object, tipo.isOutputPA());

					tipo.createOutput(results, groupId, object.getMetadata()
							.getStart(), threadNumber, groupsToProcess, g);

				} else {
					throw new RuntimeException("No sweep area for " + pair
							+ " found!");
				}
			}
		}
	}

	/**
	 * if process_done is called on tipo, this method is invoked
	 */
	public void interruptOnDone() {
		tipoIsDone = true;
		tipoIsClosed = false;
		interrupt();
	}

	/**
	 * if process_close is called on tipo, this method is invoked
	 */
	public void interruptOnClose() {
		tipoIsClosed = true;
		tipoIsDone = false;
		interrupt();
	}

	/**
	 * returns the groups this worker processes
	 * 
	 * @return
	 */
	public Map<Object, ITimeIntervalSweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> getGroupsToProcess() {
		return groupsToProcess;
	}
}
