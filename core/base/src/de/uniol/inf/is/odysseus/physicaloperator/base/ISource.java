package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.ISubscribable;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Interface for data sources in a query graph.
 * 
 * It provides data to subscribed {@link ISink sinks}. Subscriptions are managed
 * via {@link #subscribe(ISink, int)} and {@link #unsubscribe(ISink, int)}.
 * Subscriptions can be added/removed at any time (before or after
 * {@link #open()} was called).
 * 
 * @author Jonas Jacobi, Tobias Witt
 */
public interface ISource<T> extends IPhysicalOperator,
		ISubscribable<ISink<? super T>, PhysicalSubscription<ISink<? super T>>> {
	/**
	 * Gets called initially once from every subscribed sink. Setup work should
	 * be done in here.
	 * 
	 * @throws OpenFailedException
	 *             if the source can't be initialised e.g. because some needed
	 *             resources like socket connections can't be allocated.
	 */
	public void open(IPhysicalOperator op) throws OpenFailedException;

	/**
	 * Calls {@link ISink#process(T)} on all subscribed {@link ISink sinks}.
	 * 
	 * @param object
	 *            the parameter for processNext.
	 */
	public void transfer(T object, int sourceOutPort);

	public void transfer(T object);

	/**
	 * Same as above, but for transfering a batch of elements.
	 */
	public void transfer(Collection<T> object, int sourceOutPort);

	public void transfer(Collection<T> object);

	/**
	 * Close down the connection/do not read any more data
	 */
	public void close(IPhysicalOperator op);

	public void sendPunctuation(PointInTime punctuation);

	public void sendPunctuation(PointInTime punctuation, int outPort);

	/**
	 * Removes several subscriptions in remove list to this source and
	 * subscribes a sink in one 'atomic' step, so that no transfer() can be
	 * executed between these steps.
	 * 
	 * @param remove
	 *            {@link List} of {@link PhysicalSubscription}s to remove.
	 * @param sink
	 *            new {@link ISink} to subscribe.
	 * @param sinkInPort
	 *            sinkInPort.
	 * @param sourceOutPort
	 *            sourceOutPort.
	 * @param schema
	 *            Output schema of source.
	 */
	public void atomicReplaceSink(
			List<PhysicalSubscription<ISink<? super T>>> remove,
			ISink<? super T> sink, int sinkInPort, int sourceOutPort,
			SDFAttributeList schema);

	/**
	 * Removes a subscription to this source and subscribes several sinks in the
	 * sinks list in one 'atomic' step, so that no transfer() can be executed
	 * between these steps.
	 * 
	 * @param remove
	 *            {@link PhysicalSubscription} to remove.
	 * @param sink
	 *            {@link List} of new {@link ISink}s to subscribe.
	 * @param sinkInPort
	 *            sinkInPort.
	 * @param sourceOutPort
	 *            sourceOutPort.
	 * @param schema
	 *            Output schema of source.
	 */
	public void atomicReplaceSink(
			PhysicalSubscription<ISink<? super T>> remove,
			List<ISink<? super T>> sinks, int sinkInPort, int sourceOutPort,
			SDFAttributeList schema);

}
