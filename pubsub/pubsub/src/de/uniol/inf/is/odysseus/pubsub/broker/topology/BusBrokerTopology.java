/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a joinPlan of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.pubsub.broker.topology;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.IBroker;
import de.uniol.inf.is.odysseus.pubsub.broker.SimpleBroker;

public class BusBrokerTopology<T extends IStreamObject<?>> extends AbstractBrokerTopology<T> {

	private final String TOPOLOGY_TYPE = "BusTopology";
	private List<IBroker<T>> brokerBus = new ArrayList<IBroker<T>>();;
	
	public BusBrokerTopology(){
		// needed for OSGi
	}
	
	public BusBrokerTopology(String domain){
		setDomain(domain);
	}
	
	@Override
	public String getType(){
		return TOPOLOGY_TYPE;
	}


	@Override
	public List<IBroker<T>> getBrokers() {
		// It's a Bus, so every Broker needs to be adressed, but only Brokers with Subscriptions
		return brokerBus;
	}


	@Override
	IBroker<T> getBrokerByName(String name) {
		// Find Broker in existing Bus
		for (IBroker<T> broker : brokerBus) {
			if (broker.getName().toLowerCase().equals(name.toLowerCase())){
				return broker;
			}
		}
		// If Broker with given name does not exist, create new one and return it
		IBroker<T> newBroker = new SimpleBroker<T>(name, super.getDomain());
		brokerBus.add(newBroker);
		return newBroker;
	}

	@Override
	public <E extends IStreamObject<?>> IBrokerTopology<E> getInstance(String domain) {
		return new BusBrokerTopology<E>(domain);
	}

}
