package de.uniol.inf.is.odysseus.peer.recovery.tuplestore;

import java.util.ArrayDeque;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

@SuppressWarnings("rawtypes")
public class ArrayDequeRecoveryTupleStore implements IRecoveryTupleStore{
	
	private ArrayDeque<Tuple> tupleStore;

	public ArrayDequeRecoveryTupleStore() {
		tupleStore = new ArrayDeque<Tuple>();
	}
	
	@Override
	public void saveTuple(Tuple tuple) {
		tupleStore.addLast(tuple);
	}

	@Override
	public Tuple getNextTuple() {
		Tuple first = tupleStore.getFirst();
		tupleStore.removeFirst();
		return first;
	}
}
