package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.buffer;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

/***
 * Implements an ArrayDeque Buffer, which should be able to buffer tuples while loadbalancing is running.
 * Deprecated because subscriptions can be suspended.
 * @author Carsten Cordes
 *
 */
@Deprecated
public class ArrayDequeLoadBalancingBuffer implements ILoadBalancingBuffer{
	
	/***
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(ArrayDequeLoadBalancingBuffer.class);
	
	/**
	 * Buffer
	 */
	private ArrayDeque<IStreamObject<? extends ITimeInterval>> tupleStore;

	/***
	 * Constructor
	 */
	public ArrayDequeLoadBalancingBuffer() {
		tupleStore = new ArrayDeque<IStreamObject<? extends ITimeInterval>>();
	}
	
	/**
	 * Saves tuple in Buffer
	 */
	@Override
	public void saveTuple(IStreamObject<? extends ITimeInterval> tuple) {
		tupleStore.addLast(tuple);
	}

	/**
	 * Gets Tuple from buffer if there are any.
	 */
	@Override
	public IStreamObject<? extends ITimeInterval> getNextTuple() {
		try {
			IStreamObject<? extends ITimeInterval> first = tupleStore.getFirst();
			tupleStore.removeFirst();
			return first;
		}
		catch(NoSuchElementException e) {
			LOG.debug("Buffer is empty.");
			return null;
		}
	}
}
