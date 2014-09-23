package de.uniol.inf.is.odysseus.peer.recovery.tuplestore;

import java.util.ArrayDeque;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class ArrayDequeRecoveryTupleStore implements IRecoveryTupleStore{
	
	private ArrayDeque<IStreamObject<? extends ITimeInterval>> tupleStore;

	public ArrayDequeRecoveryTupleStore() {
		tupleStore = new ArrayDeque<IStreamObject<? extends ITimeInterval>>();
	}
	
	@Override
	public void saveTuple(IStreamObject<? extends ITimeInterval> tuple) {
		tupleStore.addLast(tuple);
	}

	@Override
	public IStreamObject<? extends ITimeInterval> getNextTuple() {
		IStreamObject<? extends ITimeInterval> first = tupleStore.getFirst();
		tupleStore.removeFirst();
		return first;
	}
}
