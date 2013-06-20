package de.uniol.inf.is.odysseus.pubsub.physicaloperator;

import java.util.List;
import java.util.UUID;

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
	private IBrokerTopology<T> brokerTopology;
	private String identifier;

	public PublishPO(String topologyType, String domain, List<String> topics) {
		super();
		this.topologyType = topologyType;
		this.domain = domain;
		this.topicStrings = topics;
		this.topics = TopicBuilder.ConvertStringsToTopics(topics);
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
		brokerTopology.advertise(topics, this);
	}

	@Override
	protected void process_close() throws OpenFailedException {
		brokerTopology.unadvertise(topics, this);
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

}
