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
    private List<GaugeVisualizationInformation> gaugeVisualizationInformation;
    private List<AreaChartVisualizationInformation> areaChartVisualizationInformation;
    private UUID identifier = UUID.randomUUID();

    public CollectionInformation() {
        this.connectionInformation = new ArrayList<>();
        this.gaugeVisualizationInformation = new ArrayList<>();
        this.areaChartVisualizationInformation = new ArrayList<>();
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

    public List<GaugeVisualizationInformation> getGaugeVisualizationInformation() {
        return gaugeVisualizationInformation;
    }

    public void setGaugeVisualizationInformation(List<GaugeVisualizationInformation> gaugeVisualizationInformation) {
        this.gaugeVisualizationInformation = gaugeVisualizationInformation;
    }

    public List<AreaChartVisualizationInformation> getAreaChartVisualizationInformation() {
        return areaChartVisualizationInformation;
    }

    public void setAreaChartVisualizationInformation(List<AreaChartVisualizationInformation> areaChartVisualizationInformation) {
        this.areaChartVisualizationInformation = areaChartVisualizationInformation;
    }

    public UUID getIdentifier() {
        return identifier;
    }
}

