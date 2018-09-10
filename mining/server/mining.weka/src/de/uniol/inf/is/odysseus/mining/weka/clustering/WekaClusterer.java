package de.uniol.inf.is.odysseus.mining.weka.clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Clusterer;
import weka.clusterers.Cobweb;
import weka.clusterers.EM;
import weka.clusterers.FarthestFirst;
import weka.clusterers.HierarchicalClusterer;
import weka.clusterers.MakeDensityBasedClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.OptionHandler;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mining.clustering.IClusterer;
import de.uniol.inf.is.odysseus.mining.weka.mapping.WekaAttributeResolver;
import de.uniol.inf.is.odysseus.mining.weka.mapping.WekaConverter;

public class WekaClusterer<M extends ITimeInterval> implements IClusterer<M> {

	private Clusterer clusterer = new SimpleKMeans();
	private WekaAttributeResolver war;

	private enum WekaClusterAlgorithm {
		SIMPLEKMEANS, EM, COBWEB, FARTHESTFIRST, DENSITY_KMEANS, HIERARCHICAL;

		public static List<String> names() {
			List<String> list = new ArrayList<>();
			for (WekaClusterAlgorithm wca : WekaClusterAlgorithm.values()) {
				list.add(wca.name());
			}
			return list;
		}
	}

	@Override
	public void init(String algorithm, SDFSchema schema) {
		this.war = new WekaAttributeResolver(schema);
		WekaClusterAlgorithm model = WekaClusterAlgorithm.valueOf(algorithm.toUpperCase());
		if (model != null) {
			switch (model) {
			case SIMPLEKMEANS:
				clusterer = new SimpleKMeans();
				break;
			case EM:
				clusterer = new EM();
				break;
			case COBWEB:
				clusterer = new Cobweb();
				break;
			case FARTHESTFIRST:
				clusterer = new FarthestFirst();
				break;
			case DENSITY_KMEANS:
				clusterer = new MakeDensityBasedClusterer();
				break;
			case HIERARCHICAL:
				clusterer = new HierarchicalClusterer();
				break;
			default:
				throw new IllegalArgumentException("There is no classifier model called " + model + "!");
			}
		}

	}

	@Override
	public Map<Integer, List<Tuple<M>>> processClustering(List<Tuple<M>> tuples) {
		Map<Integer, List<Tuple<M>>> map = new HashMap<>();
		Instances instances = WekaConverter.convertToInstances(tuples, war);
		try {
			if (tuples.size() < clusterer.numberOfClusters()) {
				return map;
			}
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
	public void setOptions(Map<String, String> options) {
		try {			
				if (clusterer instanceof OptionHandler) {
					String[] wekaoptions = weka.core.Utils.splitOptions(options.get("arguments"));
					((OptionHandler) clusterer).setOptions(wekaoptions);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getName() {
		return "weka";
	}

	@Override
	public List<String> getAlgorithmNames() {
		return WekaClusterAlgorithm.names();
	}

	@Override
	public IClusterer<M> createInstance() {
		return new WekaClusterer<M>();
	}

}
