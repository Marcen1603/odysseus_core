package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.buffer;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class ArrayDequeLoadBalancingBuffer implements ILoadBalancingBuffer{
	
	
	private static final Logger LOG = LoggerFactory
			.getLogger(ArrayDequeLoadBalancingBuffer.class);
	
	private ArrayDeque<IStreamObject<? extends ITimeInterval>> tupleStore;

	public ArrayDequeLoadBalancingBuffer() {
		tupleStore = new ArrayDeque<IStreamObject<? extends ITimeInterval>>();
	}
	
	@Override
	public void saveTuple(IStreamObject<? extends ITimeInterval> tuple) {
		tupleStore.addLast(tuple);
	}

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
