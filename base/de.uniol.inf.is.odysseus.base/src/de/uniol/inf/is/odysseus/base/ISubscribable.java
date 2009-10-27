package de.uniol.inf.is.odysseus.base;

import java.util.Collection;


public interface ISubscribable<T, S extends ISubscription<T>> {
	/**
	 * Subscribe will be called at a source operator. The first
	 * parameter of this method call will be the sink.
	 * 
	 * So, if B is the following operator of A, then
	 * A.subscribe(B) will be called.
	 * 
	 * A -> B
	 *
	 */
	public void subscribe(T sink, int sinkPort, int sourcePort);
	
	/**
	 * Removes a subscription installed by {@link ISubscribable#subscribe(Object, int, int)}
	 */
	public void unsubscribe(T sink, int sinkPort, int sourcePort);
	
	/**
	 * Removes a subscription installed by the methods
	 * {@link ISubscriber#subscribeTo(Object, int, int)} or
	 * {@link ISubscribable#subscribe(Object, int, int)}
	 * @param subscription
	 */
	public void unsubscribe(S subscription);
	public Collection<S> getSubscriptions();
}
