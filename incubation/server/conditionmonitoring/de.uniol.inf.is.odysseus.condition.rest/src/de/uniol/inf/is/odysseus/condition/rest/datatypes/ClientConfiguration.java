package de.uniol.inf.is.odysseus.condition.rest.datatypes;

import java.util.List;

import de.uniol.inf.is.odysseus.condition.rest.dto.response.CollectionInformation;

public class ClientConfiguration {

	 // To which query the client has to connect and which query IDs and outputs it has to connect to
    public List<ConnectionInformation> connectionInformation;

    // Which of the attributes of the connections should be visualized
    public List<VisualizationInformation> visualizationInformation;

    // Which collections should be created
    public List<CollectionInformation> collections;
	
}
