package de.uniol.inf.is.odysseus.mining.classification;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface IClassificationLearner<M extends ITimeInterval> {

	

	/**
	 * Creates and initializes a new instance of the learning algorithms 
	 */
	public IClassificationLearner<M> createInstance();
	
	/**
	 * Initializes the classification learning algorithm
	 * @param inputschema The schema of the tuples (including the classAttribute)
	 * @param classAttribute The attribute that denotes the class
	 * @param nominals Nominal values for attributes, if exists
	 */
	public void init(String algorithm, SDFSchema inputschema, SDFAttribute classAttribute, Map<SDFAttribute, List<String>> nominals);
	
	/**
	 * Creates a classifier by using given tuples
	 * 
	 * @param tuples The training set 	
	 * @return A classifier
	 */
	public IClassifier<M> createClassifier(List<Tuple<M>> tuples);
	
	/** 
	 * This allows to set some options that can be used by the implementing class
	 * @param options
	 */
	public void setOptions(Map<String, String> options);
	
	/**
	 * A system wide unique name of the classifier
	 * @return the unique name
	 */
	public String getName();
	
	/**
	 * A list of possible algorithms used by this learner
	 * @return a list of names
	 */
	public List<String> getAlgorithmNames();

}
