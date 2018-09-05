package de.uniol.inf.is.odysseus.wrapper.xovis.communication.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import de.uniol.inf.is.odysseus.wrapper.xovis.communication.gpbObjects.ClientResponseObjectStreamObj.ClientResponseObjectStream;
import de.uniol.inf.is.odysseus.wrapper.xovis.communication.gpbObjects.ClientResponseObjectStreamObj.ClientResponseObjectStream.Status;

public class UDPStreamEstablisher implements Runnable {

	protected Thread t;

	private DatagramSocket clientSocket;
	private InetAddress host;
	private int port;

	public UDPStreamEstablisher(DatagramSocket cSocket, InetAddress host, int port) {
		this.clientSocket = cSocket;
		this.host = host;
		this.port = port;
	}

	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.setName("StreamEstablisher");
			t.start();
		}
	}

	public void close() {
		if (t != null) {
			t.interrupt();
		}
		t = null;
	}

	public Thread getT() {
		return t;
	}

	public void setT(Thread t) {
		this.t = t;
	}

	@Override
	public void run() {
		/*
		 * * Establish a new thread to keep running individually and taking *
		 * care of the client responses (stream establishing and heartbeat).
		 */
		try {
			/*
			 * * Setup the GPB builder instance and build the client * response
			 * with Status START. This client response * serves as stream
			 * establish request as well as client * heartbeat.
			 */
			ClientResponseObjectStream.Builder clientResponseBuilder = ClientResponseObjectStream
					.newBuilder();
			ClientResponseObjectStream clientResponse = clientResponseBuilder
					.setStatus(Status.START).build();
			byte[] sendData = clientResponse.toByteArray();
			DatagramPacket clientResponseDatagram = new DatagramPacket(sendData, sendData.length, host, port);
			/*
			 * Send the client response every 3 seconds.
			 */
			while (!t.isInterrupted()) {
				clientSocket.send(clientResponseDatagram);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// Crybaby
				}
			}
		} catch (IOException e) {
			System.out.println("An error occurred:");
			e.printStackTrace();
		}
	}
}
