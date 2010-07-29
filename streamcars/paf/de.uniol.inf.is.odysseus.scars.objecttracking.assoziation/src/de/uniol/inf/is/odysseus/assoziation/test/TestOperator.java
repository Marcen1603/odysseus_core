package de.uniol.inf.is.odysseus.assoziation.test;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;

public class TestOperator <M extends IProbability & IConnectionContainer> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	MVRelationalTuple<M> tupel;
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		tupel = object;
	}

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public MVRelationalTuple<M> returnTupel() {
		return tupel;
	}
}
