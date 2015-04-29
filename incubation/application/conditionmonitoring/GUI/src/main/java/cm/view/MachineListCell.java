package cm.view;

import cm.model.Machine;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Created by Tobi on 15.03.2015.
 */
public class MachineListCell extends ListCell<Machine> {

    private Parent root;

    @FXML
    Label machineName;
    @FXML
    Label machineState;
    @FXML
    AnchorPane machineBackground;

    public MachineListCell() {
        super();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("machineListCell.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateItem(Machine item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            Machine machine = getListView().getItems().get(super.getIndex());
            machineName.setText(machine.getName());
            machineState.setText(machine.getState());

            if (machine.getState().equals(Machine.BAD_STATE)) {
                machineBackground.setStyle("-fx-background-color: red");
            } else {
                machineBackground.setStyle("-fx-background-color: greenyellow");
            }

            setGraphic(root);
        }
    }

}
