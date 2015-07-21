package de.uniol.inf.is.odysseus.condition.rest.datatypes;

public class GaugeVisualizationInformation extends VisualizationInformation {

	private double minValue;
	private double maxValue;
	// If you have values between 0 and 1 (for example) you can let it stretch
	// to 0 - 100
	private boolean stretch;

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

	public boolean isStretch() {
		return stretch;
	}

	public void setStretch(boolean stretch) {
		this.stretch = stretch;
	}

}
