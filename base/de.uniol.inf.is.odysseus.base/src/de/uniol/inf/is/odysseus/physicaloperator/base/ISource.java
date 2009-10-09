package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.ISubscribable;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;

/**
 * Interface for data sources in a query graph.
 * 
 * It provides data to subscribed {@link ISink sinks}. Subscriptions are managed
 * via {@link #subscribe(ISink, int)} and {@link #unsubscribe(ISink, int)}.
 * Subscriptions can be added/removed at any time (before or after
 * {@link #open()} was called).
 * 
 * @author Jonas Jacobi
 */
public interface ISource<T> extends IPhysicalOperator, ISubscribable<ISink<? super T>, PhysicalSubscription<ISink<? super T>>>{
	/**
	 * Gets called initially once from every subscribed sink. Setup work should
	 * be done in here.
	 * 
	 * @throws OpenFailedException
	 *             if the source can't be initialised e.g. because some needed
	 *             resources like socket connections can't be allocated.
	 */
	public void open() throws OpenFailedException;

	/**
	 * Calls {@link ISink#process(T)} on all subscribed {@link ISink sinks}.
	 * 
	 * @param object
	 *            the parameter for processNext.
	 */
	public void transfer(T object, int sourcePort);
	public void transfer(T object);
	
	/**
	 * Same as above, but for transfering a batch of elements.
	 */
	public void transfer(Collection<T> object, int sourcePort);
	public void transfer(Collection<T> object);
	
	/**
	 * Close down the connection/do not read any more data
	 */
	public void close();

	public void sendPunctuation(PointInTime punctuation);
	

}
