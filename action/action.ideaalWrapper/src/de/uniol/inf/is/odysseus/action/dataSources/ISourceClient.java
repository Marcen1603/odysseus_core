/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.action.dataSources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * Abstract client for reading data from sources and
 * sending it as a tuple to {@link StreamClient}s
 * @author Simon Flandergan
 *
 */
public abstract class ISourceClient implements Runnable{
	protected Logger logger;
	
	private List<StreamClient> clients;
	private long tupleCount = 0;
	
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

		long start = System.currentTimeMillis();
		boolean run = true;
		while (run) {
			run  = this.processData();
			
			//break if all clients disconnected
			if (this.clients.size() < 1){
				break;
			}
		}
		cleanUp();
		
		this.logger.info("Stopped producing tuples ...");
		
		long runtime = System.currentTimeMillis()-start;
		
		long hours = runtime / (60 * 60* 1000);
		long balance = runtime % (60 * 60* 1000);
		
		long mins = balance / (60 * 1000);
		balance %= (60*1000);
		
		long secs = balance / 1000;
		
		this.logger.info("Generator run for: "+
				hours+ "h "+
				mins+"min "+
				secs+"sec ...");
		
		this.logger.info("... And produced "+this.tupleCount+" tuples");
		
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
	protected abstract boolean processData();
	
	/**
	 * Cleanup method. Called when thread ends
	 */
	protected abstract void cleanUp();

	/**
	 * Returns schema of tuples sent by server
	 * @return
	 */
	public abstract SDFSchema getSchema();

	protected void sendTupleToClients(Tuple<IMetaAttribute> tuple) {
		//send tuple to clients
		this.logger.debug("Sent tuple :"+tuple.toString());
		this.tupleCount++;
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
