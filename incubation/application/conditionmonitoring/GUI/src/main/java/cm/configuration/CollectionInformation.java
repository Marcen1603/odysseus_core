package cm.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Tobias
 * @since 17.06.2015.
 */
public class CollectionInformation {
    private String name;
    private List<ConnectionInformation> connectionInformation;
    private List<VisualizationInformation> visualizationInformation;
    private UUID identifier = UUID.randomUUID();

    public CollectionInformation() {
        this.connectionInformation = new ArrayList<>();
        this.visualizationInformation = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ConnectionInformation> getConnectionInformation() {
        return connectionInformation;
    }

    public void setConnectionInformation(List<ConnectionInformation> connectionInformation) {
        this.connectionInformation = connectionInformation;
    }

    public List<VisualizationInformation> getVisualizationInformation() {
        return visualizationInformation;
    }

    public void setVisualizationInformation(List<VisualizationInformation> visualizationInformation) {
        this.visualizationInformation = visualizationInformation;
    }

    public void addVisualizationInformation(VisualizationInformation visualizationInformation) {
        this.visualizationInformation.add(visualizationInformation);
    }

    public UUID getIdentifier() {
        return identifier;
    }
}

