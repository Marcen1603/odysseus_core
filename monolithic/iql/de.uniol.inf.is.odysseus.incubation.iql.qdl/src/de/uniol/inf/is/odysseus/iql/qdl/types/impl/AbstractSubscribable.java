package de.uniol.inf.is.odysseus.iql.qdl.types.impl;

import java.util.Collection;
import java.util.HashSet;

import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscribable;
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscriber;
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscription;

public class AbstractSubscribable implements Subscribable {
	private Collection<Subscription> subscriptions = new HashSet<>();

	
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
