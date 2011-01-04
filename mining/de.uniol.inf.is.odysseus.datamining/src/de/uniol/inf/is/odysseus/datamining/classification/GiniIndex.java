package de.uniol.inf.is.odysseus.datamining.classification;

import java.util.Map;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public class GiniIndex<T extends IMetaAttribute> extends AbstractGainMeasure<T> {

	public GiniIndex(Double probability, Double tie) {
		super(probability,tie);
			}

	@Override
	protected Double getPartitionQuality(Map<Object, Integer> vector) {
		Double quality = 1D;
		Integer tupleCount = getTupleCount(vector);
		for(Integer  classCount: vector.values()){
			 Double probability = ((double)classCount)/ tupleCount;
			quality -= Math.pow(probability,2);
		}
		return quality;
	}	
}
