package de.uniol.inf.is.odysseus.wrapper.snet.deprecated;

import java.io.DataInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockingInputStreamReader implements Runnable {

	protected final Logger LOG = LoggerFactory.getLogger(WSNCommunicator.class);
	protected Thread t;
	
	private DataInputStream is;
	
	public BlockingInputStreamReader(DataInputStream input) {
		is = input;
	}
		
	@Override
	public void run() {
		
	}
	
	public byte[] getResponse(){
        try {
			while (is.available() == 0) {
				try {
					Thread.sleep(250);
					//System.out.println("I'm waiting");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			byte[] response = new byte[is.available()];
			is.read(response);
			return response;
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	public void start(){
		if(t == null){
			t = new Thread(this);
			t.setName("BlockingInputStreamReader");
			t.start();
			LOG.debug(this.getClass().toString() + " started.");
		} else {
			LOG.debug("Trying to start BlockingInputStreamReader - but it was already running");
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

}
