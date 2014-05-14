package de.uniol.inf.is.odysseus.wrapper.zeromq.communication;

import java.nio.ByteBuffer;

import org.zeromq.ZMQ;

import de.uniol.inf.is.odysseus.wrapper.zeromq.ZeroMQTransportHandler;

/**
 * 
 * This wrapper uses a Java implementation of ZeroMQ (see packages org.zeromq and zmq). ZeroMQ is licensed under LGPL (see: license/lgpl-3.0.txt) and can only be used or distributed as specified in the LGPL license.
 * 
 * @author Jan Benno Meyer zu Holte
 *
 */
public class ZMQPushConsumer extends AZMQConnector {

	public ZMQPushConsumer(ZeroMQTransportHandler transh){
		ZMQTH = transh;
		socket = ZMQTH.getContext().getContext().socket(ZMQ.SUB);
		if (ZMQTH.getHost() != null && ZMQTH.getReadPort() > 0) {
			socket.bind("tcp://" + ZMQTH.getHost() + ":" + ZMQTH.getReadPort());
			LOG.debug("ZeroMQ consumer created listening on tcp://" + ZMQTH.getHost() + ":" + ZMQTH.getReadPort());
		}
	}
	
	@Override
	public void run() {
		while(!t.isInterrupted()){
			byte[] data = socket.recv(1);
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

	@Override
	public void send(byte[] message) {
		// no operations since is consumer
	}
}
