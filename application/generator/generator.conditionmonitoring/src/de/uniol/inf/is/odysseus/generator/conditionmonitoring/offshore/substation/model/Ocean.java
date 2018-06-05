package de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation.model;

public class Ocean implements ISuckable, IPushable {

	@Override
	public double push(double liters) {
		return liters;
	}

	@Override
	public boolean isOutPipeFlowOpen() {
		return true;
	}

	@Override
	public double suck(double liters) {
		return liters;
	}

	@Override
	public boolean isInPipeFlowOpen() {
		return true;
	}

}
