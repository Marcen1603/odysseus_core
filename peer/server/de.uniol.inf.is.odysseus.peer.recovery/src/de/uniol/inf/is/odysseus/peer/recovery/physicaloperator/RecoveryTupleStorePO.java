package de.uniol.inf.is.odysseus.peer.recovery.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.peer.recovery.tuplestore.ArrayDequeRecoveryTupleStore;
import de.uniol.inf.is.odysseus.peer.recovery.tuplestore.IRecoveryTupleStore;

public class RecoveryTupleStorePO<T extends IStreamObject<? extends ITimeInterval>>
		extends AbstractPipe<T, T> {
	
	IRecoveryTupleStore tupleStore; 
	
	public RecoveryTupleStorePO() {
		super();
		tupleStore = new ArrayDequeRecoveryTupleStore();
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		// TODO How to synchronize when the new peer is there?
		
		// First, just save the incoming tuples
		tupleStore.saveTuple(object);
	}

}
