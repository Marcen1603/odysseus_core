package de.uniol.inf.is.odysseus.condition.rest.dto.response;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.condition.rest.datatypes.ConnectionInformation;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.VisualizationInformation;

/**
 * @author Tobias
 * @since 17.06.2015.
 */
public class CollectionInformation {
    private String name;
    private List<ConnectionInformation> connectionInformation;
    private List<VisualizationInformation> visualizationInformation;
    
    public CollectionInformation() {
    	this.connectionInformation = new ArrayList<>();
    	this.visualizationInformation = new ArrayList<VisualizationInformation>();
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
}
