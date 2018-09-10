package de.uniol.inf.is.odysseus.mining.kohonen;

import java.util.List;

import metrics.MetricModel;
import network.NetworkModel;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mining.classification.IClassifier;

/**
 * Implements a classifier that classifies incoming tuples with a kohonen map.
 * Incoming tuples must contain numeric values.
 * 
 * @author Dennis Nowak
 * 
 */
public class KohonenClassifier<M extends ITimeInterval> implements
		IClassifier<M> {

	private ClassRetriever classifier;

	/**
	 * Creates an instance of KohonenClassifier
	 * 
	 * @param networkModel
	 *            the kohonen map
	 * @param metricModel
	 *            the metric to use
	 */
	public KohonenClassifier(NetworkModel networkModel, MetricModel metricModel) {
		this.classifier = new ClassRetriever(networkModel, metricModel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.mining.classification.IClassifier#init(de.uniol
	 * .inf.is.odysseus.core.sdf.schema.SDFSchema,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute)
	 */
	@Override
	public void init(SDFSchema schema, SDFAttribute classAttribute) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.mining.classification.IClassifier#classify(de
	 * .uniol.inf.is.odysseus.core.collection.Tuple)
	 */
	@Override
	public Object classify(Tuple<M> tuple) {
		double[] vector = new double[tuple.size()];
		for (int i = 0; i < tuple.size(); i++) {
			vector[i] = (double) tuple.getAttribute(i);
		}
		List<Integer> list = classifier.classify(vector);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KohonenClassifier";
	}

}
