package de.uniol.inf.is.odysseus.pubsub.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.TopicBuilder;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.BrokerTopologyRegistry;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.IBrokerTopology;

/**
 * 
 * @author ChrisToenjesDeye
 * 
 * @param <T>
 */
public class SubscribePO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	private List<IPredicate<? super T>> predicates;
	private SDFSchema schema;
	private String brokerName;
	private String domain;
	private List<String> topicStrings;
	private List<Topic> topics;

	public SubscribePO(List<IPredicate<? super T>> predicates,
			String brokername, SDFSchema schema, List<String> topics,
			String domain) {
		super();
		this.brokerName = brokername;
		this.schema = schema;
		this.domain = domain;
		this.topicStrings = topics;
		this.topics = TopicBuilder.ConvertStringsToTopics(topics);
		initPredicates(predicates);
	}

	public SubscribePO(SubscribePO<T> splitPO) {
		super();
		this.brokerName = splitPO.brokerName;
		this.schema = splitPO.schema;
		this.topicStrings = new ArrayList<String>(topicStrings);
		this.topics = new ArrayList<Topic>(topics);
		initPredicates(splitPO.predicates);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		@SuppressWarnings("unchecked")
		IBrokerTopology<T> b = (IBrokerTopology<T>) BrokerTopologyRegistry
				.getTopologyByDomain(domain);
		b.subscribe(predicates, this, brokerName);
	}

	@Override
	protected void process_close() throws OpenFailedException {
		@SuppressWarnings("unchecked")
		IBrokerTopology<T> b = (IBrokerTopology<T>) BrokerTopologyRegistry
				.getTopologyByDomain(domain);
		b.unsubscribe(predicates, this, brokerName);
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
		transfer(object);
	}

}
