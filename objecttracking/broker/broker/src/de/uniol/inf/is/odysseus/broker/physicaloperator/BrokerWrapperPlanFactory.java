/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.broker.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A factory for managing BrokerPO objects.
 * 
 * @author Dennis Geesen
 */
public class BrokerWrapperPlanFactory {

	/** All existing BrokerPOs. */
	private static Map<String, BrokerPO<?>> sources = new HashMap<String, BrokerPO<?>>();

	/**
	 * Gets a BrokerPO by his name.
	 *
	 * @param brokername the name of the broker
	 * @return the physical broker operator
	 */
	public synchronized static BrokerPO<?> getPlan(String brokername) {
		BrokerPO<?> po = sources.get(brokername);
		return po;
	}

	/**
	 * Puts a new BrokerPO to the factory
	 *
	 * @param brokername the name of the broker
	 * @param s the new broker
	 */
	public synchronized static void putPlan(String brokername, BrokerPO<?> broker) {
		sources.put(brokername, broker);
	}
		
	/**
	 * Gets all brokers
	 *
	 * @return all brokers
	 */
	public synchronized static Map<String, BrokerPO<?>> getBroker() {
		return sources;
	}

	/**
	 * Clears the factory and removes all existing brokers.
	 */
	public synchronized static void clearSources() {
		sources.clear();
	}
	
	/**
	 * Checks if the factory is empty.
	 *
	 * @return true, if is empty
	 */
	public synchronized static boolean isEmpty(){
		return sources.isEmpty();
	}
	
	/**
	 * Gets all brokers as Map
	 *
	 * @return all brokers
	 */
	public synchronized static Map<String, BrokerPO<?>> getAll(){
		return sources;
	}
	
	/**
	 * Gets a list of all brokers.
	 *
	 * @return a list of all brokers
	 */
	public synchronized static List<BrokerPO<?>> getAllBrokerPOs(){
		List<BrokerPO<?>> liste = new ArrayList<BrokerPO<?>>();
		for(String key :sources.keySet()){
			liste.add(sources.get(key));
		}
		return liste;
	}
	
}
