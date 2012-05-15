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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
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
	private List<SDFAttribute> attributes;
	private TITransferArea<Tuple<M>, Tuple<M>> transferFunction = new TITransferArea<Tuple<M>, Tuple<M>>(1);

	public ClusteringPO(IClusterer<M> clusterer, List<SDFAttribute> attributes) {
		this.clusterer = clusterer;
		this.attributes = attributes;
	}

	public ClusteringPO(ClusteringPO<M> clusteringPO) {
		this.clusterer = clusteringPO.clusterer;
		this.attributes = clusteringPO.attributes;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected synchronized void process_next(Tuple<M> object, int port) {
		sweepArea.insert(object);
		sweepArea.purgeElementsBefore(object.getMetadata().getStart());
		System.out.println("---------------------------");
		System.out.println(object);
		System.out.println("---SA: ---");
		System.out.println(sweepArea.getSweepAreaAsString(object.getMetadata().getStart()));
		System.out.println("----------");
		Iterator<Tuple<M>> qualifies = sweepArea.queryOverlaps(object.getMetadata());
		Map<Integer, List<Tuple<M>>> clustered = this.clusterer.processClustering(qualifies);

		for (Entry<Integer, List<Tuple<M>>> cluster : clustered.entrySet()) {
			for (Tuple<M> tuple : cluster.getValue()) {
				tuple = tuple.append(cluster.getKey());
				this.transferFunction.transfer(tuple);
			}
		}
		transferFunction.newElement(object, port);

	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		transferFunction.init(this);
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
