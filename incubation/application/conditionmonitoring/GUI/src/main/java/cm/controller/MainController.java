package cm.controller;

import cm.controller.listeners.MachineListViewListener;
import cm.data.DataHandler;
import cm.model.Collection;
import cm.model.MachineEvent;
import cm.model.Sensor;
import cm.view.MachineEventListCell;
import cm.view.MachineListCell;
import cm.view.SensorListCell;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class MainController {

    @FXML
    ListView<Collection> machineList;

    @FXML
    ListView<MachineEvent> machineEventList;

    @FXML
    Label machineName;
    @FXML
    Label machineStatus;
    @FXML
    Label machinePrediction;
    @FXML
    Button newCollectionButton;

    @FXML
    ListView<MachineEvent> machineEvents;
    @FXML
    ListView<Sensor> sensorsList;

    @FXML Button addConnectionButton;

    ObservableList<Sensor> observableSensorList = FXCollections.observableArrayList();

    public MainController() {

    }

    @FXML
    private void initialize() {

        machineList.setItems(DataHandler.getInstance().getObservableCollectionList());
        machineList.setCellFactory(listView -> new MachineListCell());
        machineEventList.setItems(DataHandler.getInstance().getObservableEventList());
        machineEventList.setCellFactory(listView -> new MachineEventListCell());

        // To react to clicks on the listView of machines
        machineList.getSelectionModel().getSelectedItems().addListener(new MachineListViewListener(this));

        // Add a few sensors
        sensorsList.setItems(observableSensorList);

        sensorsList.setCellFactory(listView -> new SensorListCell());
    }

    public void updateMachineView(Collection collection) {
        machineName.setText(collection.getName());
        machineStatus.setText(collection.getState());
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
}
