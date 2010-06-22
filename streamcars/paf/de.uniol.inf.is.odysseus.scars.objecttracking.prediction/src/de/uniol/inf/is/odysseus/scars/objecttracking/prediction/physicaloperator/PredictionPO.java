package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.ObjectrelationialSchemaAttributeResolver;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.logicaloperator.PredictionAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata.PredictionFunctionContainer;

public class PredictionPO<M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>>> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private PointInTime currentTime;
	private MVRelationalTuple<M> currentScan;
	
	private int[] timeStampPath;
	private int[] objListPath;

	private PredictionFunctionContainer<M> predictionFunctions;
	
	
	
	public PredictionPO(PredictionAO<M> predictionAO) {
		predictionFunctions = predictionAO.getPredictionFunctions();
		timeStampPath = predictionAO.getTimeStampPath();
		objListPath = predictionAO.getObjListPath();
	}

	public PredictionPO(PredictionPO<M> copy) {
		predictionFunctions = new PredictionFunctionContainer<M>(copy.predictionFunctions);
		timeStampPath = copy.timeStampPath;
		objListPath = copy.objListPath;
	}


	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		// TODO sehr simple, muss noch darauf geachtet werden das die zeitintervalle bei den zwei eingängen zusammenpassen,
		// ist jetzt nicht garantiert (sweaparea? irgendein Buffer?).
		if(port == 0) {
			currentTime = (PointInTime)ObjectrelationialSchemaAttributeResolver.resolveTuple(object, timeStampPath);
		} else if(port == 1) {
			currentScan = object;
		}
		
		if(currentTime != null && currentScan != null) {
			predictData();
			MVRelationalTuple<M> tmp = currentScan;
			currentTime = null;
			currentScan = null;
			transfer(tmp);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void predictData() {
		MVRelationalTuple<?> list = (MVRelationalTuple<?>)ObjectrelationialSchemaAttributeResolver.resolveTuple(currentScan, objListPath);
		for(MVRelationalTuple<M> obj : (MVRelationalTuple<M>[])list.getAttributes()) {
			IPredictionFunction<MVRelationalTuple<M>, M> pf = predictionFunctions.get(obj.getMetadata().getPredictionFunctionKey());
			pf.predictAll(getOutputSchema(), obj, currentTime);
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
