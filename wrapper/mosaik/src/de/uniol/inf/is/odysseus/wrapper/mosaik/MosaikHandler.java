package de.uniol.inf.is.odysseus.wrapper.mosaik;

import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.offis.mosaik.api.SimProcess;
import de.offis.mosaik.api.Simulator;

public class MosaikHandler implements Runnable {

	static Logger LOG = LoggerFactory.getLogger(MosaikHandler.class);
	
//	private MosaikTransportHandler tHandler;
	private int port;
	private ServerSocket serverSocket;
	private Socket socket;
	private Simulator sim;

	public MosaikHandler(MosaikTransportHandler mosaikTransportHandler, int port) {
//		this.tHandler = mosaikTransportHandler;
		this.port = port;
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
	    	LOG.debug("Server gestartet");
	    	
	    	socket = serverSocket.accept();
	    	LOG.debug("Client accepted");
//	    	sim = new OdysseusSimulator("Odysseus");
//	    	((OdysseusSimulator) sim).setTHandler(this.tHandler);
	    	SimProcess.startSimulation(socket, sim);
		} catch(Exception e) {
			e.printStackTrace();
		}
	        
	}

}
