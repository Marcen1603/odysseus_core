/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/** Copyright 2012 The Odysseus Team
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

package de.uniol.inf.is.odysseus.mining.physicaloperator;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.mining.clustering.IClusterer;
import de.uniol.inf.is.odysseus.sweeparea.FastArrayList;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * 
 * @author Dennis Geesen Created at: 14.05.2012
 */
public class ClusteringPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	final private ITimeIntervalSweepArea<Tuple<M>> sweepArea;
	// private ITimeIntervalSweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<Tuple<M>>();
	private FastArrayList<PointInTime> points = new FastArrayList<PointInTime>();	
	private IClusterer<M> clusterer;

	public ClusteringPO(IClusterer<M> clusterer, ITimeIntervalSweepArea<Tuple<M>> sweepArea) {
		this.clusterer = clusterer;		
		this.sweepArea = sweepArea;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(Tuple<M> object, int port) {
		// System.out.println("------------------------------------------------------------");
		// System.out.println("IN: " + object);
		// System.out.println("SA:" + sweepArea.getSweepAreaAsString());
		// Iterator<TupleList<M>> qualifies = (new HashSet<TupleList<M>>(sweepArea)).iterator();

		if (!this.points.contains(object.getMetadata().getStart())) {
			this.points.add(object.getMetadata().getStart());
		}
		if (!this.points.contains(object.getMetadata().getEnd())) {
			this.points.add(object.getMetadata().getEnd());
		}
		Collections.sort(this.points);
		// we append before, so we only need a "simple" clone
		sweepArea.insert(object);
		int removeTill = 0;
		for (int i = 1; i < this.points.size(); i++) {
			PointInTime endP = this.points.get(i);
			PointInTime startP = this.points.get(i - 1);
			if (endP.beforeOrEquals(object.getMetadata().getStart())) {
				synchronized (this.sweepArea) {
					TimeInterval ti = new TimeInterval(startP, endP);
					List<Tuple<M>> qualifies = this.sweepArea.queryOverlapsAsList(ti);
					Map<Integer, List<Tuple<M>>> results = clusterer.processClustering(qualifies);					
					for (Entry<Integer, List<Tuple<M>>> cluster : results.entrySet()) {
						for (Tuple<M> result : cluster.getValue()) {
							Tuple<M> newTuple = result.append(cluster.getKey());
							M metadata = (M) result.getMetadata().clone();
							newTuple.setMetadata(metadata);
							newTuple.getMetadata().setStartAndEnd(startP, endP);
							transfer(newTuple);
						}
					}
					// System.out.println("TRANSFER: " + (System.currentTimeMillis() - time - duration));
					removeTill = i;
				}
			} else {
				break;
			}
		}
		if (removeTill != 0) {
			// System.out.println("PURGEING...");
			this.points.removeRange(0, removeTill);
			// long time = System.currentTimeMillis();
			sweepArea.purgeElementsBefore(object.getMetadata().getStart());
			// long duration = System.currentTimeMillis() - time;
			// System.out.println("DURATION PURGE: " + duration);
		}

	}

	@Override
	protected void process_open() throws OpenFailedException {
	}

	@Override
	protected void process_close() {
		// this might be improvable...
		sweepArea.clear();
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// transferFunction.sendPunctuation(punctuation);
	}

}
