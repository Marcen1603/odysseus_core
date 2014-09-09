package de.uniol.inf.is.odysseus.mining.kohonen;

import java.util.List;

import metrics.MetricModel;
import network.NetworkModel;
import kohonen.ClassRetriever;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mining.classification.IClassifier;

/**
 * @author Dennis Nowak
 *
 */
public class KohonenClassifier<M extends ITimeInterval> implements IClassifier<M> {
	
	private ClassRetriever classifier;
	
	/*
	 * 
	 */
	public KohonenClassifier(NetworkModel networkModel, MetricModel metricModel) {
		this.classifier = new ClassRetriever(networkModel, metricModel);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mining.classification.IClassifier#init(de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema, de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute)
	 */
	@Override
	public void init(SDFSchema schema, SDFAttribute classAttribute) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mining.classification.IClassifier#classify(de.uniol.inf.is.odysseus.core.collection.Tuple)
	 */
	@Override
	public Object classify(Tuple<M> tuple) {
		double[] vector = new double[tuple.size()];
		for(int i = 0; i < tuple.size(); i++) {
			vector[i] = (double) tuple.getAttribute(i);
		}
		List<Integer> list  = classifier.classify(vector);
		return list;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "KohonenClassifier";
	}

}
