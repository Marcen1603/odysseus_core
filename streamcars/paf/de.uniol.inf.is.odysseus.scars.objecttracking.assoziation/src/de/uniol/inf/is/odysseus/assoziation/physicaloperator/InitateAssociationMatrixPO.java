package de.uniol.inf.is.odysseus.assoziation.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

public class InitateAssociationMatrixPO<T extends IProbability>  extends AbstractPipe<MVRelationalTuple<T>, MVRelationalTuple<T>> {

	private MVRelationalTuple<T> oldTuple;
	private MVRelationalTuple<T> newTuple;
	
	@Override
	public AbstractPipe<MVRelationalTuple<T>, MVRelationalTuple<T>> clone() {
		return null;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(MVRelationalTuple<T> object, int port) {
		if(port == 0) {
			newTuple = object;
		} else if(port == 1) {
			oldTuple = object;
		}
		
		if(newTuple != null && oldTuple != null) {
			newTuple.addAttributeValue(2, oldTuple.getAttribute(1));
			

			
			newTuple = null;
			oldTuple = null;
		}
	}
	
	private void createCorrelationMatrix(MVRelationalTuple<T> tuple) {
		
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}

}
