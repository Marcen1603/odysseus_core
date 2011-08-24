/** Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.service.sensor.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.service.sensor.ISensor;
import de.uniol.inf.is.odysseus.service.sensor.data.DataTuple;
import de.uniol.inf.is.odysseus.service.sensor.data.Schema;

/**
 * Implements an ISensor as a server. The server is listening on a given port
 * for new clients. If a new client connects to the server, the server opens a
 * new socket-connection for each client using a StreamClientHandler.
 */
public class SensorStreamServer extends Thread implements ISensor {

	/** The socket where the sensor is listening on. */
	private ServerSocket socket;

	/**
	 * This list holds all currently registered clients as a StreamClientHandler
	 * .
	 */
	private List<StreamClientHandler> streamClientHandlers = new ArrayList<StreamClientHandler>();

	/** The port this sensor is listening on. */
	private int port;

	/** The schema (the name and the according data type) for this sensor. */
	private Schema schema;

	/**
	 * Instantiates a new sensor stream server.
	 * 
	 * @param port
	 *            the port which the server listens on
	 * @param attributes
	 *            the attributes which the sensor provides
	 * @throws Exception
	 *             the exception is thrown is the socket can not be opened
	 */
	public SensorStreamServer(int port, Schema schema) throws Exception {
		this.port = port;
		this.schema = schema;

		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		socket = serverChannel.socket();
		socket.bind(new InetSocketAddress(port));
		serverChannel.configureBlocking(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		System.out.println("Starting new sensor on port " + this.socket.getLocalPort() + " and waiting for a connection...");
		while (true) {
			Socket connection = null;
			try {
				connection = socket.accept();
				System.out.println("New connection from " + connection.getInetAddress());
				System.out.println("Creating a new StreamHandler for the new client...");
				synchronized (this) {
					StreamClientHandler streamClient = new StreamClientHandler(connection, this.schema);
					System.out.println("StreamHandler for the new client created!");
					this.streamClientHandlers.add(streamClient);
				}
				sleep(0);
			} catch (InterruptedException e) {
				System.out.println("Server interrupted");
				break;
			} catch (ClosedByInterruptException e) {
				System.out.println("Server interrupted");
				break;
			} catch (IOException ex) {
				System.err.println("Error: ");
				ex.printStackTrace(System.err);
				continue;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.service.sensor.ISensor#sendTuple(de.uniol.inf
	 * .is.odysseus.service.sensor.data.DataTuple)
	 */
	public synchronized void sendTuple(DataTuple tuple) {
		List<StreamClientHandler> stillAlive = new ArrayList<StreamClientHandler>();
		for (StreamClientHandler sch : this.streamClientHandlers) {
			try {
				sch.transferTuple(tuple);
			} catch (IOException e) {
				System.out.println(e.getLocalizedMessage());
				sch.setDead(true);
			}
			// no exception, so this client was ok, so he is still alive
			if (!sch.isDead()) {
				stillAlive.add(sch);
			}
		}

		// now we only use clients that are alive
		this.streamClientHandlers = stillAlive;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.service.sensor.ISensor#getPort()
	 */
	public int getPort() {
		return this.port;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.service.sensor.ISensor#getOwnHost()
	 */
	public String getOwnHost() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			System.err.println("Eigener Host konnte nicht aufgelöst werden!");
			e.printStackTrace();
		}
		return "0.0.0.0";
	}
}
