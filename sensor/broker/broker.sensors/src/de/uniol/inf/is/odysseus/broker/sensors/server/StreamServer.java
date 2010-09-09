package de.uniol.inf.is.odysseus.broker.sensors.server;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import de.uniol.inf.is.odysseus.broker.sensors.generator.StreamType;

/**
 * The server for a stream where a client can connect to.
 * 
 * @author Dennis Geesen
 */
public class StreamServer extends Thread {

	/** The listing port. */
	private int port;
	
	/** The type of the provided stream. */
	private StreamType type;

	/**
	 * Instantiates a new stream server.
	 *
	 * @param port the listing port
	 * @param type the type of the stream
	 */
	public StreamServer(int port, StreamType type) {
		this.port = port;
		this.type = type;
	}

	/**
	 * Initiates and starts a new stream server
	 *
	 * @param port the listing port
	 * @param type the type of the stream
	 */
	public static void startNew(int port, StreamType type){
		StreamServer server = new StreamServer(port, type);
		server.start();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try {
			System.out.println("Waiting for connection on port " + this.port);
			ServerSocketChannel serverChannel = ServerSocketChannel.open();
			SocketAddress port = new InetSocketAddress(this.port);
			serverChannel.socket().bind(port);
			while (true) {
				SocketChannel clientChannel = serverChannel.accept();
				System.out.println("Connection from " + clientChannel.socket().getInetAddress());
				StreamClientHandler handler = new StreamClientHandler(clientChannel, type);				
				handler.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
