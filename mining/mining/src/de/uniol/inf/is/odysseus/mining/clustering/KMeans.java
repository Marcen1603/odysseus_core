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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.mining.distance.EuclidianDistance;
import de.uniol.inf.is.odysseus.mining.distance.IDistance;

/**
 * 
 * @author Dennis Geesen Created at: 15.05.2012
 */
public class KMeans<M extends ITimeInterval> implements IClusterer<M> {

	private int numberOfClusters = 2;
	private Map<Integer, Tuple<M>> means = new HashMap<Integer, Tuple<M>>();
	private Map<Integer, ClusterDescription<M>> clusters = new HashMap<Integer, ClusterDescription<M>>();

	private IDistance distance = new EuclidianDistance();

	public void setOptions(Map<String, String> options) {
		numberOfClusters = Integer.parseInt(options.get("K"));
	}

	public void init(){
		means = new HashMap<Integer, Tuple<M>>();
		clusters = new HashMap<Integer, ClusterDescription<M>>();
	}
	
	@Override
	public Map<Integer, List<Tuple<M>>> processClustering(Iterator<Tuple<M>> tuples) {
		init();
		while (tuples.hasNext()) {
			Tuple<M> tuple = tuples.next();
			if (means.size() < numberOfClusters) {
				int clusterid = means.size() + 1;
				this.means.put(clusterid, tuple);
				ClusterDescription<M> cd = new ClusterDescription<M>();
				cd.addTuple(tuple);
				this.clusters.put(clusterid, cd);
			} else {
				int assignment = findNearestCluster(tuple);
				this.clusters.get(assignment).addTuple(tuple);
			}
		}

		boolean changed = true;
		while (changed) {
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
				for (Tuple<M> tuple : current) {
					int assign = findNearestCluster(tuple);
					// new and old cluster are not the same, so reassign
					if (assign != currentCluster) {
						reassignTuple(tuple, currentCluster, assign);
						changed = true;
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

	private int findNearestCluster(Tuple<M> tuple) {
		int id = -1;
		double currentMin = Double.MAX_VALUE;
		for (Entry<Integer, Tuple<M>> cluster : means.entrySet()) {
			double currentDistance = this.distance.getDistance(tuple, cluster.getValue());
			if (currentDistance < currentMin) {
				id = cluster.getKey();
				currentMin = currentDistance;
			}
		}
		return id;
	}	
}
