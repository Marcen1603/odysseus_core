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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.mining.TupleList;
import de.uniol.inf.is.odysseus.mining.clustering.IClusterer;

/**
 * 
 * @author Dennis Geesen Created at: 14.05.2012
 */
public class ClusteringPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	private IClusterer<M> clusterer;
	private DefaultTISweepArea<TupleList<M>> sweepArea = new DefaultTISweepArea<TupleList<M>>();
	// private TITransferArea<Tuple<M>, Tuple<M>> transferFunction = new TITransferArea<Tuple<M>, Tuple<M>>();
	private int[] attributePositions;

	// private ArrayList<TupleList<M>> sweepArea = new ArrayList<TupleList<M>>();

	public ClusteringPO(IClusterer<M> clusterer, int[] attributePositions) {
		this.clusterer = clusterer;
		this.attributePositions = attributePositions;
	}

	public ClusteringPO(ClusteringPO<M> clusteringPO) {
		this.clusterer = clusteringPO.clusterer;
		this.attributePositions = clusteringPO.attributePositions;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected synchronized void process_next(Tuple<M> object, int port) {
//		System.out.println("------------------------------------------------------------");
//		System.out.println("IN: " + object);
//		System.out.println("SA:" + sweepArea.getSweepAreaAsString());
		// Iterator<TupleList<M>> qualifies = (new HashSet<TupleList<M>>(sweepArea)).iterator();
		// sweepArea.purgeElementsBefore(object.getMetadata().getStart());		
		
		
		
		
		
		Set<Tuple<M>> toCluster = new HashSet<>();
		Iterator<TupleList<M>> qualifies = sweepArea.extractElementsStartingBefore(object.getMetadata().getStart());
		
		if (!qualifies.hasNext()) {
			TupleList<M> tl = new TupleList<M>(object);
			M meta = (M) object.getMetadata().clone();
			tl.setMetadata(meta);
			sweepArea.insert(tl);
		} else {
			while (qualifies.hasNext()) {
				TupleList<M> next = qualifies.next();
				// System.out.println("NEXT: " + next);
				// gibt es einen zeitfortschritt, dann ist next vollständig und kann geclustert werden
				if (next.getMetadata().getStart().before(object.getMetadata().getStart())) {
					for (Tuple<M> tuple : next.getList()) {
						PointInTime start = next.getMetadata().getStart();
						PointInTime end = PointInTime.min(next.getMetadata().getEnd(), object.getMetadata().getStart());
						tuple.getMetadata().setStartAndEnd(start, end);
						toCluster.add(tuple);
					}
				}
				// schneiden sich next und das aktuelle element
				if (next.getMetadata().getEnd().after(object.getMetadata().getStart())) {
					// dann erweitere next um das aktuelle element
					TupleList<M> newList = next.clone();
					newList.add(object);
					newList.setMetadata((M) object.getMetadata().clone());
					newList.getMetadata().setEnd(PointInTime.min(object.getMetadata().getEnd(), next.getMetadata().getEnd()));
					insertOrReplace(newList);
					// additionally, there could be a part before the new element
					if (next.getMetadata().getEnd().before(object.getMetadata().getEnd())) {
						TupleList<M> tl = new TupleList<M>(object);
						tl.setMetadata((M) object.getMetadata().clone());
						tl.getMetadata().setStart(PointInTime.max(object.getMetadata().getStart(), next.getMetadata().getEnd()));
						insertOrReplace(tl);
					}
					// and there could be also a part after the element
					if (next.getMetadata().getEnd().after(object.getMetadata().getEnd())) {
						TupleList<M> tl = next.clone();
						tl.setMetadata((M) object.getMetadata().clone());
						tl.getMetadata().setEnd(next.getMetadata().getEnd());
						tl.getMetadata().setStart(object.getMetadata().getEnd());
						insertOrReplace(tl);
					}
				} else {
					// else, we only add the new object
					TupleList<M> tl = new TupleList<>(object);
					tl.setMetadata((M) object.getMetadata().clone());
					insertOrReplace(tl);
				}
			}
		}
		Iterator<Tuple<M>> results = calculateClusters(toCluster);
		while (results.hasNext()) {
			Tuple<M> result = results.next();
			// System.out.println("INSERT: " + result);
			// transferFunction.transfer(result);
			transfer(result);
		}
		// transferFunction.newElement(object, port);
	}

	private void insertOrReplace(TupleList<M> tuple) {
//		sweepArea.remove(tuple);
		sweepArea.insert(tuple);
	}

	private Iterator<Tuple<M>> calculateClusters(Set<Tuple<M>> toCluster) {
		ArrayList<Tuple<M>> clustered = new ArrayList<Tuple<M>>();
		for (Tuple<M> t : toCluster) {
			clustered.add(t.clone());
		}
		return clustered.iterator();
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		// transferFunction.init(this);
		sweepArea.clear();
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// transferFunction.sendPunctuation(punctuation);
	}

	@Override
	public AbstractPipe<Tuple<M>, Tuple<M>> clone() {
		return new ClusteringPO<M>(this);
	}

}
