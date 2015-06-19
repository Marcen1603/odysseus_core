package cm.view;

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
public class MachineEventListCell extends ListCell<Event> {

    private Parent root;

    @FXML Label machineEventMachineNameLabel;
    @FXML Label machineEventEventDescriptionLabel;
    @FXML Label machineEventAnomalyScore;
    @FXML Pane machineEventColorBar;

    public MachineEventListCell() {
        super();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("machineEvent.fxml"));
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
            String eventDescription = "";
            Map<String, String> attributes = event.getAttributes();
            for (String attributeName : attributes.keySet()) {
                if (attributeName.equalsIgnoreCase("anomalyScore")) {
                    anomalyScore = Double.parseDouble(attributes.get(attributeName));
                } else {
                    eventDescription += attributeName + ": " + attributes.get(attributeName) + "\n";
                }
            }

            machineEventEventDescriptionLabel.setText(eventDescription);
            machineEventAnomalyScore.setText(anomalyScore.toString());

            String color = getColor(anomalyScore);
            machineEventColorBar.setStyle(color + " " + "-fx-effect: innershadow(gaussian, black, 4, 0.4, 0, 0)");

            machineEventMachineNameLabel.setText(item.getConnection().getSocketInfo().getName());

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
