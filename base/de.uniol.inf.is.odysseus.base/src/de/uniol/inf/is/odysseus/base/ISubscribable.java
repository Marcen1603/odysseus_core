package de.uniol.inf.is.odysseus.base;

import java.util.Collection;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


public interface ISubscribable<T, S extends ISubscription<T>> {
	/**
	 * SubscribeSink will be called at a source operator. The first
	 * parameter of this method call will be the sink.
	 * 
	 * So, if B is the following operator of A, then
	 * A.subscribe(B) will be called.
	 * 
	 * A -> B
	 *
	 */
	public void subscribeSink(T sink, int sinkPort, int sourcePort, SDFAttributeList schema);
	
	/**
	 * Removes a subscription installed by {@link ISubscribable#subscribe(Object, int, int)}
	 */
	public void unsubscribeSink(T sink, int sinkPort, int sourcePort, SDFAttributeList schema);
	
	/**
	 * Removes a subscription installed by the methods
	 * {@link ISubscriber#subscribeTo(Object, int, int)} or
	 * {@link ISubscribable#subscribe(Object, int, int)}
	 * @param subscription
	 */
	public void unsubscribeSink(S subscription);
	public Collection<S> getSubscriptions();
}
