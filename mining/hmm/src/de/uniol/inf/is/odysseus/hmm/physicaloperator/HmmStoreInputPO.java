package de.uniol.inf.is.odysseus.hmm.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class HmmStoreInputPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void process_next(Tuple<M> object, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractPipe<Tuple<M>, Tuple<M>> clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
