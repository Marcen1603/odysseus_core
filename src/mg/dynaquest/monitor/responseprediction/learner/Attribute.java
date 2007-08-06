package mg.dynaquest.monitor.responseprediction.learner;

import mg.dynaquest.monitor.responseprediction.preprocessor.PreprocessedMetadata;


/**
 * Class representing an attribute/characteristik used to classify a request.
 * The attribute values have to be nominal or ordinal
 * @author Jonas Jacobi
 *
 * @param <T> type of the different attribute values (dimension)
 * @param <K> inputtype needed by the learner
 * @see ILearner
 */
public abstract class Attribute<T, K> {

	private T[] classValues;

	/**
	 * Constructor
	 * @param values the possible values of the attribute,
	 * if the attribute is ordinal, the order is determined
	 * by the order the values are passed
	 */
	public Attribute(T... values) {
		setClassValues(values);
	}

	/**
	 * Get the class/attribute value from a {@link PreprocessedMetadata} object
	 * @param d the object containing the value
	 * @return the class/attribute value
	 */
	public abstract K classify(PreprocessedMetadata d);

	/**
	 * Set the class/attribute values of the attribute.
	 * if the attribute is ordinal, the order is determined
	 * by the order the values are passed
	 * @param values
	 */
	protected void setClassValues(T... values) {
		this.classValues = values;
	}

	/**
	 * Get the possible attribute values
	 * @return the possible attribute values
	 */
	public T[] getClassValues() {
		return this.classValues;
	}

}