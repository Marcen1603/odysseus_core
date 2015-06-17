package cm.data;

import cm.communication.dto.SocketInfo;
import cm.communication.socket.SocketReceiver;
import cm.model.Collection;
import cm.model.MachineEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tobias
 * @since 26.04.2015.
 */
public class DataHandler {

    private static DataHandler instance;
    private ObservableList<MachineEvent> observableEventList;
    private ObservableList<Collection> observableCollectionList;

    private DataHandler() {
        this.observableEventList = FXCollections.observableArrayList();
        this.observableCollectionList = FXCollections.observableArrayList();
    }

    public static DataHandler getInstance() {
        if (instance == null)
            instance = new DataHandler();
        return instance;
    }

    public void addCollection(Collection collection) {
        Platform.runLater(() -> observableCollectionList.add(collection));
    }

    public void addMachineEvent(MachineEvent event) {
        // Add the new element at the top of the list to show it on the top of the ListView
        Platform.runLater(() -> observableEventList.add(0, event));
    }

    public ObservableList<MachineEvent> getCollectionEvents(Collection collection) {
        List<MachineEvent> events = new ArrayList<>();
        for (MachineEvent event : observableEventList) {
            for (SocketInfo connection : collection.getConnections()) {
                if (event.getConnection().getSocketInfo().equals(connection)) {
                    events.add(event);
                }
            }
        }
        ObservableList<MachineEvent> observableList = FXCollections.observableList(events);
        return observableList;
    }

    public ObservableList<MachineEvent> getObservableEventList() {
        return observableEventList;
    }

    public ObservableList<Collection> getObservableCollectionList() {
        return observableCollectionList;
    }
}
