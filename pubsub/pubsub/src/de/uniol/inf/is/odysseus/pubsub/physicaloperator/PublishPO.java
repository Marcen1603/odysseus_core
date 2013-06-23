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

public class PublishPO<T extends IStreamObject<?>> extends AbstractSink<T> {

	private String topologyType;
	private String domain;
	private List<String> topicStrings;
	private List<Topic> topics;
	private IBrokerTopology<T> brokerTopology;
	private String identifier;

	public PublishPO(String topologyType, String domain, List<String> topics) {
		super();
		this.topologyType = topologyType;
		this.domain = domain;
		this.topicStrings = topics;
		this.topics = TopicHelper.convertStringsToTopics(topics);
		this.identifier = UUID.randomUUID().toString();
	}

	@Override
	protected void process_next(T object, int port) {
		brokerTopology.transfer(object, this);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_open() throws OpenFailedException {
		brokerTopology = (IBrokerTopology<T>) BrokerTopologyRegistry
				.<T>getTopologyByTypeAndDomain(topologyType, domain);
		if (!topics.isEmpty()){
			brokerTopology.advertise(topics, this);			
		}
	}

	@Override
	protected void process_close() throws OpenFailedException {
		if (!topics.isEmpty()){
			brokerTopology.unadvertise(topics, this);			
		}
		BrokerTopologyRegistry.unregister(domain);
	}

	@Override
	public AbstractSink<T> clone() {
		return new PublishPO<T>(this.topologyType, this.domain,
				this.topicStrings);
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

}
