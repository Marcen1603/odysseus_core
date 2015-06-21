package cm.controller;

import cm.communication.CommunicationService;
import cm.communication.socket.SocketReceiver;
import cm.configuration.VisualizationInformation;
import cm.data.DataHandler;
import cm.model.Event;
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

    private Map<SocketReceiver, SimpleMetroArcGauge> socketMap;
    private Map<SimpleMetroArcGauge, VisualizationInformation> informationMap;
    private AreaChart chart;
    private XYChart.Series series;
    private int counter;

    @FXML
    FlowPane overviewFlowPane;

    @FXML
    private void initialize() {
        socketMap = new HashMap<>();
        informationMap = new HashMap<>();
    }

    public void addGauge(SimpleMetroArcGauge gauge, VisualizationInformation visualizationInformation) {
        List<SocketReceiver> receivers = CommunicationService.getSocketReceivers(visualizationInformation.getConnectionInformation());
        for (SocketReceiver receiver : receivers) {
            DataHandler.getInstance().addObserverForConnection(receiver, this);
            socketMap.put(receiver, gauge);
        }

        overviewFlowPane.getChildren().add(gauge);
        informationMap.put(gauge, visualizationInformation);
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
        series = streamData;
        streamData.setName(visualizationInformation.getAttribute());
        counter = 1;

        ac.getData().addAll(streamData);
        chart = ac;
        overviewFlowPane.getChildren().add(ac);
    }

    @Override
    public void update(Observable o, Object obj) {
        if (obj instanceof Event) {
            Event event = (Event) obj;
            for (SocketReceiver receiver : socketMap.keySet()) {
                if (receiver.equals(event.getConnection())) {
                    SimpleMetroArcGauge gauge = socketMap.get(receiver);
                    VisualizationInformation visualizationInformation = informationMap.get(gauge);
                    String attribute = event.getAttributes().get(visualizationInformation.getAttribute());
                    if (attribute != null) {
                        double value = Double.parseDouble(attribute);
                        gauge.setValue(value);
                        if (series != null)
                            series.getData().add(new XYChart.Data<>(counter++, value));
                    }
                }
            }
        }
    }
}
