package de.uniol.inf.is.odysseus.action.dataSources;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Abstract client for reading data from sources and
 * sending it as a tuple to {@link StreamClient}s
 * @author Simon Flandergan
 *
 */
public abstract class ISourceClient extends Thread{
	protected List<StreamClient> clients;
	
	public ISourceClient(){
		clients = new ArrayList<StreamClient>();
	}
	
	/**
	 * Adds a new Client which should receive tuples
	 * @param streamClient
	 */
	public void addClient(StreamClient streamClient) {
		synchronized (this.clients) {
			this.clients.add(streamClient);
		}
	}
	
	@Override
	public void run() {
		boolean run = true;
		while (run) {
			run  = this.processData();
		};
		cleanUp();
		
		//clean up for clients
		for (StreamClient client : clients){
			client.closeSocket();
		}
		this.clients.clear();
	}
	
	/**
	 * Method called by thread. Should implement processing logic.
	 * @return true if thread should continue, false if it should end
	 */
	public abstract boolean processData();
	
	/**
	 * Cleanup method. Called when thread ends
	 */
	public abstract void cleanUp();

	/**
	 * Returns schema of tuples sent by server
	 * @return
	 */
	public abstract SDFAttributeList getSchema();

	
	
}
