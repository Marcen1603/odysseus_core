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
package de.uniol.inf.is.odysseus.storing.physicaloperator;

import java.sql.Connection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.storing.DatabaseIterator;

public class DatabaseAccessPO extends AbstractSource<RelationalTuple<?>> {

	protected volatile static Logger LOGGER = LoggerFactory.getLogger(DatabaseAccessPO.class);
	
	private String tableName;
	private Connection connection;
	private boolean timesenstiv;
	
	private int starttimestamp;
	
	private Thread thread;
	
	public DatabaseAccessPO(String tableName, Connection connection, boolean timeSensitiv){
		this.tableName = tableName;
		this.connection = connection;
		this.timesenstiv = timeSensitiv;
	}	
	
	public DatabaseAccessPO(DatabaseAccessPO original) {
		this.tableName = original.tableName;
		this.connection = original.connection;
	}
	
	@Override
	protected void process_close() {
		thread.interrupt();
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		DatabaseIterator di = new DatabaseIterator(this.tableName, this.getOutputSchema(), this.connection);
		if(this.timesenstiv){
			for(int i = 0; i < getOutputSchema().size(); i++){
				if(getOutputSchema().get(i).getDatatype().getQualName() == "STARTTIMESTAMP"){
					this.starttimestamp = i;
					LOGGER.info(String.format("Found STARTTIMESTAMP(%i) for sensitive mode.",i));
				}
				if(getOutputSchema().get(i).getDatatype().getQualName() == "STARTTIMESTAMP"){
					//this.endtimestamp = i;
					LOGGER.info(String.format("Found ENDTIMESTAMP(%i) for sensitive mode.",i));
				}
			}
			thread = new TransferThread(di, timesenstiv);
		}else{
			thread = new TransferThread(di,timesenstiv);
		}
		thread.start();
	}

	@Override
	public DatabaseAccessPO clone() {	
		return new DatabaseAccessPO(this);
	}
	
	
	/*
	 * Private Transfer Thread 
	 */
	private class TransferThread extends Thread {
		DatabaseIterator di;
		boolean timesenstiv;
		
		public TransferThread(DatabaseIterator di, boolean timesenstiv) {
			this.di = di;
			this.timesenstiv = timesenstiv;
		}
		
		@Override
		public void run() {
			super.run();
			while(!interrupted()){
				if(timesenstiv){
					LOGGER.info("Start Database Transfer Thread: running in time sensitive mode.");
					timesenstivTransfer(di);
				}
				else{
					LOGGER.info("Start Database Transfer Thread: running NOT in time sensitive mode.");
					normalTransfer(di);
				}
			}	
		}
		
		private void normalTransfer(DatabaseIterator di){
			while(di.hasNext()){
				if(interrupted()){
					LOGGER.error("Interruped database transfer for table "+ tableName);
					break;
				}
				for(RelationalTuple<?> t : di.next()){
					transfer(t);			
				}
			}
			LOGGER.error("No more elements in database for table "+ tableName);
		}
		
		private void timesenstivTransfer(DatabaseIterator di){
			while(di.hasNext()){
				if(interrupted()){
					LOGGER.error("Interruped database transfer for table "+ tableName);
					break;
				}
				
				List<RelationalTuple<?>> list = di.next();
				for(int i=0;i<list.size();i++){
					RelationalTuple<?> current = list.get(i);
					transfer(list.get(i));
					if(i==(list.size()-1)){
						break;
					}

					RelationalTuple<?> next = list.get(i+1);
					
					long currentStart = current.getAttribute(starttimestamp);
					long nextStart = next.getAttribute(starttimestamp);
					
					long timeout = nextStart - currentStart;
					try {
						sleep(timeout);
					} catch (InterruptedException e) {					
						e.printStackTrace();
					}
				}						
				for(RelationalTuple<?> t : di.next()){
					transfer(t);			
				}
			}
			LOGGER.error("No more elements in database for table "+ tableName);
		}
	}

}
