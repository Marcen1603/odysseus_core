package de.uniol.inf.is.odysseus.iql.qdl.types.subscription;

import java.util.Collection;

public interface Subscriber {
	public void subscribeToSource(Subscribable source);
		
	public void subscribeToSource(Subscribable source, int sourceOutPort, int sinkInPort);
	
	public void unsubscribeToSource(Subscribable source, int sourceOutPort, int sinkInPort);

	public Collection<Subscription> getSubscriptionsToSource();
}
