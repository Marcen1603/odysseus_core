package de.uniol.inf.is.odysseus.action.dataSources.generator;

public class Tool {
	private int limit1;
	private int limit2;
	private double usageRate;
	private int id;

	public Tool(int limit1, int limit2, int toolID) {
		this.limit1 = limit1;
		this.limit2 = limit2;
		this.id = toolID;
		
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
	
	public int getId() {
		return id;
	}
	
	

}
