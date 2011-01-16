package de.uniol.inf.is.odysseus.datamining.classification;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

/**
 * This class is an abstract class used to calculate a quality of the
 * partitioning by subtracting the quality of an partitioning from the quality
 * of the unpartitioned node
 * 
 * @author Sven Vorlauf
 * 
 * @param <T>
 *            the type of the IMetaAttribute
 */
public abstract class AbstractGainMeasure<T extends IMetaAttribute> extends
		AbstractAttributeEvaluationMeasure<T> {

	/**
	 * create a new gain measure with a given tie and a given probability
	 * 
	 * @param probability
	 *            the probability that the chosen attribute is not the best one
	 * @param tie
	 *            the bound to split the node when the difference between the
	 *            best two measures is lower than this bound
	 */
	public AbstractGainMeasure(Double probability, Double tie) {
		super(probability, tie);
	}

	/**get the quality of the partitioning created by splitting on a specific attribute
	 * @param dataLayer the distribution of the classes refering to an specific attribute
	 * @param tupleCount the number of tuples used to create the distribution
	 * @return the quality of the partitioning
	 */
	protected Double getAttributeQuality(
			HashMap<Object, HashMap<Object, Integer>> dataLayer,
			Integer tupleCount) {
		Double quality = 0D;
		
		// calculate the weighted average of the partition qualities
		for (Map<Object, Integer> dataVector : dataLayer.values()) {
			quality += ((double) getTupleCount(dataVector)) / tupleCount
					* getPartitionQuality(dataVector);
		}
		return quality;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.datamining.classification.
	 * AbstractAttributeEvaluationMeasure
	 * #getEvaluationMeasure(de.uniol.inf.is.odysseus
	 * .datamining.classification.DataCube, int)
	 */
	@Override
	public Double getEvaluationMeasure(DataCube<T> statistics, int attributeId) {
		// return the difference between the partitioning quality and the
		// quality of the unpartitioned node
		return getPartitionQuality(statistics.getClassCountVector())
				- getAttributeQuality(
						statistics.getClassCountLayer(attributeId),
						getTupleCount(statistics.getClassCountVector()));
	}

	/**
	 * get the quality of a partition
	 * @param vector the class distribution in the partition
	 * @return the partition quality
	 */
	protected abstract Double getPartitionQuality(Map<Object, Integer> vector);

}
