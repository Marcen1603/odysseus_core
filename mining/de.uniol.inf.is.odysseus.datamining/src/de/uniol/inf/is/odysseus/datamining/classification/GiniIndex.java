package de.uniol.inf.is.odysseus.datamining.classification;

import java.util.Map;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

/**
 * This class represents a evaluation measure using the gini index
 * 
 * @author Sven Vorlauf
 * 
 * @param <T>
 *            the type of the IMetaAttribute
 */
public class GiniIndex<T extends IMetaAttribute> extends AbstractGainMeasure<T> {

	/**
	 * create a new gini index with a given tie and a given probability
	 * 
	 * @param probability
	 *            the probability that the chosen attribute is not the best one
	 * @param tie
	 *            the bound to split the node when the difference between the
	 *            best two measures is lower than this bound
	 */
	public GiniIndex(Double probability, Double tie) {
		super(probability, tie);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.datamining.classification.AbstractGainMeasure
	 * #getPartitionQuality(java.util.Map)
	 */
	@Override
	protected Double getPartitionQuality(Map<Object, Integer> vector) {
		Double quality = 1D;
		Integer tupleCount = getTupleCount(vector);
		for (Integer classCount : vector.values()) {
			Double probability = ((double) classCount) / tupleCount;
			quality -= Math.pow(probability, 2);
		}
		return quality;
	}
}
