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

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.TopicHelper;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.BrokerTopologyRegistry;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.IBrokerTopology;

/**
 * Physical Subscribe Operator. The Operator provides the subscribe functionality in publish/Subscribe systems.
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class SubscribePO<T extends IStreamObject<?>> extends AbstractPipe<T, T> implements Observer{

	private List<IPredicate<? super T>> predicates;
	private String brokerName;
	private String domain;
	private List<String> topicStrings;
	private List<Topic> topics;
	private String identifier;
	private IBrokerTopology<T> brokerTopology;

	public SubscribePO(List<IPredicate<? super T>> predicates,
			String brokername, List<String> topics,
			String domain) {
		super();
		this.brokerName = brokername;
		this.domain = domain;
		this.topicStrings = topics;
		this.topics = TopicHelper.convertStringsToTopics(topics);
		initPredicates(predicates);
		this.identifier = UUID.randomUUID().toString();
	}

	public SubscribePO(SubscribePO<T> splitPO) {
		super();
		this.brokerName = splitPO.brokerName;
		this.topicStrings = new ArrayList<String>(topicStrings);
		this.topics = new ArrayList<Topic>(topics);
		initPredicates(splitPO.predicates);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_open() throws OpenFailedException {
		brokerTopology = (IBrokerTopology<T>) BrokerTopologyRegistry
				.getTopologyByDomain(domain);
		if (brokerTopology != null) {
			BrokerTopologyRegistry.register(domain);
			if (!topics.isEmpty() || !predicates.isEmpty()) {
				brokerTopology.subscribe(predicates, topics, brokerName, this);
			}
		} else {
			BrokerTopologyRegistry.putSubscriberIntoPendingList(domain, this);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void subscribe(IBrokerTopology<?> topology){
		brokerTopology = (IBrokerTopology<T>) topology;
		if (brokerTopology != null) {
			BrokerTopologyRegistry.register(domain);
			if (!topics.isEmpty() || !predicates.isEmpty()) {
				brokerTopology.subscribe(predicates, topics, brokerName, this);
			}
		}
	}

	@Override
	protected void process_close() {
		@SuppressWarnings("unchecked")
		IBrokerTopology<T> b = (IBrokerTopology<T>) BrokerTopologyRegistry
				.getTopologyByDomain(domain);
		if (b != null) {
			if (!topics.isEmpty() || !predicates.isEmpty()) {
				b.unsubscribe(predicates, topics, brokerName, this);
			}
			BrokerTopologyRegistry.unregister(domain);
		}
	}

	@Override
	public AbstractPipe<T, T> clone() {
		return new SubscribePO<T>(this);
	}

	private void initPredicates(List<IPredicate<? super T>> predicates) {
		this.predicates = new ArrayList<IPredicate<? super T>>(
				predicates.size());
		for (IPredicate<? super T> p : predicates) {
			this.predicates.add(p.clone());
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		// No transfer in process_next, because broker calls receive
		// transfer(object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable observable, Object object) {
		transfer((T) object);	
	}

	public String getIdentifier() {
		return identifier;
	}

	public List<IPredicate<? super T>> getPredicates() {
		return predicates;
	}

	public List<Topic> getTopics() {
		return topics;
	}


}
