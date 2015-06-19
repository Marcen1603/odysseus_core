package cm.view;

import cm.model.Collection;
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
public class MachineListCell extends ListCell<Collection> {

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
    public void updateItem(Collection item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            Collection collection = getListView().getItems().get(super.getIndex());
            machineName.setText(collection.getName());
            machineBackground.setStyle("-fx-background-color: greenyellow");
            setGraphic(root);
        }
    }

}
