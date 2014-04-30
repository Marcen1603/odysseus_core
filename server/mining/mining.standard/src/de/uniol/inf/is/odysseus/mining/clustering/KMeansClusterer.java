package de.uniol.inf.is.odysseus.mining.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatencyTimeInterval;
import de.uniol.inf.is.odysseus.mining.distance.EuclidianDistance;
import de.uniol.inf.is.odysseus.mining.distance.IDistance;

public class KMeansClusterer<M extends ILatencyTimeInterval> implements IClusterer<M> {

	private IDistance distanceFunction = new EuclidianDistance();
	private int k = 3;
	private SDFSchema schema;

	@Override
	public Map<Integer, List<Tuple<M>>> processClustering(List<Tuple<M>> tuples) {
		List<Tuple<M>> pool = new ArrayList<>();

		for (Tuple<M> newTuple : tuples) {
			// initially, all is part of cluster -1!
			newTuple.setMetadata("CLUSTERID", -1);
			pool.add(newTuple);
		}
		// we need at least k tuples for a k-means clustering
		if (pool.size() >= k) {
			Map<Integer, Cluster<M>> means = new HashMap<>(k);
			// choose k random points for initial clustering
			Random random = new Random();
			for (int i = 0; i < k; i++) {
				Tuple<M> initialMean = pool.remove(random.nextInt(pool.size()));
				initialMean.setMetadata("CLUSTERID", i);
				means.put(i, new Cluster<M>(i, initialMean));
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
					for (Cluster<M> cluster : means.values()) {
						double distance = this.distanceFunction.getDistance(cluster.getMean(), tuple, this.schema);
						if (distance < currentDistance) {
							nearest = cluster;
							currentDistance = distance;
						}
					}
					// add tuple to nearest, if it was not the old one!
					int oldId = (int) tuple.getMetadata("CLUSTERID");
					if (oldId != nearest.getNumber()) {
						// we have a change
						changed = true;
						nearest.addTuple(tuple);
						if (oldId != -1) {
							means.get(oldId).removeTuple(tuple);
						}
						tuple.setMetadata("CLUSTERID", nearest.getNumber());
					}
				}

				pool.clear();
				// if we had a change...
				if (changed) {
					// ... recalculate the means and refill the pool
					for (Cluster<M> cluster : means.values()) {
						cluster.recalcMean();
						pool.addAll(cluster.getTuples());
					}
				}

			}
		}
		return null;
	}

	@Override
	public KMeansClusterer<M> createInstance() {
		return new KMeansClusterer<>();
	}

	@Override
	public void setOptions(Map<String, String> options) {
		String kStr = options.get("k");
		if (kStr != null) {
			this.k = Integer.parseInt(kStr);
		}
	}

	@Override
	public String getName() {
		return "standard";
	}

	@Override
	public List<String> getAlgorithmNames() {
		return Arrays.asList("kmeans");
	}

	@Override
	public void init(String algorithm, SDFSchema schema) {
		this.schema = schema;

	}

}
