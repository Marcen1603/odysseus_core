package de.uniol.inf.is.odysseus.wrapper.snet.communication;

import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.snet.communication.util.BlockingInputStreamReader;
import de.uniol.inf.is.odysseus.wrapper.snet.communication.util.TcpOutputStream;
import de.uniol.inf.is.odysseus.wrapper.snet.communication.util.WSNMessage;
import de.uniol.inf.is.odysseus.wrapper.snet.communication.util.WSNMessage.Command;


public class WSNCommunicator implements Runnable {

	protected final Logger LOG = LoggerFactory.getLogger(WSNCommunicator.class);
	
	protected Thread t;
	
	private byte[] WSNID;
	private String appName = "";

	private BlockingInputStreamReader isReader;
	private TcpOutputStream out;
	
	
	public WSNCommunicator(String applicationName, BlockingInputStreamReader inputStreamReader, TcpOutputStream output) {
		this.appName = applicationName;
		isReader = inputStreamReader;
		out = output;
		register();
	}

	public void register(){
		byte[] payload = appName.getBytes(Charset.forName("UTF-8"));
		WSNMessage registerMsg = new WSNMessage(Command.REGISTER_APPLICATION_REQ, payload);
		try {
			out.write(registerMsg.getWSNFrame());
			LOG.debug("Registering " + this.appName + " with S-Net");
			WSNMessage response = new WSNMessage(isReader.getResponse());
			if(response.getCommand() == Command.REGISTER_APPLICATION_CFM.byteCode){
				this.setWSNID(response.getPayload());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public byte[] getWSNID() {
		return WSNID;
	}

	private void setWSNID(byte[] wSNID) {
		WSNID = wSNID;
	}
}
