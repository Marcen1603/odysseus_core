package de.uniol.inf.is.odysseus.base;

/**
 * @author Marco Grawunder
 */
import java.util.Collection;


public interface ISubscriber<T, S extends ISubscription<T>> {
	public void subscribeTo(T source, int sinkPort, int sourcePort);
	public void unsubscribeTo(S subscription);
	public void unsubscribeSubscriptionTo(T source, int sinkPort, int sourcePort);
	public Collection<S> getSubscribedTo();
	public S getSubscribedTo(int i);
}
