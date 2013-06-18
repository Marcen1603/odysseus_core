package de.uniol.inf.is.odysseus.pubsub.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.TopicBuilder;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.BrokerTopologyRegistry;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.IBrokerTopology;

public class PublishPO<T extends IStreamObject<?>> extends AbstractSink<T> {

	private String topologyType;
	private String domain;
	private List<String> topicStrings;
	private List<Topic> topics;

	public PublishPO(String topologyType, String domain, List<String> topics) {
		super();
		this.topologyType = topologyType;
		this.domain = domain;
		this.topicStrings = topics;
		this.topics = TopicBuilder.ConvertStringsToTopics(topics);
	}

	@Override
	protected void process_next(T object, int port) {
		IBrokerTopology<?> b = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain(topologyType, domain);
		b.transfer(object);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		@SuppressWarnings("unchecked")
		IBrokerTopology<T> b = (IBrokerTopology<T>) BrokerTopologyRegistry
				.getTopologyByTypeAndDomain(topologyType, domain);
		b.advertise(topics, this);
	}

	@Override
	protected void process_close() throws OpenFailedException {
		@SuppressWarnings("unchecked")
		IBrokerTopology<T> b = (IBrokerTopology<T>) BrokerTopologyRegistry
				.getTopologyByTypeAndDomain(topologyType, domain);
		b.unadvertise(topics, this);
	}

	@Override
	public AbstractSink<T> clone() {
		return new PublishPO<T>(this.topologyType, this.domain,
				this.topicStrings);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

	}

}
