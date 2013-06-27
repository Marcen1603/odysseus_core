package de.uniol.inf.is.odysseus.pubsub.broker.topology;

import java.util.Observable;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.observer.PublisherObservable;

public abstract class AbstractTopology<T extends IStreamObject<?>> implements
		IBrokerTopology<T> {
	
	private String domainName;	
	private int numberOfAgents = 0;
	
	
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void update(Observable observable, Object object) {
		PublisherObservable publisherObservable = (PublisherObservable) observable;
		transfer((T) object, publisherObservable.getPublisher());
	}

}
