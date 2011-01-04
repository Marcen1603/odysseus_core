package de.uniol.inf.is.odysseus.datamining.classification;

import java.util.Map;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public abstract class AbstractAttributeEvaluationMeasure<T extends IMetaAttribute> {

	private double probability;
	
	private double tie;

	public abstract Double getEvaluationMeasure(DataCube<T> statistics,
			int attribute);

	protected Integer getTupleCount(Map<Object, Integer> vector) {
		Integer count = 0;
		for (Integer classCount : vector.values()) {
			count += classCount;
		}
		return count;
	}

	public Integer getSplitAttribute(DataCube<T> statistics) {
		if(statistics.getAttributeCount() == 0){
			return null;
		}
		Integer bestAttribute = null;
		double bestQuality = 0D;
		double secondQuality = 0D;
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
		double difference = bestQuality - secondQuality;
		Double bound = getHoeffdingBound(statistics, probability);
		System.out.println(difference+" > "+bound +"?");
		return difference > bound  || bound < tie ? bestAttribute
				: null;
	}

	public AbstractAttributeEvaluationMeasure(Double probability, Double tie) {
		this.tie = tie;
		this.probability = probability;
	}

	private Double getHoeffdingBound(DataCube<T> statistics, Double quality) {
		return Math.sqrt(Math.log(1 / quality)
				/ (2 * getTupleCount(statistics.getClassCountVector())));
	}
}
