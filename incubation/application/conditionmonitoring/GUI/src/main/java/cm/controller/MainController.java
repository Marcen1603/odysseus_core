package cm.controller;

import cm.communication.dto.SocketInfo;
import cm.communication.rest.RestException;
import cm.communication.rest.RestService;
import cm.communication.socket.SocketReceiver;
import cm.controller.listeners.MachineListViewListener;
import cm.data.DataHandler;
import cm.model.Machine;
import cm.model.MachineEvent;
import cm.model.Sensor;
import cm.view.MachineEventListCell;
import cm.view.MachineListCell;
import cm.view.SensorListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class MainController {

    @FXML
    ListView<Machine> machineList;

    @FXML
    ListView<MachineEvent> machineEventList;

    @FXML
    Label machineName;
    @FXML
    Label machineStatus;
    @FXML
    Label machinePrediction;

    @FXML
    TextArea queryField;


    @FXML
    ListView<MachineEvent> machineEvents;
    @FXML ListView<Sensor> sensorsList;

    ObservableList<Sensor> observableSensorList = FXCollections.observableArrayList();

    public MainController() {

    }

    @FXML
    private void initialize() {

        // Create a few machines
        Machine machine1 = new Machine(0, "Wind Turbine Herakles", Machine.OK_STATE);
        Machine machine2 = new Machine(1, "Car", Machine.BAD_STATE);

        DataHandler.getInstance().addMachine(machine1);
        DataHandler.getInstance().addMachine(machine2);

        machineList.setItems(DataHandler.getInstance().getObservableMachineList());

        machineList.setCellFactory(listView -> new MachineListCell());

        // Create a few events
        MachineEvent event1 = new MachineEvent(machine1, "Temperature unexpected high");
        MachineEvent event2 = new MachineEvent(machine2, "Bearing will fail soon");

        DataHandler.getInstance().addMachineEvent(event1);
        DataHandler.getInstance().addMachineEvent(event2);

        machineEventList.setItems(DataHandler.getInstance().getObservableMachineEventList());

        machineEventList.setCellFactory(listView -> new MachineEventListCell());

        // To react to clicks on the listView of machines
        machineList.getSelectionModel().selectedItemProperty().addListener(new MachineListViewListener(this));

        // Add a few sensors
        Sensor temperatureSensor = new Sensor(machine1, "Temperature Sensor", 25.0, "OK");
        observableSensorList.add(temperatureSensor);
        sensorsList.setItems(observableSensorList);

        sensorsList.setCellFactory(listView -> new SensorListCell());

        String conditionQL = "#PARSER ConditionQL\n" +
                "#DOREWRITE false\n" +
                "#RUNQUERY\n" +
                "\n" +
                "{\n" +
                "\t\"msgType\"\t:\t\"ANOMALYDETECTION\",\n" +
                "\t\"algorithm\"\t:\t{\n" +
                "\t\t\t\t\t\t\"algorithm\"\t:\t\"VALUEAREA\",\n" +
                "\t\t\t\t\t\t\"parameters\":\t{\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\"minValue\"\t:\t\"20\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\"maxValue\"\t:\t\"25\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t},\n" +
                "\t\"stream\"\t:\t{\n" +
                "\t\t\t\t\t\t\"streamName\"\t:\t\"tempSensor\"\n" +
                "\t\t\t\t\t}\n" +
                "}";

        queryField.setText(conditionQL);
    }

    public void updateMachineView(Machine machine) {
        machineName.setText(machine.getName());
        machineStatus.setText(machine.getState());
        machineEvents.setItems(DataHandler.getInstance().getMachineEvents(machine));
        machineEvents.setCellFactory(listView -> new MachineEventListCell());
    }

    public void sendRequest(ActionEvent actionEvent) {
        String conditionQL = queryField.getText();

        try {
            String token = RestService.login("127.0.0.1", "System", "manager");
            SocketInfo socketInfo = RestService.runQuery("127.0.0.1", token, conditionQL);
            System.out.println("SocketInfo: " + socketInfo.getIp() + ":" + socketInfo.getPort() + ", Schema: " + socketInfo.getSchema());
            SocketReceiver receiver = new SocketReceiver(socketInfo);
        } catch (RestException e) {
            e.printStackTrace();
        }
    }
}
