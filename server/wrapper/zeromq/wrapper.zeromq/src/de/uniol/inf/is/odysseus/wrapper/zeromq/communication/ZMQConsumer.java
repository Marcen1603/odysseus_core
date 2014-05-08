package de.uniol.inf.is.odysseus.wrapper.zeromq.communication;

import java.nio.ByteBuffer;

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
public class ZMQConsumer implements Runnable {
	private Thread t;
	private ZeroMQTransportHandler ZMQTH;
	private Socket subscriber;

	public ZMQConsumer(ZeroMQTransportHandler transh){
		ZMQTH = transh;
		subscriber = ZMQTH.getContext().socket(ZMQ.SUB);
		if (ZMQTH.getHost() != null && ZMQTH.getReadPort() > 0) {
			subscriber.bind("tcp://" + ZMQTH.getHost() + ":" + ZMQTH.getReadPort());
		}
	}
	
	@Override
	public void run() {
		while(true){
			byte[] data = subscriber.recv(1);
			if(data != null){
				try{
					ByteBuffer wrapped = ByteBuffer.wrap(data);
					wrapped.position(wrapped.limit());
					ZMQTH.fireProcess(wrapped);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
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
