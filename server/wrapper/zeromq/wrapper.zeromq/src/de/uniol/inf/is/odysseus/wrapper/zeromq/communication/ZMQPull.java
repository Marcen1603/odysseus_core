package de.uniol.inf.is.odysseus.wrapper.zeromq.communication;

import org.zeromq.ZMQ;

import de.uniol.inf.is.odysseus.wrapper.zeromq.ZeroMQTransportHandler;

public class ZMQPull extends AZMQConnector {
	private int timeout;

	public ZMQPull(ZeroMQTransportHandler transh){
		ZMQTH = transh;
		timeout = ZMQTH.getTimeout();
		socket = ZMQTH.getContext().getContext().socket(ZMQ.PULL);	
		socket.setReceiveTimeOut(timeout);
		if (ZMQTH.getHost() != null && ZMQTH.getReadPort() > 0) {
			socket.connect("tcp://" + ZMQTH.getHost() + ":" + ZMQTH.getReadPort());
			LOG.debug("ZeroMQ consumer connected to mosaik / tcp://" + ZMQTH.getHost() + ":" + ZMQTH.getReadPort());
			LOG.debug("ZeroMQ consumer requesting new input with a timeout of " + timeout + " seconds.");
		}
	}

	@Override
	public void run() {
		while(!t.isInterrupted()){
			byte[] data = socket.recv(0);
			if(data != null){
//				System.out.println("data != null");
				try{
					String[] stringArray = new String[1];
					stringArray[0] = new String(data).trim();
					ZMQTH.fireProcess(stringArray);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void send(byte[] message) {
		//Pull..
	}

}
