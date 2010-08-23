package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata.PredictionFunctionContainer;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;

public class PredictionAssignPO<M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>>> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {
	
	private PredictionFunctionContainer<M> predictionFunctions;
	private int[] pathToList;
	
	public PredictionAssignPO() {
		super();
	}
	
	public void init(int[] pathToList, PredictionFunctionContainer<M> predictionFunctions) {
		this.pathToList = pathToList;
		this.predictionFunctions = predictionFunctions;
	}
	
	public PredictionAssignPO(PredictionAssignPO<M> copy) {
		super(copy);
		predictionFunctions = new PredictionFunctionContainer<M>(copy.predictionFunctions);
		this.pathToList = new int[copy.pathToList.length];
		System.arraycopy(copy.pathToList, 0, this.pathToList, 0, copy.pathToList.length);
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		TupleHelper helper = new TupleHelper(object);
		Object listObj = helper.getObject(pathToList);
		
		if(listObj instanceof MVRelationalTuple<?>) {
			Object[] objList = ((MVRelationalTuple<?>) listObj).getAttributes();
			for(Object mvObj : objList ) {
				evaluatePredicateKey((MVRelationalTuple<M>)mvObj);
			}
		}
		transfer(object);
		return;
		
	}
	
	private void evaluatePredicateKey(MVRelationalTuple<M> tuple) {
		for(IPredicate<MVRelationalTuple<M>> pred : predictionFunctions) {
			if(pred.evaluate(tuple)) {
				tuple.getMetadata().setPredictionFunctionKey(pred);
				return;
			}
		}
		tuple.getMetadata().setPredictionFunctionKey(predictionFunctions.getDefaultPredictionFunctionKey());
		System.err.println("No Predicate can be assigned and evaluated to the current tuple (DEFAULT_PREDICTION_FUNCTION)");
		return;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
	}
	
	@Override
	public PredictionAssignPO<M> clone() {
		return new PredictionAssignPO<M>(this);
	}

}
