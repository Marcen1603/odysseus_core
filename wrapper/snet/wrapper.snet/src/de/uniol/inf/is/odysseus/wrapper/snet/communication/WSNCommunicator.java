package de.uniol.inf.is.odysseus.wrapper.snet.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WSNCommunicator implements Runnable {

	protected final Logger LOG = LoggerFactory.getLogger(WSNCommunicator.class);
	
	protected Thread t;
	
	private byte[] WSNID = new byte[2];
	private String appName = "";
	
	public WSNCommunicator(String applicationName) {
		this.appName = applicationName;
	}

	public void register(){
		
	}
	
	public void unregister(){
		
	}
	
	public void subscribeService(){
		
	}
	
	public void unscubscribeService(){
		
	}
	
	public void processIncoming(byte message){
		
	}
	
	public void sendRequest(byte[] message){
		
	}
	
	public byte[] getPayload(){
		return null;
	}
	
	public void start(){
		if(t == null){
			t = new Thread(this);
			t.setName("SnetConnector");
			t.start();
			LOG.debug(this.getClass().toString() + " started.");
		} else {
			LOG.debug("Trying to start WSN-Communicator - but it was already running");
		}
	}
	
	public void close(){
		if(t != null){
			t.interrupt();
		}
		t = null;
		LOG.debug(this.getClass().toString() + " closed.");
	}

	public Thread getT() {
		return t;
	}

	public void setT(Thread t) {
		this.t = t;
	}

	@Override
	public void run() {
		
	}
}
