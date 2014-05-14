package de.uniol.inf.is.odysseus.wrapper.zeromq.communication;

import java.io.ByteArrayInputStream;

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
	private int frequency;

	public ZMQPullConsumer(ZeroMQTransportHandler transh){
		ZMQTH = transh;
		params = ZMQTH.getParams();
		frequency = ZMQTH.getFrequency();
		ZMQ.Socket socket = ZMQTH.getContext().getContext().socket(ZMQ.REQ);	
		if (ZMQTH.getHost() != null && ZMQTH.getReadPort() > 0) {
			socket.connect("tcp://" + ZMQTH.getHost() + ":" + ZMQTH.getReadPort());
			LOG.debug("ZeroMQ consumer connected to tcp://" + ZMQTH.getHost() + ":" + ZMQTH.getReadPort());
			LOG.debug("ZeroMQ consumer requesting new input every " + frequency + "ms when started.");
		}
	}
	
	private byte[] getIn(String[] params) {
		 socket.send(params[0].getBytes(), 0);
		 return socket.recv(0);
	}
	
	@Override
	public void run() {
		while(!t.isInterrupted()){
			try {
				Thread.sleep(frequency);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			byte[] data = getIn(params);
			if(data != null){
				try{
					ZMQTH.setInput(new ByteArrayInputStream(data));
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
