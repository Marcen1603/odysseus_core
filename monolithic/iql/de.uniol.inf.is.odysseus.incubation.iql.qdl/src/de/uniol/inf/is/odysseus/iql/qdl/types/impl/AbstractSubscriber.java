package de.uniol.inf.is.odysseus.iql.qdl.types.impl;

import java.util.Collection;
import java.util.HashSet;

import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscribable;
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscriber;
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscription;

public class AbstractSubscriber implements Subscriber {
	private Collection<Subscription> subscriptionsToSource = new HashSet<>();

	@Override
	public void subscribeToSource(Subscribable source) {
		subscribeToSource(source, 0 ,subscriptionsToSource.size());
	}


	@Override
	public void subscribeToSource(Subscribable source, int sourceOutPort,int sinkInPort) {
		Subscription subs = new Subscription(source, this, sourceOutPort, sinkInPort);
		subscriptionsToSource.add(subs);
		source.getSubscriptions().add(subs);
	}

	@Override
	public void unsubscribeToSource(Subscribable source, int sourceOutPort,	int sinkInPort) {
		Subscription subs = new Subscription(source, this, sourceOutPort, sinkInPort);
		subscriptionsToSource.remove(subs);
		source.getSubscriptions().remove(subs);		
	}

	@Override
	public Collection<Subscription> getSubscriptionsToSource() {
		return subscriptionsToSource;
	}

}
