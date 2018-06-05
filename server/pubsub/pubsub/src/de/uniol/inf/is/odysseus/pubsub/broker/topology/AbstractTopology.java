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

import java.util.Observable;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.observer.PublisherObservable;

/**
 * Base abstract class for broker topologies
 * 
 * @author ChrisToenjesDeye
 * 
 */
public abstract class AbstractTopology<T extends IStreamObject<?>> implements
		IBrokerTopology<T> {
	
	private String domainName;	
	private int numberOfAgents = 0;
	
	/**
	 * returns the name of this domain
	 */
	@Override
	public String getDomain() {
		return domainName;
	}

	/**
	 * Sets the domain of the topology
	 * @param domain
	 */
	public void setDomain(String domain) {
		this.domainName = domain;
	}
	
	/**
	 * increment the number of agents, which are registered on this topology
	 * if no agent exists the topology will be deleted in BrokerTopologyRegistry
	 */
	@Override
	public void incrementNumberOfAgents(){
		numberOfAgents++;
	}
	
	/**
	 * decrement the number of agents, which are registered on this topology
	 * if no agent exists the topology will be deleted in BrokerTopologyRegistry
	 */
	@Override
	public void decrementNumberOfAgents(){
		numberOfAgents--;
	}
	
	/**
	 * returns of the topology has any registered agents (publisher or subscriber)
	 * if no agent exists the topology will be deleted in BrokerTopologyRegistry
	 * @return
	 */
	@Override
	public boolean hasAgents(){
		if (numberOfAgents > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * if publisher publishes new object to broker topology this method will be 
	 * called via Observer. transfers object to brokers.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void update(Observable observable, Object object) {
		PublisherObservable publisherObservable = (PublisherObservable) observable;
		transfer((T) object, publisherObservable.getPublisherUid());
	}

}
