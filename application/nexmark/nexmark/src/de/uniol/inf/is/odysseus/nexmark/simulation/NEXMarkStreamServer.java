/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.nexmark.simulation;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.nexmark.generator.NEXMarkStreamType;

public class NEXMarkStreamServer extends Thread {

	private static final Logger logger = LoggerFactory.getLogger( NEXMarkStreamServer.class );
	
	private ServerSocket socket;
	private int port;
	private NexmarkServer nexmarkServer;
	final private NEXMarkStreamType type;
	final private boolean useNIO;
	final private boolean useGlobalGenerator;
	
	public NEXMarkStreamServer(int port, NexmarkServer nexmarkServer, 
							   NEXMarkStreamType type, boolean useNIO, boolean useGlobalGenerator)
			throws IOException {
		this.useNIO = useNIO;
		this.useGlobalGenerator = useGlobalGenerator;
		if (useNIO){
			ServerSocketChannel serverChannel = ServerSocketChannel.open();
			socket = serverChannel.socket();
			socket.bind(new InetSocketAddress(port));
			serverChannel.configureBlocking(true);
		}else{
			this.socket = new ServerSocket(port);
		}
		this.nexmarkServer = nexmarkServer;
		this.type = type;
		this.port = port;
	}

	/**
	 * Wartet auf eingehende Verbindungen. Bei einer Verbindung wird ein
	 * {@link NexmarkStreamClientHandler} erzeugt, der eine Verbindung mit
	 * streams bearbeitet. Ein {@link NexmarkStaticClientHandler} wird erzeugt
	 * wenn eine statische Anfrage gestellt wird.
	 */
	@Override
	public void run() {
		// System.out.println("NEXMark Simulation Server for " + sourceURI +
		// " started");
	
		while (true) {
			// Wait for Client connection
			logger.info("waiting for connection "+type+" on port: " + this.port+ " using NIO="+useNIO+" with globaleGen="+useGlobalGenerator);
			Socket connection = null;
			try {
				connection = socket.accept();
			} catch (IOException e) {
				break;
			}

			// if (interrupted()) {
			// break;
			// }

			logger.info("Connection from: " + connection.getInetAddress());

			// Handle client connection
			NEXMarkClient client = null;
			try {
				client = new NEXMarkClient(connection, type, useNIO);
			} catch (IOException e) {
				logger.error("Client with ip: " + connection.getInetAddress()
						+ " could not be handled.");
				continue;
			}
			// } catch (WrongStreamNameException e) {
			// System.err.println("Client with ip: " +
			// connection.getInetAddress()
			// + " could not be handled. " + e.getMessage());
			// continue;
			// }
			// System.out.println("New " + client);

			// behandle streams mit category als Anfrage anders, da category
			// ein statischer "stream" ist
			if (client.streamType == NEXMarkStreamType.CATEGORY) {
				NexmarkStaticClientHandler clientHandler = new NexmarkStaticClientHandler(
						connection, client);
				clientHandler.start();
			} else {
				handleIncomingStreamRequest(connection, client);

			}
		}

		// Alle verbundenen Clients trennen
		// Iterator<NexmarkStreamClientHandler> iter =
		// aktiveClientHandler.values().iterator();
		// while (iter.hasNext()) {
		// iter.next().interrupt();
		// }

		logger.info("NEXMark Simulation Server of type '" + type.name + "' stopped");
	}

	private void handleIncomingStreamRequest(Socket connection, NEXMarkClient client) {
		InetAddress ip = connection.getInetAddress();

		// existiert bereits ein ClientHandler fuer die Ip, fuege
		// Verbindung zu diesem hinzu, sonst erstelle einen neuen.
		HashMap<InetAddress, NexmarkStreamClientHandler> activeClientHandler = nexmarkServer
				.getActiveClientHandler();

		synchronized (activeClientHandler) {
			NexmarkStreamClientHandler clientHandler = null;
			if (activeClientHandler.containsKey(ip)) {
				clientHandler = activeClientHandler.get(ip);

				// Versuche client zu ClientHandler hinzuzufuegen. Es gelingt
				// nicht, wenn der ClientHandler keine Anfragen mehr entgegen
				// nimmt aber der Server davon noch nicht benachrichtigt wurde
				if (!clientHandler.addClient(client)) {
					clientHandler = new NexmarkStreamClientHandler(connection, nexmarkServer, useGlobalGenerator);
					clientHandler.addClient(client);
					activeClientHandler.put(ip, clientHandler);
					clientHandler.start();
				}

				logger.info("Added stream '" + client.streamType.name + "' to Client");
			} else {
				clientHandler = new NexmarkStreamClientHandler(connection, nexmarkServer, useGlobalGenerator);
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
