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

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.AggregateTISweepArea;

/**
 * Worker thread for processing data stream objects, gets elements from blocking queue and executes them 
 * 
 * @author ChrisToenjesDeye
 *
 */
public class ThreadedAggregateTIPOWorker<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>>
		extends Thread {

	private ThreadedAggregateTIPO<Q, R, W> tipo;
	private ArrayBlockingQueue<Pair<Long, R>> queue;
	private int threadNumber;

	public ThreadedAggregateTIPOWorker(ThreadGroup threadGroup,
			ThreadedAggregateTIPO<Q, R, W> threadedAggregateTIPO, int threadNumber,
			ArrayBlockingQueue<Pair<Long, R>> queue) {
		super(threadGroup, "Threaded aggregate worker thread" + threadNumber);
		super.setDaemon(true);
		this.threadNumber = threadNumber;
		this.tipo = threadedAggregateTIPO;
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			// check if thread is interrupted
			if (isInterrupted()) {
				break;
			}
			
			// get values from queue
			Pair<Long, R> pair = null;
			Long groupId;
			R object;
			try {
				pair = queue.take();
			} catch (InterruptedException e) {
				break;
			}
			groupId = pair.getE1();
			object = pair.getE2();

			if (groupId != null && object != null){
				// get sweep area from groupId
				AggregateTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa = tipo.getSweepAreaForGroup(groupId);
				
				// if sweep are is found, process aggregation
				if (sa != null){
					synchronized (sa) {
						List<PairMap<SDFSchema, AggregateFunction, W, Q>> results = tipo
								.updateSA(sa, object, tipo.isOutputPA());
						tipo.createOutput(results, groupId, object.getMetadata().getStart(),
								threadNumber);
					}
				}
			}
		}

	}
}
