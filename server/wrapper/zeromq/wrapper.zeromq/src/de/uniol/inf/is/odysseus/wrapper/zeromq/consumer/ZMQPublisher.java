package de.uniol.inf.is.odysseus.wrapper.zeromq.consumer;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.wrapper.zeromq.ZeroMQTransportHandler;

public class ZMQPublisher implements Runnable {
	private Thread t;
	private ZeroMQTransportHandler ZMQTH;

	public ZMQPublisher(ZeroMQTransportHandler transh){
		ZMQTH = transh;
	}
	
	@Override
	public void run() {
		while(true){
			byte[] data = ZMQTH.subscriber.recv(1);
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
}
