package de.uniol.inf.is.odysseus.client.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.client.communication.socket.SocketReceiver;
import de.uniol.inf.is.odysseus.rest.dto.response.SocketInfo;

/**
 * @author Tobias Brandt
 * @since 16.06.2015.
 */
public class ConnectionHandler {

	private static ConnectionHandler instance;
	private List<SocketReceiver> connections;
	private List<SocketInfo> socketInformation;

	private ConnectionHandler() {
		connections = new ArrayList<>();
		socketInformation = new ArrayList<>();
	}

	public static ConnectionHandler getInstance() {
		if (instance == null) {
			instance = new ConnectionHandler();
		}
		return instance;
	}

	public void addConnection(SocketReceiver info) {
		connections.add(info);
		socketInformation.add(info.getSocketInfo());
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

		Iterator<SocketInfo> infoIter = socketInformation.iterator();
		while (infoIter.hasNext()) {
			SocketInfo socketInfo = infoIter.next();
			if (socketInfo.equals(info)) {
				infoIter.remove();
			}
		}
	}

	public List<SocketInfo> getSocketInformation() {
		return this.socketInformation;
	}
}
