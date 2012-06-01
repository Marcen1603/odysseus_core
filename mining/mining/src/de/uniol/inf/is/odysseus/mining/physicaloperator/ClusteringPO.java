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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.mining.clustering.IClusterer;

/**
 * 
 * @author Dennis Geesen Created at: 14.05.2012
 */
public class ClusteringPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	private IClusterer<M> clusterer;
	private DefaultTISweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<Tuple<M>>();	
	private TITransferArea<Tuple<M>, Tuple<M>> transferFunction = new TITransferArea<Tuple<M>, Tuple<M>>(1);
	private int run = 0;
	private int[] attributePositions;
	private PointInTime lastWritten;

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

	@Override
	protected synchronized void process_next(Tuple<M> object, int port) {
		sweepArea.insert(object);		

		System.err.println("---------------------------");
		System.err.println("in: "+object);
		System.err.println("---SA: ---");
		System.err.println(sweepArea.getSweepAreaAsString(object.getMetadata().getStart()));
		System.err.println("----------");
		Iterator<Tuple<M>> qualifies = sweepArea.queryElementsStartingBefore(object.getMetadata().getStart());		 
		Map<Integer, List<Tuple<M>>> clustered = this.clusterer.processClustering(qualifies, attributePositions);
		
		for (Entry<Integer, List<Tuple<M>>> cluster : clustered.entrySet()) {
			for (Tuple<M> tuple : cluster.getValue()) {
				tuple = tuple.append(cluster.getKey());				
				tuple = tuple.append(run);
				tuple.getMetadata().setStart(lastWritten);
				tuple.getMetadata().setEnd(object.getMetadata().getStart());
				System.err.println("transfer: "+tuple);
				this.transferFunction.transfer(tuple);
			}
		}
		run++;
		transferFunction.newElement(object, port);
		sweepArea.purgeElementsBefore(object.getMetadata().getStart());
		lastWritten = object.getMetadata().getStart();
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		transferFunction.init(this);
		sweepArea.clear();		
	}	

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		transferFunction.newHeartbeat(timestamp, port);
	}

	@Override
	public AbstractPipe<Tuple<M>, Tuple<M>> clone() {
		return new ClusteringPO<M>(this);
	}

}
