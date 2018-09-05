package de.uniol.inf.is.odysseus.wrapper.zeromq.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ.Socket;

import de.uniol.inf.is.odysseus.wrapper.zeromq.ZeroMQTransportHandler;

public abstract class AZMQConnector implements Runnable {

	protected final Logger LOG = LoggerFactory.getLogger(AZMQConnector.class);
	
	protected Thread t;
	protected ZeroMQTransportHandler ZMQTH;
	protected Socket socket;
	
	public abstract void send(byte[] message);
	
	public void start(){
		if(t == null){
			t = new Thread(this);
			t.setName("AZMQConnector");
			t.start();
			LOG.debug(this.getClass().toString() + " started.");
		} else {
			LOG.debug("Trying to start ZeroMQ - but it was already running");
		}
	}
	
	public void close(){
		if(socket != null) {
			socket.close();
		}
		if(t != null){
			t.interrupt();
		}
		socket = null;
		t = null;
		LOG.debug(this.getClass().toString() + " closed.");
	}

	public Thread getT() {
		return t;
	}

	public void setT(Thread t) {
		this.t = t;
	}

	public ZeroMQTransportHandler getZMQTH() {
		return ZMQTH;
	}

	public void setZMQTH(ZeroMQTransportHandler zMQTH) {
		ZMQTH = zMQTH;
	}
}
