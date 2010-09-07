package de.uniol.inf.is.odysseus.scars.operator.jdvesink.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class DatagramServer implements IServer {

	private DatagramSocket socket;
	
	private int port;
	private InetAddress address;

	public DatagramServer(String adress, int port) {
		try {
			this.port = port;
			this.address = InetAddress.getByName(adress);
			this.socket = new DatagramSocket();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void sendData(ByteBuffer buffer) {
		DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.capacity(), this.address, port);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		this.socket.close();
	}

	@Override
	public void start() {
		
	}

}
