package de.uniol.inf.is.odysseus.filtering;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

public class FilterPO<M extends IProbability> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>>  {

	
	
	public FilterPO() {
	super();	
	}
	
	public FilterPO(FilterPO<M> copy) {
	super(copy);
	}
	
	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}

	public int hashcode() {
		return 0;
	}

	public boolean equals(Object obj) {
		return false;
		
	}
}
