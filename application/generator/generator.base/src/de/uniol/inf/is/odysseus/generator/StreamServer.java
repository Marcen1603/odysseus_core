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
package de.uniol.inf.is.odysseus.generator;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.List;

public class StreamServer extends Thread implements UncaughtExceptionHandler{

	final private StreamClientHandler defaultHandler;
	final private ServerSocket socket;
	final private IDataGenerator dataGenerator;
	final private boolean newGeneratorEachConnection;
	final private List<StreamClientHandler> clients = new ArrayList<StreamClientHandler>();
	private int throughputEach = 0;
	private int instanceCounter = 1;
	private String name = "";

	public StreamServer(int port, IDataGenerator dataGenerator,
			boolean newGeneratorEachConnection) throws Exception {
		this(port, dataGenerator, newGeneratorEachConnection, dataGenerator
				.getClass().getSimpleName());
	}

	public StreamServer(int port, IDataGenerator dataGenerator,
			boolean newGeneratorEachConnection, String name) throws Exception {
		this(port, dataGenerator, newGeneratorEachConnection, 0, name);
	}

	public StreamServer(int port, IDataGenerator dataGenerator,
			boolean newGeneratorEachConnection, int printThroughputEach)
			throws Exception {
		this(port, dataGenerator, newGeneratorEachConnection,
				printThroughputEach, dataGenerator.getClass().getSimpleName());
	}

	public StreamServer(int port, IDataGenerator dataGenerator)
			throws Exception {
		this(port, dataGenerator, true, dataGenerator.getClass()
				.getSimpleName());
	}

	public StreamServer(int port, IDataGenerator dataGenerator, String name)
			throws Exception {
		this(port, dataGenerator, true, 0, name);
	}

	public StreamServer(int port, IDataGenerator dataGenerator,
			int printThroughputEach) throws Exception {
		this(port, dataGenerator, true, printThroughputEach, dataGenerator
				.getClass().getSimpleName());
	}

	public StreamServer(int port, IDataGenerator dataGenerator,
			int printThroughputEach, String name) throws Exception {
		this(port, dataGenerator, true, printThroughputEach, name);
	}

	public StreamServer(int port, IDataGenerator dataGenerator,
			boolean newGeneratorEachConnection, int printThroughputEach,
			String name) throws Exception {
		this.dataGenerator = dataGenerator;
		this.newGeneratorEachConnection = newGeneratorEachConnection;
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		socket = serverChannel.socket();
		socket.bind(new InetSocketAddress(port));
		serverChannel.configureBlocking(true);
		this.throughputEach = printThroughputEach;
		this.name = name;
		if (!newGeneratorEachConnection) {
			this.defaultHandler = new StreamClientHandler();
			defaultHandler.setGenerator(dataGenerator.newCleanInstance());
			defaultHandler.setThroughputEach(this.throughputEach);
			defaultHandler.start();
			defaultHandler.setInstanceNumber(instanceCounter);
			defaultHandler.setStreamName(name);
			addClient(defaultHandler);
			instanceCounter++;
		}else{
			defaultHandler = null;
		}
	}

	@Override
	public void run() {
		System.out.println("Starting new server on port "
				+ this.socket.getLocalPort() + " for " + this.name);
		while (true) {
			Socket connection = null;
			try {
				System.out.println("Waiting for connection...");
				connection = socket.accept();
				System.out.println("New connection from "
						+ connection.getInetAddress() + " on port on port "
						+ this.socket.getLocalPort() + " for " + this.name);

				if (newGeneratorEachConnection) {
					StreamClientHandler streamClient = new StreamClientHandler();
					streamClient.setGenerator(dataGenerator.newCleanInstance());
					streamClient.setConnection(connection);
					streamClient.setThroughputEach(this.throughputEach);
					streamClient.start();
					streamClient.setInstanceNumber(instanceCounter);
					streamClient.setStreamName(name);
					addClient(streamClient);
					instanceCounter++;
					System.out.println("Started new simulation");
				} else {
					defaultHandler.addConnection(connection);
					System.out.println("Added Client to running simulation.");
				}

			} catch (Exception ex) {
				System.err.println("Error: " + ex.getStackTrace());
				continue;
			}
		}
	}

	private void addClient(StreamClientHandler streamClient) {
		this.clients.add(streamClient);
		streamClient.setUncaughtExceptionHandler(this);
	}

	public void stopClients() {
		synchronized (this) {
			for (StreamClientHandler sch : this.clients) {
				sch.stopClient();
			}
		}
	}

	public void proceedClients() {
		synchronized (this) {
			for (StreamClientHandler sch : this.clients) {
				sch.proceed();
			}
		}
	}

	public void pauseClients() {
		synchronized (this) {
			for (StreamClientHandler sch : this.clients) {
				sch.pause();
			}
		}
	}

	public void printStats() {
		synchronized (this) {
			for (StreamClientHandler sch : this.clients) {
				sch.printStatus();
			}
		}

	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.err.println("Exception from thread "+t);
		e.printStackTrace();
	}
}
