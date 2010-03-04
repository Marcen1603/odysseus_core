package de.uniol.inf.is.odysseus.action.dataSources.generator;

public class Tool {
	private int limit1;
	private int limit2;
	private double usageRate;

	public Tool(int limit1, int limit2) {
		this.limit1 = limit1;
		this.limit2 = limit2;
		this.usageRate = 0.0d;
	}
	
	public void increaseUsageRate(double value){
		this.usageRate += value;
	}
	
	public double getUsageRate() {
		return usageRate;
	}
	
	public boolean isLimit1Hit(){
		return this.usageRate >= this.limit1;
	}
	
	public boolean isLimit2Hit(){
		return this.usageRate >= this.limit2;
	}
	
	
	
	

}
