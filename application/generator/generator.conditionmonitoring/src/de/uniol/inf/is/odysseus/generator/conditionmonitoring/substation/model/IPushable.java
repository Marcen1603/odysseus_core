package de.uniol.inf.is.odysseus.generator.conditionmonitoring.substation.model;

public interface IPushable {
	
	public double push(double liters);
	public boolean isOutPipeFlowOpen();

}
