package cm.configuration;

import cm.model.Collection;

import java.util.List;

/**
 * @author Tobias
 * @since 17.06.2015.
 */
public class Configuration {

    // To which query the client has to connect and which query IDs and outputs it has to connect to
    public List<ConnectionInformation> connectionInformation;

    // Which of the attributes of the connections should be visualized
    public List<VisualizationInformation> visualizationInformation;

    // Which collections should be created
    public List<CollectionInformation> collections;
}
