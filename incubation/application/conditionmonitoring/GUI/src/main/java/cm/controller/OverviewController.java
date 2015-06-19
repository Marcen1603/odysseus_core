package cm.controller;

import cm.communication.CommunicationService;
import cm.communication.socket.SocketReceiver;
import cm.configuration.VisualizationInformation;
import cm.data.ConnectionHandler;
import cm.data.DataHandler;
import cm.model.Event;
import javafx.fxml.FXML;
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
        String animatedStyle = "-fxx-animated: YES;";
        gauge.setStyle(animatedStyle);

        for (int i = 0; i < 10; i++) {
            Segment segment = new PercentSegment(gauge, i * 10, (i+1) * 10 );
            gauge.segments().add(segment);
        }

        String colorSchemeClass = "colorscheme-green-to-red-10";
        gauge.getStyleClass().add(colorSchemeClass);

        overviewFlowPane.getChildren().add(gauge);
        informationMap.put(gauge, visualizationInformation);
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
                    }
                }
            }
        }
    }
}
