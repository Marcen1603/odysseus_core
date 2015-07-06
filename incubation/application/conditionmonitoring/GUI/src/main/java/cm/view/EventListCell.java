package cm.view;

import cm.data.ConnectionHandler;
import cm.data.DataHandler;
import cm.model.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Map;

/**
 * @author Tobias
 * @since 03.04.2015.
 */
public class EventListCell extends ListCell<Event> {

    private Parent root;

    @FXML Label eventConnectionName;
    @FXML Label machineEventEventDescriptionLabel;
    @FXML Label machineEventAttributesLabel;
    @FXML Label machineEventAnomalyScore;
    @FXML Pane machineEventColorBar;

    public EventListCell() {
        super();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("event.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateItem(Event item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            Event event = getListView().getItems().get(super.getIndex());

            Double anomalyScore = 0.0;
            String eventAttributes = "";
            Map<String, String> attributes = event.getAttributes();
            for (String attributeName : attributes.keySet()) {
                if (attributeName.equalsIgnoreCase("anomalyScore")) {
                    anomalyScore = Double.parseDouble(attributes.get(attributeName));
                } else {
                    eventAttributes += attributeName + ": " + attributes.get(attributeName) + "\n";
                }
            }

            machineEventEventDescriptionLabel.setText(item.getConnection().getSocketInfo().getDescription());
            machineEventAttributesLabel.setText(eventAttributes);
            if (anomalyScore > 0)
                machineEventAnomalyScore.setText("Anomaly Score: " + anomalyScore.toString());
            else
                machineEventAnomalyScore.setText("");

            String color = getColor(anomalyScore);
            machineEventColorBar.setStyle(color + " " + "-fx-effect: innershadow(gaussian, black, 4, 0.4, 0, 0)");

            eventConnectionName.setText(item.getConnection().getSocketInfo().getConnectionName());


            setGraphic(root);
        }
    }

    private String getColor(double anomalyScore) {
        if (anomalyScore < 0.2) {
            return "-fx-background-color: green;";
        } else if (anomalyScore < 0.5) {
            return "-fx-background-color: greenyellow;";
        } else if (anomalyScore < 0.8) {
            return "-fx-background-color: yellow;";
        } else if (anomalyScore < 0.9) {
            return "-fx-background-color: orange;";
        } else if (anomalyScore < 0.95) {
            return "-fx-background-color: orangered;";
        }
        return "-fx-background-color: red;";
    }


}
