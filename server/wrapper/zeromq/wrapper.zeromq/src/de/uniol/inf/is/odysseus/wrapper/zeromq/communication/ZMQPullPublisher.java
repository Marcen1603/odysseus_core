package de.uniol.inf.is.odysseus.wrapper.zeromq.communication;

import org.zeromq.ZMQ;

import de.uniol.inf.is.odysseus.wrapper.zeromq.ZeroMQTransportHandler;

/**
 * 
 * This wrapper uses a Java implementation of ZeroMQ (see packages org.zeromq and zmq). ZeroMQ is licensed under LGPL (see: license/lgpl-3.0.txt) and can only be used or distributed as specified in the LGPL license.
 * 
 * @author Jan Benno Meyer zu Holte
 *
 */
public class ZMQPullPublisher extends AZMQConnector {
	
	public ZMQPullPublisher(ZeroMQTransportHandler transh){
		ZMQTH = transh;
		ZMQ.Socket socket = ZMQTH.getContext().getContext().socket(ZMQ.REP);	
		if (ZMQTH.getHost() != null && ZMQTH.getReadPort() > 0) {
			socket.connect("tcp://" + ZMQTH.getHost() + ":" + ZMQTH.getReadPort());
			LOG.debug("ZeroMQ consumer connected to tcp://" + ZMQTH.getHost() + ":" + ZMQTH.getReadPort());
			LOG.debug("ZeroMQ publisher waiting for request when started.");
		}
	}
	
	@Override
	public void run() {
	}
	
	@Override
	public void send(byte[] message){
		while(!t.isInterrupted()){
			byte[] data = socket.recv(0);
			if(data != null){
				try {
					socket.send(message);
					ZMQTH.getOutputStream().write(message);
				} catch (Exception e) {
					LOG.error(e.getMessage());
				}
			}
		}
	}
}
