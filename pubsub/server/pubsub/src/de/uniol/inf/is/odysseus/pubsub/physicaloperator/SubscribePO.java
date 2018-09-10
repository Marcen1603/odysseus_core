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
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicates;
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
public class SubscribePO<T extends IStreamObject<?>> extends AbstractPipe<T, T> implements Observer, IHasPredicates{

	private List<IPredicate<? super T>> predicates;
	private boolean newBrokerNeeded;
	private String domain;
	private List<String> topicStrings;
	private List<Topic> topics;
	private String identifier;
	private IBrokerTopology<T> brokerTopology;

	public SubscribePO(List<IPredicate<? super T>> predicates,
			boolean newBrokerNeeded, List<String> topics,
			String domain) {
		super();
		this.newBrokerNeeded = newBrokerNeeded;
		this.domain = domain;
		this.topicStrings = topics;
		this.topics = TopicHelper.convertStringsToTopics(topics);
		initPredicates(predicates);
		this.identifier = UUID.randomUUID().toString();
	}

	public SubscribePO(SubscribePO<T> splitPO) {
		super();
		this.newBrokerNeeded = splitPO.newBrokerNeeded;
		this.topicStrings = new ArrayList<String>(topicStrings);
		this.topics = new ArrayList<Topic>(topics);
		initPredicates(splitPO.predicates);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable observable, Object object) {
		// transfer object to next operator, object send from broker via Observer
		transfer((T) object);
	}



	@SuppressWarnings("unchecked")
	@Override
	protected void process_open() throws OpenFailedException {
		// Get topology from registry
		brokerTopology = (IBrokerTopology<T>) BrokerTopologyRegistry
				.getTopologyByDomain(domain);
		// if topology was found, register and subscribe if predicates or topics available
		if (brokerTopology != null) {
			BrokerTopologyRegistry.register(domain);
			if (!topics.isEmpty() || !predicates.isEmpty()) {
				brokerTopology.subscribe(predicates, topics, newBrokerNeeded, this);
			}
		} else {
			// if topology not exists, put subscriber into pending list
			BrokerTopologyRegistry.putSubscriberIntoPendingList(domain, this);
		}
	}

	@SuppressWarnings("unchecked")
	public void subscribe(IBrokerTopology<?> topology){
		// needed if subscribe was before topology exists
		brokerTopology = (IBrokerTopology<T>) topology;
		if (brokerTopology != null) {
			BrokerTopologyRegistry.register(domain);
			if (!topics.isEmpty() || !predicates.isEmpty()) {
				brokerTopology.subscribe(predicates, topics, newBrokerNeeded, this);
			}
		}
	}

	@Override
	protected void process_close() {
		// unsubscribe and unregister from topology
		if (brokerTopology != null) {
			if (!topics.isEmpty() || !predicates.isEmpty()) {
				brokerTopology.unsubscribe(predicates, topics, newBrokerNeeded, this);
			}
			BrokerTopologyRegistry.unregister(domain);
		}
	}

	private void initPredicates(List<IPredicate<? super T>> predicates) {
		this.predicates = new ArrayList<IPredicate<? super T>>(
				predicates.size());
		for (IPredicate<? super T> p : predicates) {
			this.predicates.add(p.clone());
		}
	}

	public String getIdentifier() {
		return identifier;
	}

	public List<IPredicate<? super T>> getPredicates2() {
		return predicates;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<IPredicate<?>> getPredicates() {
		// This is necessary because of cast problems :-/
		List<IPredicate<?>> preds = new ArrayList<>(predicates.size());
		for (IPredicate p:predicates){
			preds.add(p);
		}
		return preds;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		// subscriber operator is like a source. Objects will send by observer
		// from brokers in publish/Subscribe systems. See update() Method.
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO: Is there a way, that subscriptions can get punctuations?
		IPunctuation retPunc = punctuation;
		for (IPredicate p:predicates){
			retPunc = p.processPunctuation(punctuation);
		}
		sendPunctuation(retPunc);
	}

	@Override
	public void setPredicates(List<IPredicate<?>> predicates) {
		//TODO
	}


}
