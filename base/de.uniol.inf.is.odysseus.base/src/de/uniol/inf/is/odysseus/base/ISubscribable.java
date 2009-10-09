package de.uniol.inf.is.odysseus.base;

import java.util.Collection;


public interface ISubscribable<T, S extends ISubscription<T>> {
	public void subscribe(T sink, int sinkPort, int sourcePort);
	public void unsubscribe(T sink, int sinkPort, int sourcePort);
	public void unsubscribe(S subscription);
	public Collection<S> getSubscribtions();
}
