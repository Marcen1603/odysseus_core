package de.uniol.inf.is.odysseus.broker.movingobjects.simulation;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;

import java.util.logging.Logger;

import de.uniol.inf.is.odysseus.broker.movingobjects.generator.MovingObjectsStreamType;

public class MovingObjectsStreamServer extends Thread {
	private static final Logger logger = Logger.getLogger(MovingObjectsStreamServer.class.getCanonicalName());
	
	private ServerSocket socket;
	private int port;
	private MovingObjectsServer movoServer;
	private MovingObjectsStreamType type;

	public MovingObjectsStreamServer(int port, MovingObjectsServer movoServer, 
			MovingObjectsStreamType type)
			throws IOException {		
			ServerSocketChannel serverChannel = ServerSocketChannel.open();
			
			socket = serverChannel.socket();		
			socket.bind(new InetSocketAddress(port));		
			serverChannel.configureBlocking(true);
			
		
		this.movoServer = movoServer;
		this.type = type;
		this.port = port;
	}
	
	@Override
	public void run() {
		while (true) {
			// Wait for Client connection
			System.out.println("waiting for connection on port: " + this.port);
			Socket connection = null;
			try {
				connection = socket.accept();
			} catch (IOException e) {
				break;
			}	
			logger.info("Connection from: " + connection.getInetAddress());

			// Handle client connection
			MovingObjectsClient client = null;
			try {
				client = new MovingObjectsClient(connection, type);
			} catch (IOException e) {
				logger.info("Client with ip: " + connection.getInetAddress()
						+ " could not be handled.");
				continue;
			}			
			handleIncomingStreamRequest(connection, client);

			
		}		
		logger.info("Simulation Server of type '" + type.name + "' stopped");
	}

	private void handleIncomingStreamRequest(Socket connection, MovingObjectsClient client) {
		InetAddress ip = connection.getInetAddress();

		// existiert bereits ein ClientHandler fuer die Ip, fuege
		// Verbindung zu diesem hinzu, sonst erstelle einen neuen.
		HashMap<InetAddress, MovingObjectsStreamClientHandler> activeClientHandler = movoServer
				.getActiveClientHandler();

		synchronized (activeClientHandler) {
			MovingObjectsStreamClientHandler clientHandler = null;
			if (activeClientHandler.containsKey(ip)) {
				clientHandler = activeClientHandler.get(ip);

				// Versuche client zu ClientHandler hinzuzufuegen. Es gelingt
				// nicht, wenn der ClientHandler keine Anfragen mehr entgegen
				// nimmt aber der Server davon noch nicht benachrichtigt wurde
				if (!clientHandler.addClient(client)) {
					clientHandler = new MovingObjectsStreamClientHandler(connection, movoServer);
					clientHandler.addClient(client);
					activeClientHandler.put(ip, clientHandler);
					clientHandler.start();
				}

				logger.info("Added stream '" + client.streamType.name + "' to Client");
			} else {
				clientHandler = new MovingObjectsStreamClientHandler(connection, movoServer);
				clientHandler.addClient(client);
				activeClientHandler.put(ip, clientHandler);
				clientHandler.start();
				logger.info("New Client with stream '" + client.streamType.name + "'");
			}
		}

	}

	@Override
	protected void finalize() throws Throwable {
		if (socket != null && !socket.isClosed()) {
			socket.close();
		}
	}

	/**
	 * Stoppt den Server. Es werden keine neuen Verbindungen angenommen. Noch
	 * verbundene Clients werden abgebrochen.
	 */
	public synchronized void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
