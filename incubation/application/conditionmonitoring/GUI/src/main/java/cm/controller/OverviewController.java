package cm.controller;

import cm.communication.CommunicationService;
import cm.communication.socket.SocketReceiver;
import cm.configuration.AreaChartVisualizationInformation;
import cm.configuration.ConfigurationService;
import cm.configuration.GaugeVisualizationInformation;
import cm.configuration.VisualizationInformation;
import cm.data.DataHandler;
import cm.model.Event;
import cm.view.GaugeElement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.FlowPane;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author Tobias
 * @since 17.06.2015.
 */
public class OverviewController implements Observer {

    private Map<SocketReceiver, List<VisualizationInformation>> visualizationInformationMap;
    private Map<VisualizationInformation, XYChart.Series> seriesMap;
    private Map<VisualizationInformation, GaugeElement> gaugeMap;
    private Map<VisualizationInformation, Long> counterMap;

    @FXML
    FlowPane overviewFlowPane;

    @FXML
    private void initialize() {
        visualizationInformationMap = new HashMap<>();
        seriesMap = new HashMap<>();
        gaugeMap = new HashMap<>();
        counterMap = new HashMap<>();
    }

    public void addGauge(GaugeVisualizationInformation visualizationInformation) {

        GaugeElement gaugeElem = new GaugeElement(visualizationInformation);
        overviewFlowPane.getChildren().add(gaugeElem);

        // Data Connection
        addConnection(visualizationInformation);

        gaugeMap.put(visualizationInformation, gaugeElem);
    }

    public void addAreaChart(AreaChartVisualizationInformation visualizationInformation) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);
        final AreaChart<Number, Number> ac =
                new AreaChart<>(xAxis, yAxis);
        ac.setTitle(visualizationInformation.getTitle());
        xAxis.setForceZeroInRange(false);

        // Show the little circle at each data point
        ac.setCreateSymbols(visualizationInformation.isShowSymbols());

        if (visualizationInformation.getMaxElements() > 0) {
            // If we regularly delete elements animation does not look good
            ac.setAnimated(false);
        }

        XYChart.Series streamData = new XYChart.Series();
        streamData.setName(visualizationInformation.getAttribute());

        seriesMap.put(visualizationInformation, streamData);
        counterMap.put(visualizationInformation, 0L);

        // Data Connection
        addConnection(visualizationInformation);

        ac.getData().addAll(streamData);
        overviewFlowPane.getChildren().add(ac);

        // Click listener to get to the linked collection (if there is one)
        addClicklistener(ac, visualizationInformation);
    }

    private void addClicklistener(Node node, VisualizationInformation visualizationInformation) {
        // Click listener to get to the linked collection (if there is one)
        if (visualizationInformation.getCollectionLink() != null) {
            MainController mainController = ConfigurationService.getInstance().getMainController();
            node.setOnMouseClicked(event -> {
                cm.model.Collection collection = DataHandler.getInstance().getCollection(visualizationInformation.getCollectionLink());
                mainController.switchToCollection(collection);
            });
        }
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
                    if (visualInfo instanceof GaugeVisualizationInformation && gaugeMap.get(visualInfo) != null) {
                        GaugeElement gauge = gaugeMap.get(visualInfo);
                        gauge.setValue(value);
                    } else if (visualInfo instanceof AreaChartVisualizationInformation && seriesMap.get(visualInfo) != null) {
                        AreaChartVisualizationInformation areaChartVisualizationInformation = (AreaChartVisualizationInformation) visualInfo;
                        XYChart.Series series = seriesMap.get(visualInfo);

                        long timeAttribute = 0;
                        if (((AreaChartVisualizationInformation) visualInfo).getTimeAttribute() != null && !((AreaChartVisualizationInformation) visualInfo).getTimeAttribute().isEmpty()) {
                            String timeAttributeString = event.getAttributes().get(((AreaChartVisualizationInformation) visualInfo).getTimeAttribute());
                            timeAttribute = Long.parseLong(timeAttributeString);
                        } else {
                            timeAttribute = counterMap.get(visualInfo);
                            timeAttribute++;
                            counterMap.put(visualInfo, timeAttribute);
                        }


                        series.getData().add(new XYChart.Data<Number, Number>(timeAttribute, value));
                        if (areaChartVisualizationInformation.getMaxElements() > 0 && series.getData().size() > areaChartVisualizationInformation.getMaxElements()) {
                            series.getData().remove(0);
                        }
                    }
                }
            }
        }
    }
}
