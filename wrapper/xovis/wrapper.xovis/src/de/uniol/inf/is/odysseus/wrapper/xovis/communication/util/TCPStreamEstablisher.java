package de.uniol.inf.is.odysseus.wrapper.xovis.communication.util;

import java.io.IOException;
import java.io.OutputStream;

import de.uniol.inf.is.odysseus.wrapper.xovis.communication.gpbObjects.ClientResponseEventStreamObj.ClientResponseEventStream;
import de.uniol.inf.is.odysseus.wrapper.xovis.communication.gpbObjects.ClientResponseEventStreamObj.ClientResponseEventStream.Status;

public class TCPStreamEstablisher implements Runnable {

	protected Thread t;

	private OutputStream out;

	public TCPStreamEstablisher(OutputStream out) {
		this.out = out;
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
			 * Setup the GPB builder instance and build the client response with
			 * Status START. This client response serves as stream establish
			 * request as well as client heartbeat.
			 */
			ClientResponseEventStream.Builder clientResponseBuilder = ClientResponseEventStream
					.newBuilder();
			ClientResponseEventStream clientResponse = clientResponseBuilder
					.setStatus(Status.START).build();
			/*
			 * Send the client response every 3 seconds.
			 */
			while (!t.isInterrupted()) {
				try {
					clientResponse.writeDelimitedTo(out);
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
