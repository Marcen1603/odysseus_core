package de.uniol.inf.is.odysseus.mining.clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class WekaClusterer<M extends ITimeInterval> implements IClusterer<M> {

	private SimpleKMeans clusterer = new SimpleKMeans();
	private SDFSchema schema;
	private ArrayList<Attribute> wekaSchema;

	public WekaClusterer(SDFSchema schema, int[] attributes) {
		this.schema = schema;
		this.wekaSchema = calcAttributeList(attributes);
	}

	@Override
	public Map<Integer, List<Tuple<M>>> processClustering(List<Tuple<M>> tuples, int[] attributes) {
		Map<Integer, List<Tuple<M>>> map = new HashMap<>();
		Instances instances = new Instances(this.schema.getURI(), this.wekaSchema, 10);
		// wrap up elements
		for (Tuple<M> tuple : tuples) {
			Instance inst = new DenseInstance(attributes.length);
			for (int i = 0; i < attributes.length; i++) {
				double val = ((Number) tuple.getAttribute(attributes[i])).doubleValue();
				inst.setValue(i, val);
			}
			instances.add(inst);
		}
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
				Integer id = (int)assignments[i];
				map.get(id).add(tuple);
				i++;
			}
			return map;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	private ArrayList<Attribute> calcAttributeList(int[] attributes) {
		ArrayList<Attribute> al = new ArrayList<Attribute>();
		for (int i = 0; i < attributes.length; i++) {
			SDFAttribute sdfa = this.schema.getAttribute(attributes[i]);
			Attribute attr = new Attribute(sdfa.getAttributeName());
			al.add(attr);
		}
		return al;
	}

	@Override
	public void setOptions(Map<String, List<String>> options) {
		// TODO Auto-generated method stub

	}

}
