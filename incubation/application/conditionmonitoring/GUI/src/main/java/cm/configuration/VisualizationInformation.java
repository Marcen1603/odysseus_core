package cm.configuration;

import java.util.UUID;

public class VisualizationInformation {
	
	private VisualizationType visualizationType;
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

	public VisualizationType getVisualizationType() {
		return visualizationType;
	}

	public void setVisualizationType(VisualizationType visualizationType) {
		this.visualizationType = visualizationType;
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
