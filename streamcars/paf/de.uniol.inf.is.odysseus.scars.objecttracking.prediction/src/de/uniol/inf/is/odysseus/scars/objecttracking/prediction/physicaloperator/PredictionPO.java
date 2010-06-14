package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;



public class PredictionPO<T extends ITimeInterval & IProbability & ILatency & IPredictionFunctionKey<K>, K> extends AbstractPipe<T, T> {
	
	private PointInTime currentTime;
	private MVRelationalTuple<T> currentScan;
	
	@Override
	public AbstractPipe<T, T> clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(T object, int port) {
		if(port == 0) {
			currentTime = object.getStart();
		} else if(port == 1) {
			
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) { }

}
