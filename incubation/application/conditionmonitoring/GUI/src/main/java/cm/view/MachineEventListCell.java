package cm.view;

import cm.model.MachineEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;

/**
 * Created by Tobi on 03.04.2015.
 */
public class MachineEventListCell extends ListCell<MachineEvent> {

    private Parent root;

    @FXML Label machineEventMachineNameLabel;
    @FXML Label machineEventEventDescriptionLabel;

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
    public void updateItem(MachineEvent item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            MachineEvent event = getListView().getItems().get(super.getIndex());
            machineEventMachineNameLabel.setText(event.getMachine().getName());
            machineEventEventDescriptionLabel.setText(event.getDescription());

            setGraphic(root);
        }
    }
}
