package de.uniol.inf.is.odysseus.nexmark.simulation;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.nexmark.generator.NEXMarkGenerator;
import de.uniol.inf.is.odysseus.nexmark.generator.NEXMarkGeneratorConfiguration;
import de.uniol.inf.is.odysseus.nexmark.generator.NEXMarkStreamType;
import de.uniol.inf.is.odysseus.nexmark.generator.SimpleCalendar;
import de.uniol.inf.is.odysseus.nexmark.generator.TupleContainer;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * Der NexmarkStreamClientHandler bearbeitet eingehende Streamverbindungen
 * (nexmark:person, nexmark:auction, nexmark:bid) zum {@link NexmarkServer}.
 * 
 * @see NexmarkServer
 * @author Bernd Hochschulz
 */
public class NexmarkStreamClientHandler extends Thread {
	private static final boolean CACHE_FILES = false;

	private static final String CACHED_FILE_PATH = "cached_files";
	private static Boolean threadWritesCachedFiles = false;

	private enum SimulationType {
		newSimulation,
		cachedSimulation,
		newSimulationAndCache
	};

	// Liste der Verbunden Clients
	private ArrayList<NEXMarkClient> clients;

	// File das zum cachen des Stream verwendet wird
	private File cachedFile;

	private NEXMarkGeneratorConfiguration configuration;

