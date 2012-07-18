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

package de.uniol.inf.is.odysseus.mining.clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.math3.stat.descriptive.moment.Variance;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.mining.distance.EuclidianDistance;
import de.uniol.inf.is.odysseus.mining.distance.IDistance;

/**
 * 
 * @author Dennis Geesen Created at: 15.05.2012
 */
public class KMeans<M extends ITimeInterval> implements IClusterer<M> {

	public static enum EmptyClusterStrategy {

		/** Split the cluster with largest distance variance. */
		LARGEST_VARIANCE,

		/** Split the cluster with largest number of points. */
		LARGEST_POINTS_NUMBER,

		/** Create a cluster around the point farthest from its centroid. */
		FARTHEST_POINT,

		/** Generate an error. */
		ERROR

	}

	private int numberOfClusters = 2;
	private HashMap<Integer, Tuple<M>> means = new HashMap<Integer, Tuple<M>>();
	private HashMap<Integer, ClusterDescription<M>> clusters = new HashMap<Integer, ClusterDescription<M>>();

	private IDistance distance = new EuclidianDistance();
	private Random random = new Random();
	private EmptyClusterStrategy emptyStrategy = EmptyClusterStrategy.LARGEST_POINTS_NUMBER;

	

	public void init() {
		means = new HashMap<Integer, Tuple<M>>();
		clusters = new HashMap<Integer, ClusterDescription<M>>();
	}

	@Override
	public Map<Integer, List<Tuple<M>>> processClustering(Iterator<Tuple<M>> tuples, int[] attributes) {
		init();
		boolean changed = false;
		while (tuples.hasNext()) {
			Tuple<M> tuple = tuples.next();
			if (means.size() < numberOfClusters) {
				int clusterid = means.size() + 1;
				this.means.put(clusterid, tuple);
				ClusterDescription<M> cd = new ClusterDescription<M>(attributes);
				cd.addTuple(tuple);
				this.clusters.put(clusterid, cd);
			} else {
				int assignment = findNearestCluster(tuple, attributes, null);
				this.clusters.get(assignment).addTuple(tuple);
				changed = true;
			}
		}

		
		while (changed) {
			System.out.println("################################ REASSIGN ################################");
			// if there is nothing reassigned, then we can stop
			changed = false;
			// first, recalc means
			for (Entry<Integer, ClusterDescription<M>> cluster : this.clusters.entrySet()) {
				this.means.put(cluster.getKey(), cluster.getValue().getMean());
			}

			// try to reassign values
			for (Entry<Integer, ClusterDescription<M>> cluster : this.clusters.entrySet()) {
				int currentCluster = cluster.getKey();
				List<Tuple<M>> current = new ArrayList<Tuple<M>>(cluster.getValue().getTuples());
				System.out.println("-------------------------------------");				
				for (Tuple<M> tuple : current) {
					System.out.println("Tuple: "+tuple);
					int assign = findNearestCluster(tuple, attributes, cluster);
					System.out.println("nearest: "+assign);
					System.out.println("old one: "+currentCluster);
					// new and old cluster are not the same, so reassign
					if (assign != currentCluster) {						
						reassignTuple(tuple, currentCluster, assign);
						changed = true;
					}
				}
			}

			// split empty clusters
			Iterator<Entry<Integer, ClusterDescription<M>>> iter = this.clusters.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<Integer, ClusterDescription<M>> entry = iter.next();
				if (entry.getValue().isEmpty()) {

					switch (emptyStrategy) {
					case LARGEST_VARIANCE:
						entry.getValue().addTuple(getPointFromLargestVarianceCluster(attributes));
						break;
					case LARGEST_POINTS_NUMBER:
						entry.getValue().addTuple(getPointFromLargestNumberCluster(attributes));
						break;
					case FARTHEST_POINT:
						entry.getValue().addTuple(getFarthestPoint(attributes));
						break;
					default:
						throw new RuntimeException("found an empty cluster");
					}
				}
			}
		}
		Map<Integer, List<Tuple<M>>> results = new HashMap<Integer, List<Tuple<M>>>();
		for (Entry<Integer, ClusterDescription<M>> e : this.clusters.entrySet()) {
			results.put(e.getKey(), new ArrayList<Tuple<M>>(e.getValue().getTuples()));
		}

