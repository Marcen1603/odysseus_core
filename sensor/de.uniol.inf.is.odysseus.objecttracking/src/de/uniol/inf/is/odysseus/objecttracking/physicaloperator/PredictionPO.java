package de.uniol.inf.is.odysseus.objecttracking.physicaloperator;

import java.util.Map;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.PredictionAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * This operator sets the prediction functions to stream elements
 * according to some predicates.
 * 
 * @author Andre Bolles
 */
public class PredictionPO<T extends IMetaAttributeContainer<M>, M extends IPredictionFunction & ITimeInterval> extends AbstractPipe<T, T>{

	private final Map<SDFExpression[], IPredicate<? super T>> predictionFunctions;
	private final SDFExpression[] defaultPredictionFunction;
	
	public PredictionPO(PredictionAO<T> predictionAO) {
		super();
		this.predictionFunctions = predictionAO.getPredictionFunctions();
		this.defaultPredictionFunction = predictionAO.getDefaultPredictionFunction();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T next, int port) {
		
		for (Map.Entry<SDFExpression[], IPredicate<? super T>> curPredictionFunction : this.predictionFunctions
				.entrySet()) {
			if (curPredictionFunction.getValue().evaluate(next)) {
				next.getMetadata().setPredictionFunction(curPredictionFunction.getKey());
				next.getMetadata().setTimeInterval(new TimeInterval(next.getMetadata().getStart(), next.getMetadata().getEnd()));
				next.getMetadata().initVariables();
				transfer(next);
				return;
			}
		}
		
		// the default prediction function has to be used
		next.getMetadata().setPredictionFunction(this.defaultPredictionFunction);
		next.getMetadata().setTimeInterval(new TimeInterval(next.getMetadata().getStart(), next.getMetadata().getEnd()));
		
		transfer(next);
		return;
	}
	
	public OutputMode getOutputMode(){
		return OutputMode.MODIFIED_INPUT;
	}
}
