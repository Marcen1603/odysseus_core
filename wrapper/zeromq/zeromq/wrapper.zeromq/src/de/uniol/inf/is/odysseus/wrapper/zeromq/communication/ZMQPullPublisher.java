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
	
	byte[] curMsg = "".getBytes();
	
	public ZMQPullPublisher(ZeroMQTransportHandler transh){
		ZMQTH = transh;
		socket = ZMQTH.getContext().getContext().socket(ZMQ.REP);
//		socket.setReceiveTimeOut(1000);
		if (ZMQTH.getHost() != null && ZMQTH.getReadPort() > 0) {
			socket.connect("tcp://" + ZMQTH.getHost() + ":" + ZMQTH.getWritePort());
			LOG.debug("ZeroMQ publisher connected as tcp://" + ZMQTH.getHost() + ":" + ZMQTH.getWritePort());
			LOG.debug("ZeroMQ publisher waiting for requests.");
		}
	}
	
	@Override
	public void run() {
		while(!t.isInterrupted()){
			System.err.println("Waiting for data.");;
			byte[] data = socket.recv(0);
			if(data != null){
				try {
					System.err.println("Sending " + new String(curMsg));
					socket.send(curMsg);
					ZMQTH.getOutputStream().write(curMsg);
				} catch (Exception e) {
					LOG.error(e.getMessage());
				}
			}
		}
	}
	
	@Override
	public void send(byte[] message){
		curMsg = message;
	}
}
