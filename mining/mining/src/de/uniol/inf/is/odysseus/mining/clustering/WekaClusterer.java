package de.uniol.inf.is.odysseus.mining.clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mining.weka.WekaAttributeResolver;
import de.uniol.inf.is.odysseus.mining.weka.WekaConverter;

public class WekaClusterer<M extends ITimeInterval> implements IClusterer<M> {

	private SimpleKMeans clusterer = new SimpleKMeans();
	private WekaAttributeResolver war;
	
	@Override
	public void init(SDFSchema schema) {
		this.war = new WekaAttributeResolver(schema);		
	}

	@Override
	public Map<Integer, List<Tuple<M>>> processClustering(List<Tuple<M>> tuples) {
		Map<Integer, List<Tuple<M>>> map = new HashMap<>();		
		Instances instances = WekaConverter.convertToInstances(tuples, war);		
		try {
			clusterer.buildClusterer(instances);
			ClusterEvaluation eval = new ClusterEvaluation();
			eval.setClusterer(clusterer);
			eval.evaluateClusterer(instances);
			for (int i = 0; i < eval.getNumClusters(); i++) {
				map.put(i, new ArrayList<Tuple<M>>());
			}
			double[] assignments = eval.getClusterAssignments();
			int i = 0;
			for (Tuple<M> tuple : tuples) {
				Integer id = (int) assignments[i];
				map.get(id).add(tuple);
				i++;
			}
			return map;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public void setOptions(Map<String, List<String>> options) {
		// TODO map the options from input to weka

	}

	

}
