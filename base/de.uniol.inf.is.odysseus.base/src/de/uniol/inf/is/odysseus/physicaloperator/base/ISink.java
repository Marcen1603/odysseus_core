package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.ISubscriber;
import de.uniol.inf.is.odysseus.base.PointInTime;

/**
 * Interface for data sinks in a query graph.
 * 
 * New data get pushed into a sink from the {@link ISource sources} it is
 * subscribed to by calling {@link #process(Object, int, boolean)}. If a source
 * has no more data (and therefore wont call
 * {@link #process(Object, int, boolean)} again) {@link #done(int)} gets called.
 * Setup and cleanup work is done in {@link #open()} and {@link #close()}
 * respectively.
 * 
 * @author Jonas Jacobi
 */
public interface ISink<T> extends IPhysicalOperator, ISubscriber<ISource<? extends T>, PhysicalSubscription<ISource<? extends T>>> {
		/**
	 * Process an element.
	 * 
	 * @param object
	 *            the element to process
	 * @param port
	 *            the input port, the element came from
	 * @param isReadOnly
	 *            true if object may not be modified by the sink, false
	 *            otherwise.
	 */
	public void process(T object, int port, boolean isReadOnly);

	/**
	 * Same as above, but for processing of a batch of elements.
	 */
	public void process(Collection<? extends T> object, int port,
			boolean isReadOnly);

	/**
	 * Indicates that a source wont call process again.
	 * 
	 * @param port
	 *            the input port the source is connected to.
	 */
	public void done(int port);

	public void processPunctuation(PointInTime timestamp);

}
