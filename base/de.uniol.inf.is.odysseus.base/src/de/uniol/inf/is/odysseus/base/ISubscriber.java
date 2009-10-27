package de.uniol.inf.is.odysseus.base;

/**
 * @author Marco Grawunder
 */
import java.util.Collection;


public interface ISubscriber<T, S extends ISubscription<T>> {
	
	/**
	 * SubscribeTo will be called at a sink operator. The first
	 * parameter of this method call will be the source.
	 * 
	 * So, if B is the following operator of A, then
	 * B.subscribeTo(A) will be called.
	 * 
	 * A -> B
	 *
	 */
	public void subscribeTo(T source, int sinkPort, int sourcePort);
	
	/**
	 * Removes a subscription installed by the methods
	 * {@link ISubscriber#subscribeTo(Object, int, int)} or
	 * {@link ISubscribable#subscribe(Object, int, int)}
	 * @param subscription
	 */
	public void unsubscribeTo(S subscription);
	
	/**
	 * Removes a subscription installed by {@link ISubscriber#subscribeTo(Object, int, int)}.
	 */
	public void unsubscribeSubscriptionTo(T source, int sinkPort, int sourcePort);
	public Collection<S> getSubscribedTo();
	public S getSubscribedTo(int i);
}
