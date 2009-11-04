package de.uniol.inf.is.odysseus.nexmark.simulation;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.nexmark.generator.NEXMarkGeneratorConfiguration;
import de.uniol.inf.is.odysseus.nexmark.generator.NEXMarkStreamType;

/**
 * Server fuer den NEXMark Benchmark.
 * <p>
 * Verwaltet eingehende Verbindungen zu den zu erzeugenden Streams: person,
 * auction, bid, category. Jeder Stream besitzt einen Port auf dem sich zu dem
 * Server verbunden werden kann, um die Benchmark Daten zu empfangen.
 * <p>
 * Die {@link #start()} Methode startet den Server. Die {@link #stop()} Methode
 * stoppt den Server und alle entfernt alle noch verbundenen Clients.
 * 
 * @author Bernd Hochschulz
 */
public class NexmarkServer {
	private HashMap<InetAddress, NexmarkStreamClientHandler> activeClientHandler = new HashMap<InetAddress, NexmarkStreamClientHandler>();
	
	private int elementLimit = 0;

	private NEXMarkGeneratorConfiguration configuration = new NEXMarkGeneratorConfiguration();
	private NEXMarkStreamServer personServer;
	private NEXMarkStreamServer auctionServer;
	private NEXMarkStreamServer bidServer;
	private NEXMarkStreamServer categoryServer;

	/**
	 * Erzeugt einen neuen {@link NexmarkServer}, um eingehende Benchmark
	 * Anfragen zu bearbeiten.
	 * 
	 * @param personPort
	 *            - port auf dem eingehende Verbindungen als personen Stream
	 *            interpretiert wird
	 * @param auctionPort
	 *            - port auf dem eingehende Verbindungen als auction Stream
	 *            interpretiert wird
	 * @param bidPort
	 *            - port auf dem eingehende Verbindungen als bid Stream
	 *            interpretiert wird
	 * @param categoryPort
	 *            - port auf dem eingehende Verbindungen als category Stream
	 *            interpretiert wird
	 * @throws IOException
	 *             wenn auf einem Port kein Socket erstellt werden kann
	 */
	public NexmarkServer(int personPort, int auctionPort, int bidPort, int categoryPort, boolean useNIO)
			throws IOException {
		personServer = new NEXMarkStreamServer(personPort, this, NEXMarkStreamType.PERSON, useNIO);
		auctionServer = new NEXMarkStreamServer(auctionPort, this, NEXMarkStreamType.AUCTION, useNIO);
		bidServer = new NEXMarkStreamServer(bidPort, this, NEXMarkStreamType.BID, useNIO);
		categoryServer = new NEXMarkStreamServer(categoryPort, this, NEXMarkStreamType.CATEGORY, useNIO);
	}

	/**
	 * Erzeugt einen neuen {@link NexmarkServer}, um eingehende Benchmark
	 * Anfragen zu bearbeiten.
	 * 
	 * @param personPort
	 *            - port auf dem eingehende Verbindungen als personen Stream
	 *            interpretiert wird
	 * @param auctionPort
	 *            - port auf dem eingehende Verbindungen als auction Stream
	 *            interpretiert wird
	 * @param bidPort
	 *            - port auf dem eingehende Verbindungen als bid Stream
	 *            interpretiert wird
	 * @param categoryPort
	 *            - port auf dem eingehende Verbindungen als category Stream
	 *            interpretiert wird
	 * @param elementLimit
	 *            - gibt an wieviele Elemente pro Verbindung erstellt werden
	 *            sollen.
	 * @throws IOException
	 *             wenn auf einem Port kein Socket erstellt werden kann
	 */
	public NexmarkServer(int personPort, int auctionPort, int bidPort, int categoryPort,
			int elementLimit, boolean useNIO) throws IOException {
		personServer = new NEXMarkStreamServer(personPort, this, NEXMarkStreamType.PERSON, useNIO);
		auctionServer = new NEXMarkStreamServer(auctionPort, this, NEXMarkStreamType.AUCTION, useNIO);
		bidServer = new NEXMarkStreamServer(bidPort, this, NEXMarkStreamType.BID, useNIO);
		categoryServer = new NEXMarkStreamServer(categoryPort, this, NEXMarkStreamType.CATEGORY, useNIO);
		
		this.elementLimit = elementLimit;
	}

	/**
	 * Erzeugt einen neuen {@link NexmarkServer}, um eingehende Benchmark
	 * Anfragen zu bearbeiten.
	 * 
	 * @param startPort
	 *            - gibt den startPort an. personPort = startPort, auctionPort =
	 *            startPort+1, bidPort = startPort+2, categoryPort = startPort+3
	 * @throws IOException
	 *             wenn auf einem Port kein Socket erstellt werden kann
	 */
	public NexmarkServer(int startPort, boolean useNIO) throws IOException {
		personServer = new NEXMarkStreamServer(startPort, this, NEXMarkStreamType.PERSON, useNIO);
		auctionServer = new NEXMarkStreamServer(++startPort, this, NEXMarkStreamType.AUCTION, useNIO);
		bidServer = new NEXMarkStreamServer(++startPort, this, NEXMarkStreamType.BID, useNIO);
		categoryServer = new NEXMarkStreamServer(++startPort, this, NEXMarkStreamType.CATEGORY, useNIO);
	}
	
