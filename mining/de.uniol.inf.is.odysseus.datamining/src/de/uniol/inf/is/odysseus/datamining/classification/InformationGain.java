package de.uniol.inf.is.odysseus.datamining.classification;

import java.util.Map;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public class InformationGain<T extends IMetaAttribute> extends
		AbstractGainMeasure<T> {

	public InformationGain(Double probability, Double  tie) {
		super(probability, tie);
	}

	@Override
	protected Double getPartitionQuality(Map<Object, Integer> vector) {
		Double quality = 0D;
		Integer tupleCount = getTupleCount(vector);
		for (Integer classCount : vector.values()) {
			Double probability = ((double) classCount) / tupleCount;
			quality -= probability * Math.log(probability) / Math.log(2);
		}
		return quality;
	}

}
