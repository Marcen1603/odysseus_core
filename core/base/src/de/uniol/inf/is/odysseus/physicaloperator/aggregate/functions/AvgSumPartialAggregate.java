package de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;


public class AvgSumPartialAggregate<R> implements IPartialAggregate<R> {
	Double aggValue;
	int aggCount;
	
	public AvgSumPartialAggregate(Double initAggValue, int initCount){
		this.aggValue = initAggValue;
		this.aggCount = initCount;
	}
	
	public AvgSumPartialAggregate(
			AvgSumPartialAggregate<R> avgSumPartialAggregate) {
		this.aggCount = avgSumPartialAggregate.aggCount;
		this.aggValue = new Double(avgSumPartialAggregate.aggValue);
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
	
	@Override
	public AvgSumPartialAggregate<R> clone(){
		return new AvgSumPartialAggregate(this);
	}
	

}