	/**
	 * Erzeugt einen neuen {@link NexmarkServer}, um eingehende Benchmark
	 * Anfragen zu bearbeiten.
	 * 
	 * @param startPort
	 *            - gibt den startPort an. personPort = startPort, auctionPort =
	 *            startPort+1, bidPort = startPort+2, categoryPort = startPort+3
	 * @param elementLimit
	 *            - gibt an wieviele Elemente pro Verbindung erstellt werden
	 *            sollen
	 * @throws IOException
	 *             wenn auf einem Port kein Socket erstellt werden kann
	 */
	public NexmarkServer(int startPort, int elementLimit, boolean useNIO) throws IOException {
		personServer = new NEXMarkStreamServer(startPort, this, NEXMarkStreamType.PERSON, useNIO);
		auctionServer = new NEXMarkStreamServer(++startPort, this, NEXMarkStreamType.AUCTION, useNIO);
		bidServer = new NEXMarkStreamServer(++startPort, this, NEXMarkStreamType.BID, useNIO);
		categoryServer = new NEXMarkStreamServer(++startPort, this, NEXMarkStreamType.CATEGORY, useNIO);
		
		this.elementLimit = elementLimit;
	}	

	/**
	 * Entfernt einen ClientHandler aus der Liste der ClientHandler. Kommt
	 * erneut eine Anfrage von dieser IP wird sie nun neu behandlet.
	 * 
	 * @param key
	 *            - IP der Verbindung
	 * @param nexmarkStreamClientHandler
	 */
	public void removeClientHandler(InetAddress key, NexmarkStreamClientHandler clientHandler) {
		synchronized (activeClientHandler) {
			// Tue nichts falls zu dieser Ip kein Handler registriert ist
			if (!activeClientHandler.containsKey(key))
				return;

			// entferne den ClientHandler nur wenn es auch der richtige ist. Es
			// kann vorkommen, dass bereits ein neuer ClientHandler zu dieser Ip
			// registriert ist, der noch aktiv ist.
			NexmarkStreamClientHandler currentClientHandler = activeClientHandler.get(key);
			if (currentClientHandler == clientHandler) {
				activeClientHandler.remove(key);
			}
		}

	}

	// @Override
	// public void run() {
	// personServer.start();
	// auctionServer.start();
	// bidServer.start();
	// categoryServer.start();
	// }
	//
	/**
	 * Startet den Server. Ab jetzt kann auf die festgelegten Ports verbunden
	 * werden, um die Benchmarkdaten zu empfangen.
	 */
	public void start() {
		personServer.start();
		auctionServer.start();
		bidServer.start();
		categoryServer.start();
	}

	/**
	 * Stoppt den Server. Es werden keine neuen Verbindungen angenommen. Noch
	 * verbundene Clients werden abgebrochen.
	 */
	public synchronized void stop() {
		personServer.close();
		auctionServer.close();
		bidServer.close();
		categoryServer.close();

		Iterator<NexmarkStreamClientHandler> iter = activeClientHandler.values().iterator();
		while (iter.hasNext()) {
			iter.next().interrupt();
		}
	}

	public static void main(String[] args) {
		int personPort = 0, auctionPort = 0, bidPort = 0, categoryPort = 0;
		int startPort = 0;
		int elementLimit = 0;
		boolean useNIO = false;
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			if (arg.equals("-ports") || arg.equals("-p")) {
				personPort = Integer.parseInt(args[++i]);
				auctionPort = Integer.parseInt(args[++i]);
				bidPort = Integer.parseInt(args[++i]);
				categoryPort = Integer.parseInt(args[++i]);
			} else if (arg.equals("-port_range") || arg.equals("-pr")) {
				startPort = Integer.parseInt(args[++i]);
			} else if (arg.equals("-element_limit") || arg.equals("-el")) {
				elementLimit = Integer.parseInt(args[++i]);
			} else if (arg.equals("-useNIO")){
				useNIO = true;
			}
		}
		NexmarkServer server = null;
		try {
			if (startPort > 0) {
				if (elementLimit > 0) {
					server = new NexmarkServer(startPort, elementLimit, useNIO);
				} else {
					server = new NexmarkServer(startPort, useNIO);
				}
			} else if (personPort > 0 && auctionPort > 0 && bidPort > 0 && categoryPort > 0) {
				if (elementLimit > 0) {
					server = new NexmarkServer(personPort, auctionPort, bidPort, categoryPort,
							elementLimit, useNIO);
				} else {
					server = new NexmarkServer(personPort, auctionPort, bidPort, categoryPort, useNIO);
				}
			} else {
				throw new IOException();
			}
			server.start();
		} catch (IOException e) {
			printHelp(e);
		}
	}

	private static void printHelp(Exception e) {
		if (e != null){
			System.err.println(e.getMessage()+"\n\n");
		}
		String error = "NEXMark Server Usage: "
				+ "\n  -ports|-p <personPort>, <auctionPort>, <bidPort>, <categoryPort> (to specify specific ports)"
				+ "\n  -port_range|-pr <startPort> (to specify a port range, personPort is <startPort>, auctionPort is <startPort> + 1, ..."
				+ "\n  -element_limit|-el <elementLimit> (to specify an optional element Limit. If started with this option only <elementLimit> elements are created."
				+ "\n  -useNIO use java NIO with channel (otherwise ObjectStream).";
		
		System.err.println(error);
	}

	public HashMap<InetAddress, NexmarkStreamClientHandler> getActiveClientHandler() {
		return activeClientHandler;
	}

	public NEXMarkGeneratorConfiguration getConfiguration() {
		return configuration;
	}

	public int getElementLimit() {
		return elementLimit;
	}
}
