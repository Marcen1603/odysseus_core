package de.uniol.inf.is.odysseus;

/**
 * @author Marco Grawunder
 */
import java.util.Collection;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


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
	public void subscribeToSource(T source, int sinkInPort, int sourceOutPort, SDFAttributeList schema);
	
	/**
	 * Removes a subscription installed by the methods
	 * {@link ISubscriber#subscribeTo(Object, int, int)} or
	 * {@link ISubscribable#subscribe(Object, int, int)}
	 * @param subscription
	 */
	public void unsubscribeFromSource(S subscription);
	
	public void unsubscribeFromAllSources();
	
	/**
	 * Removes a subscription installed by {@link ISubscriber#subscribeTo(Object, int, int)}.
	 */
	public void unsubscribeFromSource(T source, int sinkInPort, int sourceOutPort, SDFAttributeList schema);
	public Collection<S> getSubscribedToSource();
	public S getSubscribedToSource(int i);
}
