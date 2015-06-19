package cm.configuration;

/**
 * @author Tobias
 * @since 17.06.2015.
 */
public class VisualizationInformation {
    private ConnectionInformation connectionInformation;
    private String attribute;
    private VisualizationType visualizationType;
    private double minValue;
    private double maxValue;

    public VisualizationInformation() {
    }

    public VisualizationInformation(ConnectionInformation connectionInformation, String attribute, VisualizationType visualizationType, double minValue, double maxValue) {
        this.connectionInformation = connectionInformation;
        this.attribute = attribute;
        this.visualizationType = visualizationType;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

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
}
