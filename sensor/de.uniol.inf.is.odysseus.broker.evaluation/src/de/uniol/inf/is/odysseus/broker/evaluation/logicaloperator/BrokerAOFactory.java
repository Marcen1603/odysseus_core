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
package de.uniol.inf.is.odysseus.broker.evaluation.logicaloperator;

import de.uniol.inf.is.odysseus.broker.evaluation.dictionary.BrokerDictionary;

/**
 * A factory for creating BrokerAO objects.
 * 
 * @author Dennis Geesen
 */
public class BrokerAOFactory {

	/** The factory. */
	private static BrokerAOFactory factory;
		
	/** The last value which has been given to a broker. */
	private long lastValue;
	
	
	/**
	 * Instantiates a new factory.
	 */
	private BrokerAOFactory() {
		this.lastValue = System.currentTimeMillis();
	}
	
	/**
	 * Gets the factory.
	 *
	 * @return the factory
	 */
	public static synchronized BrokerAOFactory getFactory(){
		if(factory==null){
			factory = new BrokerAOFactory();			
		}
		return factory;
	}
	
	/**
	 * Creates a new {@link BrokerAO}.
	 * Assures that each created {@link BrokerAO} (including with same identifier) has a unique value.
	 *
	 *
	 * @param identifier the identifier
	 * @return the created broker
	 */
	public BrokerAO createBrokerAO(String identifier){
		// create a really unique identifier
		long currentTime = System.currentTimeMillis();
		while(this.lastValue==currentTime){		
			currentTime++;
		}
		this.lastValue=currentTime;
		BrokerAO brokerAO = new BrokerAO(identifier);
		brokerAO.setGeneratedTime(currentTime);
		if(BrokerDictionary.getInstance().brokerExists(identifier)){
			brokerAO.setSchema(BrokerDictionary.getInstance().getSchema(identifier));
			brokerAO.setQueueSchema(BrokerDictionary.getInstance().getQueueSchema(identifier));
		}
		return brokerAO;		
	}
	
	
	
	
}
