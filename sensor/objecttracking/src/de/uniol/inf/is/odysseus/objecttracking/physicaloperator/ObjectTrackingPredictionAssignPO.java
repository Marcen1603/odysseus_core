package de.uniol.inf.is.odysseus.objecttracking.physicaloperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingPredictionAssignAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

/**
 * This operator sets the prediction functions to stream elements
 * according to some predicates.
 * 
 * @author Andre Bolles
 */
@SuppressWarnings("unchecked")
public class ObjectTrackingPredictionAssignPO<T extends IMetaAttributeContainer<M>, M extends IPredictionFunctionKey<IPredicate>> extends AbstractPipe<T, T>{

	private final Map<IPredicate<? super T>, IPredictionFunction> predictionFunctions;
	
	public ObjectTrackingPredictionAssignPO(ObjectTrackingPredictionAssignAO<T> predictionAO) {
		super();
		this.predictionFunctions = predictionAO.getPredictionFunctions();
	}
	
	private ObjectTrackingPredictionAssignPO(ObjectTrackingPredictionAssignPO<T, M> predictionPO){
		super();
		this.predictionFunctions = new HashMap<IPredicate<? super T>, IPredictionFunction>();
		for(Entry<IPredicate<? super T>, IPredictionFunction> entry : predictionFunctions.entrySet()){
			this.predictionFunctions.put(entry.getKey().clone(), entry.getValue().clone());
		}
	}
	
	@Override
	public ObjectTrackingPredictionAssignPO<T, M> clone(){
		return new ObjectTrackingPredictionAssignPO(this);
	}

	/**
	 * This method only has to set the prediction function. All the other metadata remains.
	 */
	@Override
	protected void process_next(T next, int port) {

		for (Map.Entry<IPredicate<? super T>, IPredictionFunction> curPredictionFunction : this.predictionFunctions
				.entrySet()) {
			IPredicate pred = curPredictionFunction.getKey();
			if (pred.evaluate(next)) {
				// The keys in predictionFunctions and variables are the same, so
				// a mapping between both entries exists.
				next.getMetadata().setPredictionFunctionKey(curPredictionFunction.getKey());				
				transfer(next);
				return;
			}
		}
		
		// the default prediction function has to be used
		next.getMetadata().setPredictionFunctionKey(new TruePredicate());
		
		transfer(next);
		return;
	}
	
	@Override
	public OutputMode getOutputMode(){
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public void process_done(){
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}
}
