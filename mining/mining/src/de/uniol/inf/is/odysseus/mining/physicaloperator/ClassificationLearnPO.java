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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.FastArrayList;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.FastLinkedList;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.mining.classification.IClassificationLearner;
import de.uniol.inf.is.odysseus.mining.classification.IClassifier;

/**
 * 
 * @author Dennis Geesen Created at: 14.05.2012
 */
public class ClassificationLearnPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	private DefaultTISweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<Tuple<M>>(new FastLinkedList<Tuple<M>>());
	// private DefaultTISweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<Tuple<M>>();
	private FastArrayList<PointInTime> points = new FastArrayList<PointInTime>();	
	private IClassificationLearner<M> learn;
	

	public ClassificationLearnPO(IClassificationLearner<M> learn) {
		this.learn = learn;				
	}

	public ClassificationLearnPO(ClassificationLearnPO<M> clusteringPO) {
		this.learn = clusteringPO.learn;		
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

					long tillLearn = System.nanoTime();
					
					IClassifier<M> classifier = learn.createClassifier(qualifies);
					long afterLearn = System.nanoTime();
					
					M metadata = (M) object.getMetadata().clone();
					Tuple<M> newTuple = new Tuple<M>(1, false);
					newTuple.setAttribute(0, classifier);
					newTuple.setMetadata(metadata);
					newTuple.getMetadata().setStartAndEnd(startP, endP);
					// ((ILatency)newTuple.getMetadata()).setLatencyStart(start);
					// ((ILatency)newTuple.getMetadata()).setLatencyEnd(end);
					newTuple.setMetadata("LATENCY_BEFORE", tillLearn);
					newTuple.setMetadata("LATENCY_AFTER", afterLearn);
					transfer(newTuple);
					// System.out.println("TRANSFER: " + (System.currentTimeMillis() - time - duration));
					removeTill = i;
				}
			} else {
				break;
			}
		}
		if (removeTill != 0) {
			this.points.removeRange(0, removeTill);
			sweepArea.purgeElementsBefore(object.getMetadata().getStart());
		}

	}

	

	@Override
	protected void process_close() {
		sweepArea.clear();
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
//		if(punctuation.isHeartbeat()){
//			this.points.add(punctuation.getTime());
//		}
	}

	@Override
	public AbstractPipe<Tuple<M>, Tuple<M>> clone() {
		return new ClassificationLearnPO<M>(this);
	}

}
