package de.uniol.inf.is.odysseus.nexmark.simulation;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.nexmark.generator.NEXMarkGeneratorConfiguration;
import de.uniol.inf.is.odysseus.nexmark.generator.NEXMarkStreamType;

/**
 * Server for NEXMark Benchmark.
 * <p>
 * Manages incoming connection for the streams: person,
 * auction, bid, category. Each stream has a port which can be
 * used to connect and get the data of the benchmark.
 * <p>
 * The {@link #start()} method starts server. 
 * The {@link #stop()} method stops the server and removes all connected clients.
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
	 * Creates a new {@link NexmarkServer} to process incoming benchmark queries.
	 * 
	 * @param personPort
	 *            - connections on this port are interpreted as person stream
	 * @param auctionPort
	 *            - connections on this port are interpreted as auction stream
	 * @param bidPort
	 *            - connections on this port are interpreted as bid stream
	 * @param categoryPort
	 *            - connections on this port are interpreted as category stream
	 * @throws IOException
	 *            - if no connection cannot be established on a port
	 */
	public NexmarkServer(int personPort, int auctionPort, int bidPort, int categoryPort, boolean useNIO)
			throws IOException {
		personServer = new NEXMarkStreamServer(personPort, this, NEXMarkStreamType.PERSON, useNIO);
		auctionServer = new NEXMarkStreamServer(auctionPort, this, NEXMarkStreamType.AUCTION, useNIO);
		bidServer = new NEXMarkStreamServer(bidPort, this, NEXMarkStreamType.BID, useNIO);
		categoryServer = new NEXMarkStreamServer(categoryPort, this, NEXMarkStreamType.CATEGORY, useNIO);
	}

	/**
	 * Creates a new {@link NexmarkServer} to process incoming benchmark queries.
	 * 
	 * @param personPort
	 *            - connections on this port are interpreted as person stream
	 * @param auctionPort
	 *            - connections on this port are interpreted as auction stream
	 * @param bidPort
	 *            - connections on this port are interpreted as bid stream
	 * @param categoryPort
	 *            - connections on this port are interpreted as category stream
	 * @param elementLimit
	 *            - how many elements per connections should be created
	 * @throws IOException
	 *            - if no connection cannot be established on a port
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
	 * Creates a new {@link NexmarkServer} to process incoming benchmark queries.
	 * 
	 * @param startPort
	 *            - specifies startPort. personPort = startPort, auctionPort =
	 *            startPort+1, bidPort = startPort+2, categoryPort = startPort+3
	 * @throws IOException
	 *            - if no connection cannot be established on a port
	 */
	public NexmarkServer(int startPort, boolean useNIO) throws IOException {
		personServer = new NEXMarkStreamServer(startPort, this, NEXMarkStreamType.PERSON, useNIO);
		auctionServer = new NEXMarkStreamServer(++startPort, this, NEXMarkStreamType.AUCTION, useNIO);
		bidServer = new NEXMarkStreamServer(++startPort, this, NEXMarkStreamType.BID, useNIO);
		categoryServer = new NEXMarkStreamServer(++startPort, this, NEXMarkStreamType.CATEGORY, useNIO);
	}
	
	/**
	 * Creates a new {@link NexmarkServer} to process incoming benchmark queries.
	 * 
	 * @param startPort
	 *            - specifies startPort. personPort = startPort, auctionPort =
	 *            startPort+1, bidPort = startPort+2, categoryPort = startPort+3
	 * @param elementLimit
	 *            - how many elements per connections should be created
	 * @throws IOException
	 *            - if no connection cannot be established on a port
	 */

	public NexmarkServer(int startPort, int elementLimit, boolean useNIO) throws IOException {
		personServer = new NEXMarkStreamServer(startPort, this, NEXMarkStreamType.PERSON, useNIO);
		auctionServer = new NEXMarkStreamServer(++startPort, this, NEXMarkStreamType.AUCTION, useNIO);
		bidServer = new NEXMarkStreamServer(++startPort, this, NEXMarkStreamType.BID, useNIO);
		categoryServer = new NEXMarkStreamServer(++startPort, this, NEXMarkStreamType.CATEGORY, useNIO);
		
		this.elementLimit = elementLimit;
	}	

	/**
	 * Removes a ClientHandler from the list of ClientHandler. 
	 * If a new query comes in from that IP it will be processed as new.
	 * 
	 * @param key
	 *            - IP of connection
	 * @param nexmarkStreamClientHandler
	 */
	public void removeClientHandler(InetAddress key, NexmarkStreamClientHandler clientHandler) {
		synchronized (activeClientHandler) {
			// Do nothing if no IP is registered for this handler
			if (!activeClientHandler.containsKey(key))
				return;

			// remove ClientHandler only if it is the rigt one. It could be that a new, still active ClientHandler is already
			// registered for that IP.
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
	 * Starts the server. After that, only the specified ports can be used for connections.
	 */
	public void start() {
		personServer.start();
		auctionServer.start();
		bidServer.start();
		categoryServer.start();
	}

	/**
	 * Stops the server. No new connections are accepted. Connected clients are stopped.
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
