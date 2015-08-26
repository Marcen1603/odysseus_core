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
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.nexmark.generator.NEXMarkGenerator;
import de.uniol.inf.is.odysseus.nexmark.generator.NEXMarkGeneratorConfiguration;
import de.uniol.inf.is.odysseus.nexmark.generator.NEXMarkStreamType;
import de.uniol.inf.is.odysseus.nexmark.generator.TupleContainer;

/**
 * Der NexmarkStreamClientHandler bearbeitet eingehende Streamverbindungen
 * (nexmark:person, nexmark:auction, nexmark:bid) zum {@link NexmarkServer}.
 * 
 * @see NexmarkServer
 * @author Bernd Hochschulz, Marco Grawunder
 */
public class NexmarkStreamClientHandler extends Thread implements ITupleContainerListener {

	Logger logger = LoggerFactory.getLogger(NexmarkStreamClientHandler.class);

	// Liste der Verbunden Clients
	private ArrayList<NEXMarkClient> clients;

	private NEXMarkGeneratorConfiguration configuration;

	private NEXMarkGenerator generator;
	static private NEXMarkGenerator globalGenerator;
	final private boolean useGlobalGenerator;
	private Socket connection;
	private NexmarkServer server;

	private int sentElements = 0;

	private boolean noMoreClients;
	private boolean acceptsNewConnections = true;

	/**
	 * Erzeugt einen Client Handler um eine Verbindung zum Nexmark Benchmark
	 * Server zu bearbeiten
	 * 
	 * @param connection
	 *            - Socket der Verbindung
	 */
	public NexmarkStreamClientHandler(Socket connection, NexmarkServer server, boolean useGlobalGenerator) {
		this.clients = new ArrayList<NEXMarkClient>();
		this.connection = connection;
		this.configuration = server.getConfiguration();
		this.server = server;
		this.useGlobalGenerator = useGlobalGenerator;
	}

	/**
	 * Fuegt eine neue Verbindung zu diesem ClientHandler hinzu.
	 * 
	 * @param connection
	 *            - die neue Verbindung
	 */
	public boolean addClient(NEXMarkClient client) {
		synchronized (clients) {
			if (!acceptsNewConnections) {
				return false;
			}
			this.clients.add(client);
			return true;
		}
	}

	/**
	 * Bearbeitet eine Anfrage an den Nexmark Benchmark Server. In den
	 * OutputStream des Socktes des jeweiligen Clients werden die
	 * Simulationsdaten geschrieben.
	 */
	@Override
	public void run() {
		try {
			if (useGlobalGenerator) {
				if (globalGenerator == null) {
					globalGenerator = new NEXMarkGenerator(configuration, true, this);
					globalGenerator.start();
				}else{
					globalGenerator.addListner(this);
				}
				generator = globalGenerator;
			} else {
				generator = new NEXMarkGenerator(configuration, true, this);
				generator.start();
			}
			synchronized (this) {
				while (useGlobalGenerator || !noMoreClients) {
					this.wait(1000);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			server.removeClientHandler(connection.getInetAddress(), this);
		}

	}

	@Override
	public void newObject(TupleContainer container) throws IOException {
		sendTupleToClients(container.tuple, container.type);
		if (noMoreClients && !useGlobalGenerator) {
			sendTupleToClients(null, null);
			generator.interrupt();
			throw new IOException("No more clients");
		}
	}

	/**
	 * Sendet ein Tupel an alle verbundenen Clients die den Stream 'type'
	 * angefordert haben. Ist 'type' null wird das Tupel an alle gesendet. Wenn
	 * keine Clients mehr zuhoeren wird 'noMoreClients' auf true gesetzt.
	 * 
	 * @param tuple
	 *            - das zu sendene Tupel
	 * @param type
	 *            - Type des Tupels
	 */
	private void sendTupleToClients(Tuple<ITimeInterval> tuple, NEXMarkStreamType type) {
		synchronized (this.clients) {
			Iterator<NEXMarkClient> iter = clients.iterator();
			while (iter.hasNext()) {
				NEXMarkClient client = iter.next();
				try {
					if (type == null || client.streamType == type) {
						client.writeObject(tuple, true);
					}
				} catch (IOException e) {
					iter.remove();
					logger.info("A '" + client + "' of " + connection.getInetAddress() + " disconnected.");
				}
			}

			if (server.getElementLimit() != 0) {
				sentElements++;
				if (sentElements >= server.getElementLimit()) {
					noMoreClients = true;
					acceptsNewConnections = false;
				}
			}

			if (clients.isEmpty()) {
				noMoreClients = true;
				acceptsNewConnections = false;
			}
		}
	}
}
