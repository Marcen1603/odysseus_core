package de.uniol.inf.is.odysseus.condition.rest.datatypes;

import java.util.UUID;

public class VisualizationInformation {
	
	private ConnectionInformation connectionInformation;
	private String attribute;
	private VisualizationType visualizationType;
	private double minValue;
	private double maxValue;
	private String title;
	private UUID collectionLink;

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

	public VisualizationType getVisualizationType() {
		return visualizationType;
	}

	public void setVisualizationType(VisualizationType visualizationType) {
		this.visualizationType = visualizationType;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public UUID getCollectionLink() {
		return collectionLink;
	}

	public void setCollectionLink(UUID collectionLink) {
		this.collectionLink = collectionLink;
	}
}
