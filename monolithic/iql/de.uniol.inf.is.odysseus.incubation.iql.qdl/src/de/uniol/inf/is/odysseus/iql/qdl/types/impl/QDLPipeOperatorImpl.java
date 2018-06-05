package de.uniol.inf.is.odysseus.iql.qdl.types.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import de.uniol.inf.is.odysseus.iql.qdl.types.operator.PipeOperator;
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscribable;
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscriber;
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscription;

public class QDLPipeOperatorImpl extends AbstractSubscriber implements PipeOperator {
	private Map<String, Object> parameters = new HashMap<String, Object>();
	private Collection<Subscription> subscriptions = new HashSet<>();
	private String name;
	
	public QDLPipeOperatorImpl(String name) {		
		this.name = name;
	}
	
	public QDLPipeOperatorImpl(String name, Subscribable source) {		
		this.name = name;
		source.subscribeSink(this);
	}
	
	public QDLPipeOperatorImpl(String name, Subscribable source1, Subscribable source2) {		
		this.name = name;
		source1.subscribeSink(this);
		source2.subscribeSink(this);
	}
	
	@Override
	public void setParameter(String name, Object parameter) {
		parameters.put(name.toLowerCase(),  parameter);
	}
	
	@Override
	public Object getParameter(String name) {
		return parameters.get(name.toLowerCase());
	}
	
	@Override
	public Map<String, Object> getParameters() {
		return parameters;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	
	@Override
	public void subscribeSink(Subscriber sink) {
		subscribeSink(sink, 0, sink.getSubscriptionsToSource().size());

	}

	@Override
	public void subscribeSink(Subscriber sink, int sourceOutPort, int sinkInPort) {
		Subscription subs = new Subscription(this, sink, sourceOutPort, sinkInPort);
		subscriptions.add(subs);
		sink.getSubscriptionsToSource().add(subs);
	}

	@Override
	public void unsubscribeSink(Subscriber sink, int sourceOutPort,	int sinkInPort) {
		Subscription subs = new Subscription(this, sink, sourceOutPort, sinkInPort);
		subscriptions.remove(subs);
		sink.getSubscriptionsToSource().remove(subs);
	}

	@Override
	public Collection<Subscription> getSubscriptions() {
		return subscriptions;
	}

}
