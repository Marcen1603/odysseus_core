package de.uniol.inf.is.odysseus.datamining.classification;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;


public abstract class AbstractGainMeasure<T extends IMetaAttribute> extends AbstractAttributeEvaluationMeasure<T> {

	public AbstractGainMeasure(Double probability, Double tie) {
		super(probability, tie);
	}



	@Override
	public Double getEvaluationMeasure(DataCube<T> statistics, int attributeId) {
		
		return getPartitionQuality(statistics.getClassCountVector())- getAttributeQuality(statistics.getClassCountLayer(attributeId), getTupleCount(statistics.getClassCountVector())); 
	}
	
	
	
	protected Double getAttributeQuality(HashMap<Object, HashMap<Object, Integer>> hashMap, Integer tupleCount){
		Double quality = 0D;
		for(Map<Object, Integer> vector : hashMap.values()){
			
			quality += ((double)getTupleCount(vector))/ tupleCount * getPartitionQuality(vector);
		}
		return quality;
	}
	
	protected abstract Double getPartitionQuality(Map<Object,Integer> vector);


}
