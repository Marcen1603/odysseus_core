package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.OrAttributeResolver;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.logicaloperator.PredictionAssignAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata.PredictionFunctionContainer;

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
		pathToList = copy.pathToList;
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		
		MVRelationalTuple<?> listObj = (MVRelationalTuple<?>)OrAttributeResolver.resolveTuple(object, pathToList);
		if(listObj instanceof MVRelationalTuple<?>) {
			
			for(Object mvObj : ((MVRelationalTuple<?>) listObj).getAttributes()) {
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
		return;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) { }
	
	@Override
	public PredictionAssignPO<M> clone() {
		return new PredictionAssignPO<M>(this);
	}

}
