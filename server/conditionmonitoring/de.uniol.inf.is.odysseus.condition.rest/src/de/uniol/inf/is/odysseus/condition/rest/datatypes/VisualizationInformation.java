package de.uniol.inf.is.odysseus.condition.rest.datatypes;

import java.util.UUID;

public class VisualizationInformation {
	
	private ConnectionInformation connectionInformation;
	private String attribute;
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
