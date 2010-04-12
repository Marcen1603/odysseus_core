package de.uniol.inf.is.odysseus.action.dataSources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Abstract client for reading data from sources and
 * sending it as a tuple to {@link StreamClient}s
 * @author Simon Flandergan
 *
 */
public abstract class ISourceClient implements Runnable{
	protected List<StreamClient> clients;
	protected Logger logger;
	
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
		this.logger.info("Started producing tuples ...");
		boolean run = true;
		while (run) {
			run  = this.processData();
			
			//break if all clients disconnected
			if (this.clients.size() < 1){
				break;
			}
		};
		cleanUp();
		
		this.logger.info("Stopped producing tuples ...");
		
		//clean up for clients
		synchronized (clients) {
			for (StreamClient client : clients){
				client.closeSocket();
			}
			this.clients.clear();
		}
		
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

	public void sendTupleToClients(RelationalTuple<IMetaAttribute> tuple) {
		//send tuple to clients
		this.logger.debug("Sent tuple :"+tuple.toString());
		synchronized (clients) {
			Iterator<StreamClient> iterator = this.clients.iterator();
			while(iterator.hasNext()){
				try {
					iterator.next().writeObject(tuple);
				}catch (IOException e){
					this.logger.error("Client exited: ");
					this.logger.error(e.getMessage());
					iterator.remove();
				}
			}
		}
	}

	
	
}
