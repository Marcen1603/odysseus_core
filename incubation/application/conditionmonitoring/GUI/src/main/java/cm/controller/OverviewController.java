package cm.controller;

import cm.configuration.VisualizationInformation;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import jfxtras.labs.scene.control.gauge.linear.SimpleMetroArcGauge;

/**
 * @author Tobias
 * @since 17.06.2015.
 */
public class OverviewController {

    @FXML
    FlowPane overviewFlowPane;

    @FXML
    private void initialize() {

    }

    public void addGauge(VisualizationInformation visualizationInformation) {
        SimpleMetroArcGauge gauge = new SimpleMetroArcGauge();
        gauge.setMinValue(visualizationInformation.minValue);
        gauge.setMaxValue(visualizationInformation.maxValue);
        gauge.setValue(1);
        overviewFlowPane.getChildren().add(gauge);
    }
}