	private NEXMarkGenerator generator;
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
	public NexmarkStreamClientHandler(Socket connection, NexmarkServer server) {
		this.clients = new ArrayList<NEXMarkClient>();
		this.connection = connection;
		this.configuration = server.getConfiguration();
		this.server = server;
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
		// ermitteln wie simuliert werden soll.
		// - neue Simulation,
		// - vorgecachte Simulation,
		// - oder neue Simulation und gleichzeitiges cachen
		SimulationType simulationType = getSimulationType();

		// je nach Simulationstyp die Simulation starten.
		try {
			switch (simulationType) {
			case newSimulation:
				System.out.println("starting new Simulation - another Thread caches Files");
				this.startNewSimulation();
				break;

			case cachedSimulation:
				System.out.println("reading Simulation from Files");
				this.startSimulationFromFiles();
				break;

			case newSimulationAndCache:
				System.out.println("starting new Simulation and caching files");
				this.startNewSimulationAndCacheFiles();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			server.removeClientHandler(connection.getInetAddress(), this);
		}
	}

	private SimulationType getSimulationType() {
		if (CACHE_FILES) {
			synchronized (threadWritesCachedFiles) {
				if (threadWritesCachedFiles) {
					// wenn derzeit ein Thread die files cached, erzeuge eine
					// neue Simulation
					return SimulationType.newSimulation;
				} else {
					try {
						if (openCachedFile()) {
							// wenn cached Files existieren und sie geoeffnet
							// werden koennen, lese aus ihnen
							return SimulationType.cachedSimulation;
						} else {
							// wenn cached Files zwar geoeffnet werden koennen
							// aber neu erzeugt wurden, erzeuge eine neue
							// Simulation
							// und schreibe Tupel in die Dateien
							threadWritesCachedFiles = true;
							return SimulationType.newSimulationAndCache;
						}
					} catch (IOException e) {
						// wenn es einen Fehler beim oeffnen der Dateien gab
						// erzeuge
						// eine neue Simulation on caching
						return SimulationType.newSimulation;
					}
				}
			}
		} else {
			return SimulationType.newSimulation;
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
			generator = new NEXMarkGenerator(configuration, true);
			simulationStream = generator.getInputStream();
			generator.start();

			// sende solange Tupel vom Generator bis keine Clients mehr
			// verbunden sind
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
				sendTupleToClients(null, null);
				generator.interrupt();

				if (simulationStream != null)
					simulationStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Tupel werden aus einer Datei gelesen und zu den Clients gesendet.
	 * 
	 * @throws FileNotOpenableException
	 *             wenn aus der Datei nicht gelesen werden kann.
	 */
	private void startSimulationFromFiles() throws FileNotOpenableException {
		// Erzeuge Filestreams
		ObjectInputStream fileIn = null;

		try {
			FileInputStream personFileInputStream = new FileInputStream(cachedFile);
			fileIn = new ObjectInputStream(personFileInputStream);

		} catch (IOException e) {
			throw new FileNotOpenableException();
		}

		// lese Tupel aus Datei und sende sie den Zeitstempeln der Tupel
		// entsprechend.
		TupleContainer container;
		SimpleCalendar cal = new SimpleCalendar();

		try {
			while ((container = readNextTupleContainerFromStream(fileIn)) != null
					&& !isInterrupted()) {
				long timeToWait = getTimeToWait(container.tuple, cal);
				if (timeToWait > 0) {
					Thread.sleep(timeToWait);
				}

				sendTupleToClients(container.tuple, container.type);
				if (noMoreClients) {
					System.out.println("Client " + connection.getInetAddress() + " disconnected.");
					break;
				}
			}

		} catch (IOException e) {
			// Dateiende erreicht

		} catch (InterruptedException e) {
			// Thread wurde waehrend sleep oder Stream.read interrupted, ist ok.
			// Server wurde wahrscheinlich beendet.

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// sende "null" um Ende zu signalisieren
			sendTupleToClients(null, null);

			try {
				if (fileIn != null)
					fileIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Erstellt einen neuen Stream der Simulation und sendet die generierten
	 * Tupel an die verbundene Clients und schreibt sie in eine Datei.
	 * 
	 * @throws FileNotOpenableException
	 */
	private void startNewSimulationAndCacheFiles() throws FileNotOpenableException {

		// Filestreams erzeugen
		ObjectOutputStream fileOut = null;
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(cachedFile);
			fileOut = new ObjectOutputStream(fileOutputStream);

		} catch (IOException e) {
			throw new FileNotOpenableException();
		}

		ObjectInputStream simulationStream = null;
		try {
			// Stream der Simulation erstellen
			generator = new NEXMarkGenerator(configuration, true);
			simulationStream = generator.getInputStream();
			generator.start();

			// sende Tupel bis keine Clients mehr verbunden sind
			TupleContainer container;
			while ((container = readNextTupleContainerFromStream(simulationStream)) != null
					&& !isInterrupted()) {
				sendTupleToClients(container.tuple, container.type);
				if (noMoreClients) {
					System.out.println("Client " + connection.getInetAddress() + " disconnected.");
					break;
				}
				writeTupleToFile(fileOut, container);
			}

		} catch (EOFException e) {
			// e.printStackTrace();
			System.err.println("EOF");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// Thread wurde waehrend sleep oder Stream.read interrupted, ist ok.
			// Server wurde wahrscheinlich beendet.

		} finally {
			// der Thread war derjenige, der die Files vorgecached hat, damit
			// ist er jetzt fertig.
			synchronized (threadWritesCachedFiles) {
				threadWritesCachedFiles = false;
			}

			sendTupleToClients(null, null);
			generator.interrupt();

			try {
				if (simulationStream != null)
					simulationStream.close();

				if (fileOut != null)
					fileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Gibt die zu wartende Zeit zurueck bis das Tupel gesendet werden soll.
	 * 
	 * @param tuple
	 *            - das Tupel dessen Zeitstempel beachtet werden soll
	 * @param cal
	 *            - der {@link SimpleCalendar} mit dessen Hilfe die Zeit
	 *            bestimmt wird.
	 * @return die zu wartende Zeit
	 */
	private long getTimeToWait(RelationalTuple<ITimeInterval> tuple, SimpleCalendar cal) {
		long timeToWait = ((Long) tuple.getAttribute(0) / configuration.accelerationFactor)
				- cal.getTimeInMS();

		return timeToWait;
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
	private void sendTupleToClients(RelationalTuple<ITimeInterval> tuple, NEXMarkStreamType type) {
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
					System.out.println("A '" + client + "' of " + connection.getInetAddress()
							+ " disconnected.");
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

	/**
	 * Schreibt ein {@link TupleContainer} in den OutputStream
	 * 
	 * @param fileOut
	 *            - ObjectStream in den geschrieben wird
	 * @param container
	 *            - zu schreibender {@link TupleContainer}
	 * @throws IOException
	 *             wenn nicht in den Stream geschrieben werden kann
	 */
	private void writeTupleToFile(ObjectOutputStream fileOut, TupleContainer container)
			throws IOException {
		fileOut.writeObject(container);

	}

	/**
	 * Versucht eine bereits mit Tupel vorgecachete Datei zu oeffnen. Gelingt
	 * dies nicht wird eine neue erstellt.
	 * 
	 * @param streamName
	 * @return true wenn Datei geoeffnet werden konnte, false wenn neue Datei
	 *         erstellt wurde
	 * @throws FileNotOpenableException
	 * @throws IOException
	 */
	private boolean openCachedFile() throws IOException {
		File dir = new File(NexmarkStreamClientHandler.CACHED_FILE_PATH);
		String fileName = createFileName();
		cachedFile = new File(dir, fileName);

		if (!cachedFile.exists()) {
			if (!dir.exists()) {
				dir.mkdir();
			}

			cachedFile.createNewFile();

			return false;
		}

		return true;
	}

	/**
	 * Funktion zustaendig fuer den Dateinamen der Datei in den die erzeugten
	 * Tupel geschrieben bzw. davon gelesen werden.
	 * 
	 * @return zusammengesetzter Dateiname
	 */
	private String createFileName() {
		int minP = configuration.minDistBetweenPersons;
		int maxP = configuration.maxDistBetweenPersons;

		int minA = configuration.minDistBetweenAuctions;
		int maxA = configuration.maxDistBetweenAuctions;

		int minB = configuration.minDistBetweenBids;
		int maxB = configuration.maxDistBetweenBids;

		String configName = "persons [" + minP + " - " + maxP + "], " + "auctions [" + minA + " - "
				+ maxA + "], " + "bids [" + minB + " - " + maxB + "]";

		int burstMin = configuration.burstConfig.minTimeBetweenBursts;
		int burstMax = configuration.burstConfig.maxTimeBetweenBursts;

		int burstDurMin = configuration.burstConfig.minBurstDuration;
		int burstDurMax = configuration.burstConfig.maxBurstDuration;

		int burstFactor = configuration.burstConfig.burstAccelerationFactor;

		String burstName = "burst [" + burstMin + " - " + burstMax + "], [" + burstDurMin + " - "
				+ burstDurMax + "], [" + burstFactor + "]";

		return configName + " - " + burstName;
	}
}

class FileNotOpenableException extends Exception {
	private static final long serialVersionUID = -5936600342862181677L;
}
