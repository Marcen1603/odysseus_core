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

package de.uniol.inf.is.odysseus.pubsub.physicaloperator;

import java.util.List;
import java.util.UUID;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.TopicHelper;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.BrokerTopologyRegistry;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.IBrokerTopology;
import de.uniol.inf.is.odysseus.pubsub.observer.PublisherObservable;

/**
 * Physical Publish Operator. The Operator provides the publish functionality in publish/Subscribe systems.
 * 
 * @author ChrisToenjesDeye
 *
 */
public class PublishPO<T extends IStreamObject<?>> extends AbstractSink<T> {

	private String topologyType;
	private String routing;
	private String domain;
	private List<String> topicStrings;
	private List<Topic> topics;
	private IBrokerTopology<T> brokerTopology;
	private String identifier;
	private PublisherObservable<T> publisherObservable;

	public PublishPO(String topologyType, String domain, List<String> topics, String routing) {
		super();
		this.topologyType = topologyType;
		this.routing = routing;
		this.domain = domain;
		this.topicStrings = topics;
		this.topics = TopicHelper.convertStringsToTopics(topics);
		this.identifier = UUID.randomUUID().toString();
		this.publisherObservable = new PublisherObservable<T>(this.identifier);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_open() throws OpenFailedException {
		// Get topology from registry
		brokerTopology = (IBrokerTopology<T>) BrokerTopologyRegistry
				.<T> getTopologyByTypeAndDomain(topologyType, domain, routing);
		// If topology was found
		if (brokerTopology != null) {
			// register on topology and advertise if needed
			BrokerTopologyRegistry.register(domain);
			if (!topics.isEmpty()) {
				brokerTopology.advertise(topics, this.getIdentifier());
			}
			// Add Topology to Observers
			publisherObservable.addObserver(brokerTopology);
		}
	}
	
	@Override
	protected void process_next(T object, int port) {
		// Send object to topology via Observer
		publisherObservable.setElement(object);
	}

	@Override
	protected void process_close() throws OpenFailedException {
		// Remove Observer, remove advertisement and unregister
		if (brokerTopology != null) {
			publisherObservable.deleteObserver(brokerTopology);
			if (!topics.isEmpty()) {
				brokerTopology.unadvertise(topics, this.getIdentifier());
			}
			BrokerTopologyRegistry.unregister(domain);
		}
		super.process_close();
	}

	@Override
	public AbstractSink<T> clone() {
		return new PublishPO<T>(this.topologyType, this.domain,
				this.topicStrings, this.routing);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

	}

	public String getIdentifier() {
		return identifier;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public PublisherObservable<T> getPublisherObservable() {
		return publisherObservable;
	}

}
