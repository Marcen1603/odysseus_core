package de.uniol.inf.is.odysseus.broker.movingobjects.simulation;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.broker.movingobjects.generator.MovingObjectsStreamType;

public class MovingObjectsServer {
	private HashMap<InetAddress, MovingObjectsStreamClientHandler> activeClientHandler = new HashMap<InetAddress, MovingObjectsStreamClientHandler>();

	private MovingObjectsStreamServer vehicleServer;

	public MovingObjectsServer(int port) throws IOException {
		
		vehicleServer = new MovingObjectsStreamServer(port, this, MovingObjectsStreamType.VEHICLE);
	}

	/**
	 * Removes a ClientHandler from the list of ClientHandler. If a new query
	 * comes in from that IP it will be processed as new.
	 * 
	 * @param key
	 *            - IP of connection
	 * @param nexmarkStreamClientHandler
	 */
	public void removeClientHandler(InetAddress key, MovingObjectsStreamClientHandler clientHandler) {
		synchronized (activeClientHandler) {
			// Do nothing if no IP is registered for this handler
			if (!activeClientHandler.containsKey(key))
				return;

			// remove ClientHandler only if it is the rigt one. It could be that
			// a new, still active ClientHandler is already
			// registered for that IP.
			MovingObjectsStreamClientHandler currentClientHandler = activeClientHandler.get(key);
			if (currentClientHandler == clientHandler) {
				activeClientHandler.remove(key);
			}
		}

	}

	/**
	 * Starts the server. After that, only the specified ports can be used for
	 * connections.
	 */
	public void start() {
		System.out.println("Starting stream server...");
		vehicleServer.start();
	}

	/**
	 * Stops the server. No new connections are accepted. Connected clients are
	 * stopped.
	 */
	public synchronized void stop() {
		vehicleServer.close();

		Iterator<MovingObjectsStreamClientHandler> iter = activeClientHandler.values().iterator();
		while (iter.hasNext()) {
			iter.next().interrupt();
		}
	}

	public static void main(String[] args) {
		int startPort = 65056;
		MovingObjectsServer server = null;
		try {
			server = new MovingObjectsServer(startPort);
		} catch (IOException e) {			
			e.printStackTrace();
		}
		server.start();

	}

	public HashMap<InetAddress, MovingObjectsStreamClientHandler> getActiveClientHandler() {
		return activeClientHandler;
	}

}