		return results;
	}

	private void reassignTuple(Tuple<M> tuple, int from, int to) {
		this.clusters.get(from).removeTuple(tuple);
		this.clusters.get(to).addTuple(tuple);
	}

	private int findNearestCluster(Tuple<M> tuple, int[] attributePositions, Map.Entry<Integer, ClusterDescription<M>> currentCluster) {
		int id = -1;
		double currentMin = Double.MAX_VALUE;
		if(currentCluster!=null){
			currentMin = this.distance.getDistance(tuple.restrict(attributePositions, true), currentCluster.getValue().getMean().restrict(attributePositions, true));
			id = currentCluster.getKey();
		}
		for (Entry<Integer, Tuple<M>> cluster : means.entrySet()) {
			double currentDistance = this.distance.getDistance(tuple.restrict(attributePositions, true), cluster.getValue().restrict(attributePositions, true));
			if (currentDistance < currentMin) {
				id = cluster.getKey();
				currentMin = currentDistance;
			}
		}
		return id;
	}

	private Tuple<M> getPointFromLargestNumberCluster(int[] attributePositions) {

		int maxNumber = 0;
		ClusterDescription<M> selected = null;
		for (Entry<Integer, ClusterDescription<M>> cluster : this.clusters.entrySet()) {

			// get the number of points of the current cluster
			final int number = cluster.getValue().getTuples().size();

			// select the cluster with the largest number of points
			if (number > maxNumber) {
				maxNumber = number;
				selected = cluster.getValue();
			}

		}
		if(selected==null){
			throw new RuntimeException("found an empty cluster");
		}
		
		// extract a random point from the cluster
		Tuple<M> removed = selected.removeTuple(random.nextInt(selected.size()));
		return removed;

	}

	private Tuple<M> getPointFromLargestVarianceCluster(int[] attributePositions) {

		double maxVariance = Double.NEGATIVE_INFINITY;
		ClusterDescription<M> selected = null;
		for (Entry<Integer, ClusterDescription<M>> cluster : this.clusters.entrySet()) {
			if (!cluster.getValue().getTuples().isEmpty()) {

				// compute the distance variance of the current cluster
				Tuple<M> center = cluster.getValue().getMean();
				Variance stat = new Variance();
				for (Tuple<M> tuple : cluster.getValue().getTuples()) {

					stat.increment(this.distance.getDistance(tuple.restrict(attributePositions, true), center.restrict(attributePositions, true)));
				}
				double variance = stat.getResult();

				// select the cluster with the largest variance
				if (variance > maxVariance) {
					maxVariance = variance;
					selected = cluster.getValue();
				}

			}
		}
		if (selected == null) {
			throw new RuntimeException("found an empty cluster");
		}
		// extract a random point from the cluster
		Tuple<M> removed = selected.removeTuple(random.nextInt(selected.size()));
		return removed;

	}

	private Tuple<M> getFarthestPoint(int[] attributes) {

		double maxDistance = Double.NEGATIVE_INFINITY;
		ClusterDescription<M> selectedCluster = null;
		int selectedPoint = -1;
		for (Entry<Integer, ClusterDescription<M>> cluster : this.clusters.entrySet()) {
			if (!cluster.getValue().getTuples().isEmpty()) {
				// get the farthest point
				Tuple<M> center = cluster.getValue().getMean();
				List<Tuple<M>> tuples = cluster.getValue().getTuples();
				for (int i = 0; i < tuples.size(); ++i) {
					final double distance = this.distance.getDistance(tuples.get(0).restrict(attributes, true), center.restrict(attributes, true));
					if (distance > maxDistance) {
						maxDistance = distance;
						selectedCluster = cluster.getValue();
						selectedPoint = i;
					}
				}
			}

		}
		if (selectedCluster == null) {
			throw new RuntimeException("found an empty cluster");
		}
		
		Tuple<M> removed = selectedCluster.removeTuple(selectedPoint);
		return removed;		
	}

	
	@Override
	public void setOptions(Map<String, List<String>> options) {
		
		
	}
}
