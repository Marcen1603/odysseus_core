package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.logicaloperator.PredictionAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata.PredictionFunctionContainer;

public class PredictionPO<M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>>> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private PointInTime currentTime;
	private MVRelationalTuple<M> currentScan;
	
	private String timeAttributeName;
	private String detectedObjectListName;
	
	private PredictionFunctionContainer<M> predictionFunctions;
	
	
	
	public PredictionPO(PredictionAO<M> predictionAO) {
		predictionFunctions = predictionAO.getPredictionFunctions();
	}

	public PredictionPO(PredictionPO<M> copy) {
		predictionFunctions = new PredictionFunctionContainer<M>(copy.predictionFunctions);
	}


	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	@Override
	protected void process_open() {

	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		if(port == 0) {
			currentTime = (PointInTime)object.getAttribute(0);
		} else if(port == 1) {
			currentScan = object;
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
	}
	
	@Override
	public PredictionPO<M> clone() {
		return new PredictionPO<M>(this);
	}


}
