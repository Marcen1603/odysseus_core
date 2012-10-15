/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.mining.clustering.Cluster;
import de.uniol.inf.is.odysseus.mining.distance.EuclidianDistance;
import de.uniol.inf.is.odysseus.mining.distance.IDistance;

/**
 * @author Dennis Geesen
 * 
 */
public class ClusteringKMeansPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	private int[] attributePositions;
	private DefaultTISweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<Tuple<M>>();
	private int k = 2;
	private IDistance distanceFunction = new EuclidianDistance();
	private SDFSchema schema;

	public ClusteringKMeansPO(int k, SDFSchema schema, int[] attributePositions) {
		this.k = k;
		this.schema = schema;
		this.attributePositions = attributePositions;
	}

	public ClusteringKMeansPO(ClusteringKMeansPO<M> po) {
		this.k = po.k;
		this.attributePositions = po.attributePositions;
		this.schema = po.schema;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {

	}

	@Override
	protected synchronized void process_next(Tuple<M> object, int port) {
		PointInTime currentTime = object.getMetadata().getStart();
		Iterator<Tuple<M>> qualifies = sweepArea.queryElementsStartingBefore(currentTime);
		List<Tuple<M>> pool = new ArrayList<>();
		while (qualifies.hasNext()) {
			Tuple<M> newTuple = qualifies.next();
			// initially, all is part of cluster -1!
			newTuple.setMetadata("CLUSTERID", -1);
			pool.add(newTuple);
		}
		List<Cluster<M>> means = new ArrayList<>(k);
		// choose k random points for initial clustering
		Random random = new Random();
		for (int i = 0; i < k; i++) {
			Tuple<M> initialMean = pool.remove(random.nextInt(pool.size()));
			means.add(new Cluster<>(i, initialMean));
		}
		boolean changed = true;
		// iterate until nothing changed
		while (changed) {
			// optimistically, there is no change needed
			changed = false;
			// assign pool to nearest cluster
			for (Tuple<M> tuple : pool) {
				double currentDistance = Double.MAX_VALUE;
				Cluster<M> nearest = null;
				for (Cluster<M> cluster : means) {
					double distance = this.distanceFunction.getDistance(cluster.getMean(), tuple, this.schema);
					if (distance < currentDistance) {
						nearest = cluster;
					}
				}
				// add tuple to nearest, if it was not the old one!
				if (((int) tuple.getMetadata("CLUSTERID")) != nearest.getNumber()) {
					// we have a change
					changed = true;
					nearest.addTuple(tuple);
					tuple.setMetadata("CLUSTERID", nearest.getNumber());
				}
			}

			pool.clear();
			// if we had a change...
			if (changed) {
				// ... recalculate the means and refill the pool
				for (Cluster<M> cluster : means) {
					cluster.recalcMean();
					pool.addAll(cluster.getTuples());
				}
			}

		}
		
		// we no can transfer found clusters... but how?!
		
		// finally, remove all things we do not need anymore
		sweepArea.purgeElementsBefore(currentTime);
		// cut the object to given attributes (and clone it)
		Tuple<M> cloned = object.restrict(this.attributePositions, true);
		// and insert the current
		sweepArea.insert(cloned);

	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public ClusteringKMeansPO<M> clone() {
		return new ClusteringKMeansPO<M>(this);
	}

}
