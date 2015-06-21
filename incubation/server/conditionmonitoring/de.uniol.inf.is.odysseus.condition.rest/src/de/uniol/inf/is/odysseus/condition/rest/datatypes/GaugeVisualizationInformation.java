package de.uniol.inf.is.odysseus.condition.rest.datatypes;

public class GaugeVisualizationInformation extends VisualizationInformation {

	private double minValue;
	private double maxValue;
	
	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}
}
