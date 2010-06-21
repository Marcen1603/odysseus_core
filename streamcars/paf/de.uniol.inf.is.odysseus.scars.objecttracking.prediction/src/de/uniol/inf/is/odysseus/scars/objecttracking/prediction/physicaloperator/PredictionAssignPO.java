package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.logicaloperator.PredictionAssignAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata.PredictionFunctionContainer;

public class PredictionAssignPO<M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>>> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {
	
	private PredictionFunctionContainer<M> predictionFunctions;
	
	public PredictionAssignPO(PredictionAssignAO<M> predictionAO) {
		super();
		predictionFunctions = predictionAO.getPredictionFunctions();
	}
	
	public PredictionAssignPO(PredictionAssignPO<M> copy) {
		super(copy);
		predictionFunctions = new PredictionFunctionContainer<M>(copy.predictionFunctions);
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		for(IPredicate<MVRelationalTuple<M>> pred : predictionFunctions) {
			if(pred.evaluate(object)) {
				object.getMetadata().setPredictionFunctionKey(pred);
				transfer(object);
				return;
			}
		}
		
		object.getMetadata().setPredictionFunctionKey(predictionFunctions.getDefaultPredictionFunctionKey());
		transfer(object);
		return;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
//		sendPunctuation(timestamp);
	}
	
	@Override
	public PredictionAssignPO<M> clone() {
		return new PredictionAssignPO<M>(this);
	}

}
