package de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes;

import java.util.List;

import de.uniol.inf.is.odysseus.anomalydetection.rest.dto.response.CollectionInformation;

/**
 * Configuration for the client application which only has to show what the
 * configuration tells.
 * 
 * @author Tobias Brandt
 *
 */
public class ClientConfiguration {

	// To which query the client has to connect and which query IDs and outputs
	// it has to connect to
	private List<ConnectionInformation> connectionInformation;

	// Which collections should be created
	private List<CollectionInformation> collections;

	// For the event list in the overview
	private OverviewInformation overviewInformation;

	public List<ConnectionInformation> getConnectionInformation() {
		return connectionInformation;
	}

	public void setConnectionInformation(List<ConnectionInformation> connectionInformation) {
		this.connectionInformation = connectionInformation;
	}

	public List<CollectionInformation> getCollections() {
		return collections;
	}

	public void setCollections(List<CollectionInformation> collections) {
		this.collections = collections;
	}

	public OverviewInformation getOverviewInformation() {
		return overviewInformation;
	}

	public void setOverviewInformation(OverviewInformation overviewInformation) {
		this.overviewInformation = overviewInformation;
	}

}
