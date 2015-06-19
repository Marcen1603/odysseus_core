package cm.controller;

import cm.communication.dto.SocketInfo;
import cm.controller.listeners.MachineListViewListener;
import cm.data.ConnectionHandler;
import cm.data.DataHandler;
import cm.model.Collection;
import cm.model.Event;
import cm.model.Sensor;
import cm.view.ConnectionListCell;
import cm.view.MachineEventListCell;
import cm.view.MachineListCell;
import cm.view.SensorListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
    @FXML
    ListView<Sensor> sensorsList;

    ObservableList<Sensor> observableSensorList = FXCollections.observableArrayList();

    // Connections tab
    @FXML
    Button addConnectionButton;
    @FXML
    ListView<SocketInfo> mainConnectionList;
    List<ConnectionListCell> connectionListCells = new ArrayList<>();

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

        // Show the sensors
        sensorsList.setItems(observableSensorList);
        sensorsList.setCellFactory(listView -> new SensorListCell());
    }

    public void updateMachineView(Collection collection) {
        machineName.setText(collection.getName());
        machineEvents.setItems(DataHandler.getInstance().getCollectionEvents(collection));
        machineEvents.setCellFactory(listView -> new MachineEventListCell());
    }

    private void openNewCollectionWindow() {
        Parent root;
        try {
            InputStream inputStream = getClass().getResource("../bundles/Language_en.properties").openStream();
            ResourceBundle bundle = new PropertyResourceBundle(inputStream);
            root = FXMLLoader.load(getClass().getResource("../view/addCollection.fxml"), bundle);
            Stage stage = new Stage();
            stage.setTitle(bundle.getString("newCollectionTitle"));
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openNewConnectionWindow() {
        Parent root;
        try {
            InputStream inputStream = getClass().getResource("../bundles/Language_en.properties").openStream();
            ResourceBundle bundle = new PropertyResourceBundle(inputStream);
            root = FXMLLoader.load(getClass().getResource("../view/addConnection.fxml"), bundle);
            Stage stage = new Stage();
            stage.setTitle(bundle.getString("newConnectionTitle"));
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openAddCollection(ActionEvent actionEvent) {
        openNewCollectionWindow();
    }

    public void openAddConnection(ActionEvent actionEvent) {
        openNewConnectionWindow();
    }

    /**
     * Removes the selected connections
     *
     * @param actionEvent
     */
    public void removeConnections(ActionEvent actionEvent) {
        Iterator<ConnectionListCell> iterator = connectionListCells.iterator();
        while (iterator.hasNext()) {
            ConnectionListCell listCell = iterator.next();
            if (listCell.isCellSelected()) {
                ConnectionHandler.getInstance().removeConnection(listCell.getSocketInfo());

                iterator.remove();
            }
        }
    }

    /**
     * Saves the names of the connections
     *
     * @param actionEvent
     */
    public void saveConnectionChanges(ActionEvent actionEvent) {
        for (ConnectionListCell listCell : connectionListCells) {
            if (listCell.getSocketInfo() != null)
                listCell.getSocketInfo().setName(listCell.getNameOfConnection());
        }
    }

    public OverviewController getOverviewController() {
        return overviewIncludeController;
    }
}
