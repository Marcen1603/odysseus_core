package cm.configuration;

import java.util.List;

/**
 * @author Tobias
 * @since 17.06.2015.
 */
public class Configuration {

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
