package cm.data;

import cm.model.Machine;
import cm.model.MachineEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tobias
 * @since 26.04.2015.
 */
public class DataHandler {

    private static DataHandler instance;
    private ObservableList<MachineEvent> observableMachineEventList;
    private ObservableList<Machine> observableMachineList;

    private DataHandler() {
        this.observableMachineEventList = FXCollections.observableArrayList();
        this.observableMachineList = FXCollections.observableArrayList();
    }

    public static DataHandler getInstance() {
        if (instance == null)
            instance = new DataHandler();
        return instance;
    }

    public void addMachine(Machine machine) {
        Platform.runLater(() -> observableMachineList.add(machine));
    }

    public void addMachineEvent(MachineEvent event) {
        // Add the new element at the top of the list to show it on the top of the ListView
        Platform.runLater(() -> observableMachineEventList.add(0, event));
    }

    public Machine getMachine(int id) {
        List<Machine> machines = observableMachineList.stream().filter(m -> m.getId() == id).collect(Collectors.toList());
        if (!machines.isEmpty())
            return machines.get(0);
        return null;
    }

    public ObservableList<MachineEvent> getMachineEvents(Machine machine) {
        List<MachineEvent> events = observableMachineEventList.stream().filter(e -> e.getMachine().equals(machine)).collect(Collectors.toList());
        ObservableList<MachineEvent> observableList = FXCollections.observableList(events);
        return observableList;
    }

    public ObservableList<MachineEvent> getObservableMachineEventList() {
        return observableMachineEventList;
    }

    public ObservableList<Machine> getObservableMachineList() {
        return observableMachineList;
    }
}
