package cm.controller;

import cm.Main;
import cm.communication.CommunicationService;
import cm.communication.socket.SocketReceiver;
import cm.configuration.ConfigurationService;
import cm.configuration.VisualizationInformation;
import cm.configuration.VisualizationType;
import cm.data.DataHandler;
import cm.model.*;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.FlowPane;
import jfxtras.labs.scene.control.gauge.linear.SimpleMetroArcGauge;
import jfxtras.labs.scene.control.gauge.linear.elements.PercentSegment;
import jfxtras.labs.scene.control.gauge.linear.elements.Segment;

import java.util.*;

/**
 * @author Tobias
 * @since 17.06.2015.
 */
public class OverviewController implements Observer {

    private static final int NUMBER_OF_SEGMENTS = 10;

    private Map<SocketReceiver, List<VisualizationInformation>> visualizationInformationMap;
    private Map<VisualizationInformation, XYChart.Series> seriesMap;
    private Map<VisualizationInformation, SimpleMetroArcGauge> gaugeMap;
    private Map<VisualizationInformation, Integer> counterMap;

    @FXML
    FlowPane overviewFlowPane;

    @FXML
    private void initialize() {
        visualizationInformationMap = new HashMap<>();
        seriesMap = new HashMap<>();
        gaugeMap = new HashMap<>();
        counterMap = new HashMap<>();
    }

    public void addGauge(VisualizationInformation visualizationInformation) {

        // Create gauge
        // Value
        SimpleMetroArcGauge gauge = new SimpleMetroArcGauge();
        gauge.setMinValue(visualizationInformation.getMinValue());
        gauge.setMaxValue(visualizationInformation.getMaxValue());
        gauge.setValue(0);

        // Style
        gauge.segments().clear();
        for (int i = 0; i < NUMBER_OF_SEGMENTS; i++) {
            Segment segment = new PercentSegment(gauge, i * 100 / NUMBER_OF_SEGMENTS, (i + 1) * (100 / NUMBER_OF_SEGMENTS));
            gauge.segments().add(segment);
        }

//        String colorSchemeClass = "colorscheme-green-to-red-10";
//        gauge.getStyleClass().add(colorSchemeClass);

        // As the colorSchema does not work, just set the colors manually
        String animatedStyle = "-fxx-animated: YES;";
        String segments = "-fxx-segment0-color: #97b329;\n" +
                "    -fxx-segment1-color: #aacc2a;\n" +
                "    -fxx-segment2-color: #d4ea35;\n" +
                "    -fxx-segment3-color: #f2de31;\n" +
                "    -fxx-segment4-color: #fccb2e;\n" +
                "    -fxx-segment5-color: #f3a429;\n" +
                "    -fxx-segment6-color: #f18c23;\n" +
                "    -fxx-segment7-color: #f65821;\n" +
                "    -fxx-segment8-color: #f3351f;\n" +
                "    -fxx-segment9-color: #f61319;";
        gauge.setStyle(animatedStyle + "\n" + segments);

        // Data Connection
        addConnection(visualizationInformation);

        // Click listener to get to the linked collection (if there is one)
        if (visualizationInformation.getCollectionLink() != null) {
            MainController mainController = ConfigurationService.getInstance().getMainController();
            cm.model.Collection collection = DataHandler.getInstance().getCollection(visualizationInformation.getCollectionLink());
            gauge.setOnMouseClicked(event -> mainController.switchToCollection(collection));
        }

        overviewFlowPane.getChildren().add(gauge);
        gaugeMap.put(visualizationInformation, gauge);
    }

    public void addAreaChart(VisualizationInformation visualizationInformation) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);
        final AreaChart<Number, Number> ac =
                new AreaChart<>(xAxis, yAxis);
        ac.setTitle("Chart");

        XYChart.Series streamData = new XYChart.Series();
        streamData.setName(visualizationInformation.getAttribute());
        seriesMap.put(visualizationInformation, streamData);
        counterMap.put(visualizationInformation, 0);

        // Data Connection
        addConnection(visualizationInformation);

        ac.getData().addAll(streamData);
        overviewFlowPane.getChildren().add(ac);
    }

    private void addConnection(VisualizationInformation visualizationInformation) {
        List<SocketReceiver> receivers = CommunicationService.getSocketReceivers(visualizationInformation.getConnectionInformation());
        for (SocketReceiver receiver : receivers) {
            DataHandler.getInstance().addObserverForConnection(receiver, this);
            List<VisualizationInformation> visualizationInformationList = visualizationInformationMap.get(receiver);
            if (visualizationInformationList == null) {
                visualizationInformationList = new ArrayList<>();
                visualizationInformationMap.put(receiver, visualizationInformationList);
            }
            visualizationInformationList.add(visualizationInformation);
        }
    }

    @Override
    public void update(Observable o, Object obj) {
        if (obj instanceof Event) {
            Event event = (Event) obj;

            List<VisualizationInformation> visualizationInformation = visualizationInformationMap.get(event.getConnection());
            for (VisualizationInformation visualInfo : visualizationInformation) {
                String attribute = event.getAttributes().get(visualInfo.getAttribute());
                if (attribute != null) {
                    double value = Double.parseDouble(attribute);
                    if (visualInfo.getVisualizationType().equals(VisualizationType.GAUGE) && gaugeMap.get(visualInfo) != null) {
                        SimpleMetroArcGauge gauge = gaugeMap.get(visualInfo);
                        gauge.setValue(value);
                        String colorSchemeClass = "colorscheme-green-to-red-10";
                        gauge.getStyleClass().add(colorSchemeClass);
                    } else if (visualInfo.getVisualizationType().equals(VisualizationType.AREACHART) && seriesMap.get(visualInfo) != null) {
                        XYChart.Series series = seriesMap.get(visualInfo);
                        int counter = counterMap.get(visualInfo);
                        series.getData().add(new XYChart.Data<>(counter++, value));
                        counterMap.put(visualInfo, counter);
                    }
                }
            }
        }
    }
}
