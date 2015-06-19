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

    public void addGauge(VisualizationInformation visualizationInformation) {

        // Value
        SimpleMetroArcGauge gauge = new SimpleMetroArcGauge();
        gauge.setMinValue(visualizationInformation.getMinValue());
        gauge.setMaxValue(visualizationInformation.getMaxValue());
        gauge.setValue(0);

        List<SocketReceiver> receivers = CommunicationService.getSocketReceivers(visualizationInformation.getConnectionInformation());
        for (SocketReceiver receiver : receivers) {
            DataHandler.getInstance().addObserverForConnection(receiver, this);
            socketMap.put(receiver, gauge);
        }

        // Style
        String colorSchemeClass = "colorscheme-green-to-red-10";
        gauge.getStyleClass().add(colorSchemeClass);

        for (int i = 0; i < 10; i++) {
            Segment segment = new PercentSegment(gauge, i * 10, (i + 1) * 10);
            gauge.segments().add(segment);
        }

        String animatedStyle = "-fxx-animated: YES;";
        gauge.setStyle(animatedStyle);

        overviewFlowPane.getChildren().add(gauge);
        informationMap.put(gauge, visualizationInformation);
    }

    public void addAreaChart(VisualizationInformation visualizationInformation) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);
        final AreaChart<Number, Number> ac =
                new AreaChart<Number, Number>(xAxis, yAxis);
        ac.setTitle("Chart");

        XYChart.Series seriesMay = new XYChart.Series();
        series = seriesMay;
        seriesMay.setName(visualizationInformation.getAttribute());
        counter = 1;

        ac.getData().addAll(seriesMay);
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
