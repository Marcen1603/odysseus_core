package de.uniol.inf.is.odysseus.broker.movingobjects.simulation;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.broker.movingobjects.generator.MovingObjectsGenerator;
import de.uniol.inf.is.odysseus.broker.movingobjects.generator.MovingObjectsStreamType;
import de.uniol.inf.is.odysseus.broker.movingobjects.generator.TupleContainer;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class MovingObjectsStreamClientHandler extends Thread {
	
	// Liste der Verbunden Clients
	private ArrayList<MovingObjectsClient> clients;

	
	private MovingObjectsGenerator generator;
	private Socket connection;
	private MovingObjectsServer server;
	
	private boolean noMoreClients;
	private boolean acceptsNewConnections = true;

	/**
	 * Erzeugt einen Client Handler um eine Verbindung zum MovingObjects Benchmark
	 * Server zu bearbeiten
	 * 
	 * @param connection
	 *            - Socket der Verbindung
	 */
	public MovingObjectsStreamClientHandler(Socket connection, MovingObjectsServer server) {
		this.clients = new ArrayList<MovingObjectsClient>();
		this.connection = connection;		
		this.server = server;
	}

	/**
	 * Fuegt eine neue Verbindung zu diesem ClientHandler hinzu.
	 * 
	 * @param connection
	 *            - die neue Verbindung
	 */
	public boolean addClient(MovingObjectsClient client) {
		synchronized (clients) {
			if (!acceptsNewConnections) {
				return false;
			}
			this.clients.add(client);
			return true;
		}
	}

	/**
	 * Bearbeitet eine Anfrage an den MovingObjects Benchmark Server. In den
	 * OutputStream des Socktes des jeweiligen Clients werden die
	 * Simulationsdaten geschrieben.
	 */
	@Override
	public void run() {		
		try {			
				System.out.println("starting new Simulation - another Thread caches Files");
				this.startNewSimulation();			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			server.removeClientHandler(connection.getInetAddress(), this);
		}
	}

	/**
	 * Erstellt einen neuen Stream der Simulation und sendet die generierten
	 * Tupel zu den verbundenen Clients.
	 */
	private void startNewSimulation() {

		ObjectInputStream simulationStream = null;
		try {
			// Generator erstellen
			generator = new MovingObjectsGenerator();	
			simulationStream = generator.getInputStream();
			generator.start();

			TupleContainer container;
			while ((container = readNextTupleContainerFromStream(simulationStream)) != null
					&& !isInterrupted()) {
				sendTupleToClients(container.tuple, container.type);
				if (noMoreClients) {
					System.out.println("Client " + connection.getInetAddress() + " disconnected.");
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// Thread wurde waehrend sleep oder Stream.read interrupted, ist ok.
			// Server wurde wahrscheinlich beendet.

		} finally {
			try {
				sendTupleToClients(null,null);
				generator.interrupt();

				if (simulationStream != null)
					simulationStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Liest ein {@link TupleContainer} aus dem angegebenen Stream.
	 * 
	 * @param inStream
	 *            - Stream aus dem gelesen wird
	 * @return den naechsten {@link TupleContainer} aus dem Stream oder null
	 * @throws IOException
	 *             wenn nicht aus dem Stream gelesen werden kann
	 * @throws ClassNotFoundException
	 *             wenn die gelesene Klasse nicht gefunden werden kann
	 */
	private TupleContainer readNextTupleContainerFromStream(ObjectInputStream inStream)
			throws IOException, ClassNotFoundException, InterruptedException {
		Object obj = null;
		try {
			obj = inStream.readObject();
		} catch (InterruptedIOException e) {
			throw new InterruptedException();
		}

		if (obj instanceof TupleContainer) {
			return (TupleContainer) obj;
		}
		return null;
	}

	private void sendTupleToClients(RelationalTuple<ITimeInterval> tuple, MovingObjectsStreamType type) {
		synchronized (this.clients) {
			Iterator<MovingObjectsClient> iter = clients.iterator();
			while (iter.hasNext()) {
				MovingObjectsClient client = iter.next();
				try {					
					if(client.streamType.equals(type) || type==null){
						client.writeObject(tuple, true);
					}
				}catch (IOException e) {
					iter.remove();
					System.out.println("A '" + client + "' of " + connection.getInetAddress()
							+ " disconnected.");
				}
			}			

			if (clients.isEmpty()) {
				noMoreClients = true;
				acceptsNewConnections = false;
			}
		}
	}

}