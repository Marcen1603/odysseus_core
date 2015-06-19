package cm.data;

import cm.communication.dto.SocketInfo;
import cm.communication.socket.SocketReceiver;
import cm.model.Collection;
import cm.model.Event;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

/**
 * @author Tobias
 * @since 26.04.2015.
 */
public class DataHandler {

    private static DataHandler instance;
    private ObservableList<Event> observableEventList;
    private ObservableList<Collection> observableCollectionList;
    private Map<SocketReceiver, List<Observer>> observerMap;

    private DataHandler() {
        this.observableEventList = FXCollections.observableArrayList();
        this.observableCollectionList = FXCollections.observableArrayList();
        this.observerMap = new HashMap<>();
    }

    public static DataHandler getInstance() {
        if (instance == null)
            instance = new DataHandler();
        return instance;
    }

    public void addCollection(Collection collection) {
        Platform.runLater(() -> observableCollectionList.add(collection));
    }

    public void addEvent(Event event) {
        // Add the new element at the top of the list to show it on the top of the ListView
        Platform.runLater(() -> {
            observableEventList.add(0, event);
            // Notify observers
            List<Observer> observers = this.observerMap.get(event.getConnection());
            if (observers != null)
                observers.stream().forEach(observer -> observer.update(null, event));
        });
    }

    public ObservableList<Event> getCollectionEvents(Collection collection) {
        List<Event> events = new ArrayList<>();
        for (Event event : observableEventList) {
            for (SocketInfo connection : collection.getConnections()) {
                if (event.getConnection().getSocketInfo().equals(connection)) {
                    events.add(event);
                }
            }
        }
        return FXCollections.observableList(events);
    }

    public ObservableList<Event> getObservableEventList() {
        return observableEventList;
    }

    public ObservableList<Collection> getObservableCollectionList() {
        return observableCollectionList;
    }

    public void addObserverForConnection(SocketReceiver connection, Observer observer) {
        List<Observer> observers = this.observerMap.get(connection);
        if (observers == null) {
            observers = new ArrayList<>();
            this.observerMap.put(connection, observers);
        }
        observers.add(observer);
    }
}
