package de.uniol.inf.is.odysseus.objecttracking.physicaloperator;

import java.util.Map;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.PredictionAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.IRangePredicate;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * This operator sets the prediction functions to stream elements
 * according to some predicates.
 * 
 * @author Andre Bolles
 */
@SuppressWarnings("unchecked")
public class ObjectTrackingPredictionAssignPO<T extends IMetaAttributeContainer<M>, M extends IPredictionFunctionKey<IPredicate>> extends AbstractPipe<T, T>{

	private final Map<IPredicate<? super T>, IPredictionFunction> predictionFunctions;
	
	public ObjectTrackingPredictionAssignPO(PredictionAO<T> predictionAO) {
		super();
		this.predictionFunctions = predictionAO.getPredictionFunctions();
	}

	/**
	 * This method only has to set the prediction function. All the other metadata remains.
	 */
	@Override
	protected void process_next(T next, int port) {
		
		for (Map.Entry<IPredicate<? super T>, IPredictionFunction> curPredictionFunction : this.predictionFunctions
				.entrySet()) {
			if (curPredictionFunction.getKey().evaluate(next)) {
				// The keys in predictionFunctions and variables are the same, so
				// a mapping between both entries exists.
				next.getMetadata().setPredictionFunctionKey(curPredictionFunction.getKey());
//				next.getMetadata().setVariables(this.variables.get(curPredictionFunction.getKey()));
//				next.getMetadata().setRestrictList(this.restrictList);
//				next.getMetadata().setTimeInterval(new TimeInterval(next.getMetadata().getStart(), next.getMetadata().getEnd()));
				transfer(next);
				return;
			}
		}
		
		// the default prediction function has to be used
		// TODO Umgang mit Default Prediction Function.
//		next.getMetadata().setPredictionFunction(this.defaultPredictionFunction, null);
//		next.getMetadata().setTimeInterval(new TimeInterval(next.getMetadata().getStart(), next.getMetadata().getEnd()));
		
		transfer(next);
		return;
	}
	
	@Override
	public OutputMode getOutputMode(){
		return OutputMode.MODIFIED_INPUT;
	}
}
