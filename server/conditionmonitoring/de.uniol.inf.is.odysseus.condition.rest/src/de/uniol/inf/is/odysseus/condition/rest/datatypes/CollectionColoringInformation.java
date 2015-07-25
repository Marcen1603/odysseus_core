package de.uniol.inf.is.odysseus.condition.rest.datatypes;

/**
 * Information about the coloring in the client application
 * 
 * @author Tobias Brandt
 *
 */
public class CollectionColoringInformation {

	private ConnectionInformation connectionInformation;
	private String attribute;
	private double minValue;
	private double maxValue;

	public ConnectionInformation getConnectionInformation() {
		return connectionInformation;
	}

	public void setConnectionInformation(ConnectionInformation connectionInformation) {
		this.connectionInformation = connectionInformation;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

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
