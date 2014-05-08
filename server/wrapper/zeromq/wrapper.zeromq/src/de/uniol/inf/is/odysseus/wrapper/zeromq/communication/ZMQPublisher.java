package de.uniol.inf.is.odysseus.wrapper.zeromq.communication;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import de.uniol.inf.is.odysseus.wrapper.zeromq.ZeroMQTransportHandler;

/**
 * 
 * This wrapper uses a Java implementation of ZeroMQ (see packages org.zeromq and zmq). ZeroMQ is licensed under LGPL (see: license/lgpl-3.0.txt) and can only be used or distributed as specified in the LGPL license.
 * 
 * @author Jan Benno Meyer zu Holte
 *
 */
public class ZMQPublisher implements Runnable {
	private Thread t;
	private ZeroMQTransportHandler ZMQTH;
	private Socket publisher;

	public ZMQPublisher(ZeroMQTransportHandler transh){
		ZMQTH = transh;
		publisher = ZMQTH.getContext().socket(ZMQ.PUB);
		if (ZMQTH.getHost() != null && ZMQTH.getWritePort() > 0) {
			publisher.bind("tcp://" + ZMQTH.getHost() + ":" + ZMQTH.getWritePort());
		}
		// publisher.setHWM(1);
		System.out.println("ZeroMQ publisher created for tcp://" + ZMQTH.getHost() + ":" + ZMQTH.getWritePort() );
	}
	
	@Override
	public void run() {
	}
	
	public void send(byte[] message){
		publisher.send(message);
	}

	public void start(){
		if(t==null){
			t = new Thread(this);
			t.start();
		}
	}
	
	public void close(){
		t.interrupt();
	}
}
