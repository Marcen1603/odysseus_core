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
public class ZMQPullConsumer extends AZMQConnector {
	
	private String[] params;
	private int timeout;

	public ZMQPullConsumer(ZeroMQTransportHandler transh){
		ZMQTH = transh;
		params = ZMQTH.getParams();
		timeout = ZMQTH.getTimeout();
		socket = ZMQTH.getContext().getContext().socket(ZMQ.REQ);	
		socket.setReceiveTimeOut(timeout);
		if (ZMQTH.getHost() != null && ZMQTH.getReadPort() > 0) {
			socket.connect("tcp://" + ZMQTH.getHost() + ":" + ZMQTH.getReadPort());
			LOG.debug("ZeroMQ consumer connected to tcp://" + ZMQTH.getHost() + ":" + ZMQTH.getReadPort());
			LOG.debug("ZeroMQ consumer requesting new input with a timeout of " + timeout + " seconds.");
		}
	}
	
	private byte[] getIn(String[] params) {
		 socket.send(params[0].getBytes(), 0);
		 return socket.recv(0);
	}
	
	@Override
	public void run() {
		while(!t.isInterrupted()){
			byte[] data = getIn(params);
			if(data != null){
				try{
					ZMQTH.getInput().read(data);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void send(byte[] message) {
		// no operations since is consumer
	}
}
