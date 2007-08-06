package mg.dynaquest.monitor.responseprediction.learner;

import java.io.Serializable;
import java.util.Map;

import mg.dynaquest.monitor.responseprediction.preprocessor.PlanOperator;
import mg.dynaquest.monitor.responseprediction.preprocessor.PreprocessedMetadata;


/**
 * Interface for learning components.
 * 
 * Each learner has a classattribute and a number of other attributes.
 * The unknown classattribute gets predicted on the basis of known values
 * of the attributes.
 * Attributes are represented by {@link Attribute} objects, which 
 * also convert the preprocessed metadata to the format of the input data needed
 * by the learner.
 * 
 * If you want to implement a learner you usually don't want to implement this
 * interface, but subclass {@link AbstractLearner}.
 * @author Jonas Jacobi
 */
public interface ILearner extends Serializable {

	/**
	 * Set the attributes
	 * @param attributes attributes
	 */
	public abstract void setAttributes(Attribute... attributes);

	/**
	 * Get the attributes
	 * @return the attributes
	 */
	public abstract Attribute[] getAttributes();

	/**
	 * Set the class attribute
	 * @param classAttribute the class
	 */
	public abstract void setClassAttribute(Attribute classAttribute);

	/**
	 * Get the class attribute
	 * @return the class attribute
	 */
	public abstract Attribute getClassAttribute();

	/**
	 * Get a prediction for the class based on the known
	 * values of the attributes. The prediction is solely based
	 * on the source
	 * @param source the source for which the prediction is made
	 * @param values the values of the attributes. if not stated otherwise
	 * the values for all attributes have to be present.
	 * @return an array containing the predicted class probabilities.
	 * The index of the predicted probablity is the same as the index
	 * of the class in {@link Attribute#getClassValues()}. The sum
	 * of the probabilities is 1.
	 * @throws Exception gets thrown if predicition couldn't be made, e.g.
	 * because no all needed values were provided
	 */
	public abstract double[] classify(String source,
			Map<Attribute, Object> values) throws Exception;
	
	/**
	 * Same as above but the prediction is based
	 * on a whole plan.
	 * @see #classify(String, Map)
	 */
	public abstract double[] classify(PlanOperator po,
			Map<Attribute, Object> values) throws Exception;

	/**
	 * This methods gets called initially with all metadata of the event history,
	 * so the learner can build up its models.
	 * @param data all metadata in the event history
	 * @throws Exception gets thrown if the learner can't be initialized
	 */
	public void initLearner(Map<PlanOperator, PreprocessedMetadata> data) throws Exception;
}