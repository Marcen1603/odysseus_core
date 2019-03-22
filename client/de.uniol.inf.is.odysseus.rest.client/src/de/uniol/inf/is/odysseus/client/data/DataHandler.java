package de.uniol.inf.is.odysseus.client.data;

import de.uniol.inf.is.odysseus.client.communication.socket.SocketReceiver;
import de.uniol.inf.is.odysseus.client.model.Event;

import java.util.*;

/**
 * @author Tobias Brandt
 * @since 26.04.2015.
 */
public class DataHandler {


    private static DataHandler instance;
    private Map<SocketReceiver, List<Observer>> observerMap;

    private DataHandler() {
        this.observerMap = new HashMap<>();
    }

    public static DataHandler getInstance() {
        if (instance == null)
            instance = new DataHandler();
        return instance;
    }

    /**
     * Add a new event to the top of the list(s). Automatically handles, which
     * lists will show the events.
     *
     * @param event
     */
    public void addEvent(Event event) {
        // Notify observers
        List<Observer> observers = this.observerMap.get(event.getConnection());
        if (observers != null) {
            observers.forEach(observer -> observer.update(null, event));
        }
    }

    /**
     * Add the observer as observer for the given connection. If a new event on
     * that connection arrives, the observer will be notified
     *
     * @param connection
     * @param observer
     */
    public void addObserverForConnection(SocketReceiver connection, Observer observer) {
        List<Observer> observers = this.observerMap.get(connection);
        if (observers == null) {
            observers = new ArrayList<>();
            this.observerMap.put(connection, observers);
        }
        observers.add(observer);
    }
}
