package de.uniol.inf.is.odysseus.datamining.classification;

import java.util.Map;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

/**
 * This class is an abstract class used to calculate a quality of the
 * partitioning that is created by splitting a hoeffdingNode at a given
 * Attribute
 * 
 * @author Sven Vorlauf
 * 
 * @param <T>
 *            the type of the IMetaAttribute
 */
public abstract class AbstractAttributeEvaluationMeasure<T extends IMetaAttribute> {

	/**
	 * the probability that the chosen attribute is not the best one
	 */
	private Double probability;

	/**
	 * the bound to split the node when the difference between the best two
	 * measures is lower than this bound
	 */
	private Double tie;

	/**
	 * create a new evaluation measure with a given tie and a given probability
	 * 
	 * @param probability
	 *            the probability that the chosen attribute is not the best one
	 * @param tie
	 *            the bound to split the node when the difference between the
	 *            best two measures is lower than this bound
	 */
	public AbstractAttributeEvaluationMeasure(Double probability, Double tie) {
		this.tie = tie;
		this.probability = probability;
	}

	/**
	 * get the evaluation measure of an given attribute
	 * 
	 * @param statistics
	 *            the statistict to calculate the evaluation measure
	 * @param attribute
	 *            the attribute to get the measure from
	 * @return the evaluation measure of the attribute
	 */
	public abstract Double getEvaluationMeasure(DataCube<T> statistics,
			int attribute);

	/**
	 * get the hoeffding bound for a given number of tuples
	 * 
	 * @param tupleCount
	 *            the number of tuples
	 * @return the hoeffding bound
	 */
	private Double getHoeffdingBound(int tupleCount) {
		return Math.sqrt(Math.log(1 / probability) / (2 * tupleCount));
	}

	/**
	 * get the attribute that should be used to split the node
	 * 
	 * @param statistics
	 *            the statistict to calculete the evaluation measures
	 * @return the index of the attribute to be used or null if not to split
	 */
	public Integer getSplitAttribute(DataCube<T> statistics) {
		if (statistics.getAttributeCount() < 2) {
			// partitions with less than two tuples can not be split
			return null;
		}
		Integer bestAttribute = null;
		double bestQuality = 0D;
		double secondQuality = 0D;

		// get the best two attributes
		for (int i = 0; i < statistics.getAttributeCount(); i++) {
			Double quality = getEvaluationMeasure(statistics, i);
			if (quality > bestQuality) {
				secondQuality = bestQuality;
				bestQuality = quality;
				bestAttribute = i;
			} else if (quality > secondQuality) {
				secondQuality = quality;
			}
		}

		// calculate the hoeffding bound
		Double bound = getHoeffdingBound(getTupleCount(statistics
				.getClassCountVector()));

		double difference = bestQuality - secondQuality;

		// return the best attribute if the difference is higher than the
		// hoeffding bound or lower than the tie, null else
		return difference > bound || (tie != null && bound < tie) ? bestAttribute
				: null;
	}

	/**
	 * get the total number of tuples in a class distribution
	 * 
	 * @param vector
	 *            the class distribution
	 * @return the total number of tuples
	 */
	protected Integer getTupleCount(Map<Object, Integer> vector) {
		Integer count = 0;
		for (Integer classCount : vector.values()) {
			count += classCount;
		}
		return count;
	}
}
