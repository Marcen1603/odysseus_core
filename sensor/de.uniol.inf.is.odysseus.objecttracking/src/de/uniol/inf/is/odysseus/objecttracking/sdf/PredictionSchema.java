package de.uniol.inf.is.odysseus.objecttracking.sdf;

import java.util.Map;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;

/**
 * This class contains information about the prediction
 * functions, that can be used in the serveral operators.
 * Besides information about the predicates in use also
 * the default prediction function is contained in this object.
 * @author abolles
 *
 */
public class PredictionSchema<T> {

	
	Map<IPredicate<? super T>, IPredictionFunction> predictionFunctions;
	IPredictionFunction defaultPredictionFunction;
	
	public PredictionSchema(){	
	}
	
	public PredictionSchema(Map<IPredicate<? super T>, IPredictionFunction> predictionFunctions, IPredictionFunction defaultPredictionFunction){
		this.predictionFunctions = predictionFunctions;
		this.defaultPredictionFunction = defaultPredictionFunction;
	}
	
	public Map<IPredicate<? super T>, IPredictionFunction> getPredictionFunctions(){
		return this.predictionFunctions;
	}
	
	public IPredictionFunction getDefaultPredictionFunction(){
		return this.defaultPredictionFunction;
	}
	
	public void setPredictionFunctions(Map<IPredicate<? super T>, IPredictionFunction> predictionFunctions){
		this.predictionFunctions = predictionFunctions;
	}
	
	public void setDefaultPredicitonFunction(IPredictionFunction defaultPredictionFunction){
		this.defaultPredictionFunction = defaultPredictionFunction;
	}
}
