package de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation.model;

public interface IPushable {
	
	public double push(double liters);
	public boolean isOutPipeFlowOpen();

}
