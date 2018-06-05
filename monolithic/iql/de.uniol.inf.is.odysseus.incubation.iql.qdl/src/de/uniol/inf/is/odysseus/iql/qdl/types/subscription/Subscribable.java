package de.uniol.inf.is.odysseus.iql.qdl.types.subscription;

import java.util.Collection;

public interface Subscribable {
	public void subscribeSink(Subscriber sink);
		
	public void subscribeSink(Subscriber sink, int sourceOutPort, int sinkInPort);
	
	public void unsubscribeSink(Subscriber sink, int sourceOutPort, int sinkInPort);

	public Collection<Subscription> getSubscriptions();
	
}
