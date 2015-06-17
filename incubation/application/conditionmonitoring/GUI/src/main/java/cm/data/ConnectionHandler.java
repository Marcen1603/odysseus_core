package cm.data;

import cm.communication.dto.SocketInfo;
import cm.communication.socket.SocketReceiver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Iterator;

/**
 * @author Tobias
 * @since 16.06.2015.
 */
public class ConnectionHandler {

    private static ConnectionHandler instance;
    private ObservableList<SocketReceiver> connections;
    private ObservableList<SocketInfo> socketInfos;

    private ConnectionHandler() {
        connections = FXCollections.observableArrayList();
        socketInfos = FXCollections.observableArrayList();
    }

    public static ConnectionHandler getInstance() {
        if (instance == null) {
            instance = new ConnectionHandler();
        }
        return instance;
    }

    public void addConnection(SocketReceiver info) {
        connections.add(info);
        socketInfos.add(info.getSocketInfo());
    }

    public void removeConnection(SocketInfo info) {
        Iterator<SocketReceiver> iter = connections.iterator();
        while (iter.hasNext()) {
            SocketReceiver receiver = iter.next();
            if (receiver.getSocketInfo().equals(info)) {
                receiver.stopReceiver();
                iter.remove();
            }
        }

        Iterator<SocketInfo> infoIter = socketInfos.iterator();
        while(infoIter.hasNext()) {
            SocketInfo socketInfo = infoIter.next();
            if (socketInfo.equals(info)) {
                iter.remove();
            }
        }
    }

    public ObservableList<SocketInfo> getSocketInfos() {
        return this.socketInfos;
    }
}
