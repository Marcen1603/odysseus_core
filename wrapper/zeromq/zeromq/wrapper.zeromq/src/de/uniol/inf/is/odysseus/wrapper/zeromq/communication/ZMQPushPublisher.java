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
public class ZMQPushPublisher extends AZMQConnector {

	public ZMQPushPublisher(ZeroMQTransportHandler transh){
		ZMQTH = transh;
		socket = ZMQTH.getContext().getContext().socket(ZMQ.PUB);
		if (ZMQTH.getHost() != null && ZMQTH.getWritePort() > 0) {
			socket.bind("tcp://" + ZMQTH.getHost() + ":" + ZMQTH.getWritePort());
		}
		socket.setHWM(512);
		socket.setBacklog(5);
		socket.setSendTimeOut(500);
		LOG.debug("ZeroMQ publisher created sending on tcp://" + ZMQTH.getHost() + ":" + ZMQTH.getWritePort());
		LOG.debug("ZeroMQ publisher sending with delay of " + ZMQTH.getDelayOfMsg() + " message(s).");
	}
	
	@Override
	public void run() {
	}
	
	@Override
	public void send(byte[] message){
		try {
			socket.send(message);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
	}

}
