package de.uniol.inf.is.odysseus.broker.sensors.server;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import de.uniol.inf.is.odysseus.broker.sensors.generator.StreamType;

public class StreamServer extends Thread {

	private int port;
	private StreamType type;

	public StreamServer(int port, StreamType type) {
		this.port = port;
		this.type = type;
	}

	public static void startNew(int port, StreamType type){
		StreamServer server = new StreamServer(port, type);
		server.start();
	}
	
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
