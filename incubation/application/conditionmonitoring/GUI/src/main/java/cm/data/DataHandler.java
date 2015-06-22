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
    private Map<Collection, ObservableList<Event>> collectionEventsMap;
    private Map<SocketInfo, List<Collection>> socketColletionsMap;

    private DataHandler() {
        this.observableEventList = FXCollections.observableArrayList();
        this.observableCollectionList = FXCollections.observableArrayList();
        this.observerMap = new HashMap<>();
        this.collectionEventsMap = new HashMap<>();
        this.socketColletionsMap = new HashMap<>();
    }

    public static DataHandler getInstance() {
        if (instance == null)
            instance = new DataHandler();
        return instance;
    }

    public void addCollection(Collection collection) {
        Platform.runLater(() -> observableCollectionList.add(collection));
    }

    public Collection getCollection(UUID identifier) {
        Optional<Collection> result = observableCollectionList.stream().filter(collection -> collection.getIdentifier().equals(identifier)).findFirst();
        if (result.isPresent())
            return result.get();
        return null;
    }

    public void addEvent(Event event) {
        // Add the new element at the top of the list to show it on the top of the ListView
        Platform.runLater(() -> {
            observableEventList.add(0, event);

            // Notify observers
            List<Observer> observers = this.observerMap.get(event.getConnection());
            if (observers != null)
                observers.stream().forEach(observer -> observer.update(null, event));

            // Add to lists for collections
            List<Collection> collections = socketColletionsMap.get(event.getConnection().getSocketInfo());
            if (collections != null) {
                for (Collection collection : collections) {
                    ObservableList<Event> events = collectionEventsMap.get(collection);
                    if (events != null) {
                        events.add(0, event);
                    }
                }
            }
        });
    }

    public ObservableList<Event> getCollectionEvents(Collection collection) {
        ObservableList<Event> collectionEvents = collectionEventsMap.get(collection);
        if (collectionEvents == null) {
            collectionEvents = FXCollections.observableArrayList();
            // The collection has the normal connections and the connection for the color
            List<SocketInfo> connections = new ArrayList<>();
            connections.addAll(collection.getConnections());
            connections.add(collection.getColorConnection());
            for (SocketInfo connection : connections) {
                for (Event event : observableEventList) {
                    if (event.getConnection().getSocketInfo().equals(connection)) {
                        // This event belongs to the collection
                        collectionEvents.add(event);
                    }
                }
                // Save, that the collection belongs to this connection (to make it faster to find it when new data arrives)
                List<Collection> connectionCollections = socketColletionsMap.get(connection);
                if (connectionCollections == null) {
                    connectionCollections = new ArrayList<>();
                    socketColletionsMap.put(connection, connectionCollections);
                }
                if (!connectionCollections.contains(collection))
                    connectionCollections.add(collection);
            }
            collectionEventsMap.put(collection, collectionEvents);
        }
        return collectionEvents;
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
