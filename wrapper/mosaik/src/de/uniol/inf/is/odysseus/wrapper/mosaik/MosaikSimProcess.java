package de.uniol.inf.is.odysseus.wrapper.mosaik;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.offis.mosaik.api.SimProcess;
import de.offis.mosaik.api.Simulator;

public class MosaikSimProcess implements Runnable {

private final Logger LOG = LoggerFactory.getLogger(MosaikSimProcess.class);
	
	private Thread t;	
	private String[] args;
	private Simulator sim;
	
	public MosaikSimProcess(String[] args, Simulator sim) {
		this.args = args;
		this.sim = sim;
	}
	
	@Override
	public void run() {
		try {
			SimProcess.startSimulation(this.args, this.sim);
		} catch (Exception e) {
			LOG.info(e.getMessage());
		}
	}
	
	public void start(){
		if(t==null){
			t = new Thread(this);
			t.setName("MosaikSimProcess");
			t.start();
		}
	}
	
	public void close(){
		t.interrupt();
		t = null;
	}

}
