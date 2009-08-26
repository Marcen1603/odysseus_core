package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.PartialAggregate;


public class AvgSumPartialAggregate<R> implements PartialAggregate<R> {
	Double aggValue;
	int aggCount;
	
	public AvgSumPartialAggregate(Double initAggValue, int initCount){
		this.aggValue = initAggValue;
		this.aggCount = initCount;
	}
	
	public Double getAggValue(){
		return aggValue;
	}
	
	public int getCount(){
		return aggCount;
	}
	
	public void addAggValue(Double toAdd){
		this.aggValue += toAdd;
		aggCount++;
	}
	
	public void setAggValue(Double newAggValue, int newCount){
		this.aggValue = newAggValue;
		aggCount = newCount;
	}
	
	
	

}
