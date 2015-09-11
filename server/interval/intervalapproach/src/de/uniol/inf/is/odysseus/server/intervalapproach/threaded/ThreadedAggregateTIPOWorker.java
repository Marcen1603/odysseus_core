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
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.AggregateTISweepArea;

/**
 * Worker thread for processing data stream objects, gets elements from blocking
 * queue and executes them
 * 
 * @author ChrisToenjesDeye
 *
 */
public class ThreadedAggregateTIPOWorker<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>>
		extends Thread {

	private ThreadedAggregateTIPO<Q, R, W> tipo;
	private ArrayBlockingQueue<Pair<Long, R>> queue;
	private LinkedList<Pair<Long, R>> lastElementsQueue;
	private int threadNumber;
	private boolean tipoIsDone = false;
	private boolean tipoIsClosed = false;

	private Map<Long, AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> groupsToProcess = new ConcurrentHashMap<Long, AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>>();
	private IGroupProcessor<R, W> g = null;
	
	public ThreadedAggregateTIPOWorker(ThreadGroup threadGroup,
			ThreadedAggregateTIPO<Q, R, W> threadedAggregateTIPO,
			int threadNumber, ArrayBlockingQueue<Pair<Long, R>> queue) {
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
			// get values from queue
			Pair<Long, R> pair = null;

			try {
				pair = queue.take();
			} catch (InterruptedException e) {
				interrupt();
				continue;
			}
			processElement(pair);
		}

		// process last elements of queue only if process_close or process_done
		// is called
		if (tipoIsDone || tipoIsClosed) {
			// only drain the buffer if it is set in configuration
			if ((tipoIsDone && tipo.isDrainAtDone())
					|| (tipoIsClosed && tipo.isDrainAtClose())) {
				// draining the buffer is only needed if elements exists
				if (!queue.isEmpty()) {
					lastElementsQueue = new LinkedList<Pair<Long, R>>(queue);
					queue.clear();
					for (Pair<Long, R> pair : lastElementsQueue) {
						processElement(pair);
					}
				}
			}
		}
	}

	private void processElement(Pair<Long, R> pair) {
		Long groupId = pair.getE1();
		R object = pair.getE2();

		if (groupId != null && object != null) {
			// the grouping processor needs to know this object and group
			this.g.getGroupID(object);
			
			// get sweep area from groupId
			AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa;
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

	public void interruptOnDone() {
		tipoIsDone = true;
		tipoIsClosed = false;
		interrupt();
	}

	public void interruptOnClose() {
		tipoIsClosed = true;
		tipoIsDone = false;
		interrupt();
	}

	public Map<Long, AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> getGroupsToProcess() {
		return groupsToProcess;
	}
}
