package cm.controller;

import cm.communication.dto.SocketInfo;
import cm.controller.listeners.MachineListViewListener;
import cm.data.DataHandler;
import cm.model.Collection;
import cm.model.Event;
import cm.view.MachineEventListCell;
import cm.view.MachineListCell;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

public class MainController {


    // Overview tab
    @FXML
    Pane overviewInclude;
    @FXML
    OverviewController overviewIncludeController;

    @FXML
    ListView<Collection> machineList;

    @FXML
    ListView<Event> machineEventList;

    @FXML
    Label machineName;
    @FXML
    Label machineStatus;
    @FXML
    Label machinePrediction;
    @FXML
    Button newCollectionButton;

    @FXML
    ListView<Event> machineEvents;

    // Connections tab
    @FXML
    Button addConnectionButton;
    @FXML
    ListView<SocketInfo> mainConnectionList;

    public MainController() {

    }

    @FXML
    private void initialize() {

        // Events tab
        // ----------
        machineEventList.setItems(DataHandler.getInstance().getObservableEventList());
        machineEventList.setCellFactory(listView -> new MachineEventListCell());

        // Collections tab
        // ---------------
        machineList.setItems(DataHandler.getInstance().getObservableCollectionList());
        machineList.setCellFactory(listView -> new MachineListCell());
        // To react to clicks on the listView of collections
        machineList.getSelectionModel().getSelectedItems().addListener(new MachineListViewListener(this));
    }

    public void updateMachineView(Collection collection) {
        machineName.setText(collection.getName());
        machineEvents.setItems(DataHandler.getInstance().getCollectionEvents(collection));
        machineEvents.setCellFactory(listView -> new MachineEventListCell());
    }

    public OverviewController getOverviewController() {
        return overviewIncludeController;
    }
}
