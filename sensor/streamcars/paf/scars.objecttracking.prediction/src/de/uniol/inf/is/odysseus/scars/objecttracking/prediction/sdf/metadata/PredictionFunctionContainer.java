package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.PredictionExpression;




/**
 * This class deals as container for the prediction functions, needed for the prediction and filtering operators. 
 * Typically it is passed to the operators as metadata of the SDFSchema, in special to the SDFAttributeListExtended, 
 * which has has Key-Value-Pairs, and so for cna be requested with the enum key SDFAttributeListMetadataTypes.PREDICTION_FUNCTIONS.
 * 
 * It holds a Mapping between some predicates (for example to define the right type of object) and the corresponding prediction function.
 * Also it has a special relationship for one default prediction function, which can be set and treated differently from the normal mapping, 
 * by passing in a default predicate (e.g. the TruePredicate)
 * 
 * Additionally it deals as iterator over the predicate keys for easy to use for-loops.
 * 
 * Note: for now only LinearProbabilityPredictionFunction instances are used here.
 * 
 * @author Benjamin G
 * @version 0.0.0.1
 *
 * @param <M>
 */
public class PredictionFunctionContainer<M extends IProbability> implements Iterable<IPredicate<MVRelationalTuple<M>>>{
	
	private Map<IPredicate<MVRelationalTuple<M>>, IPredictionFunction<M>> predictionFunctions;
	private IPredicate<MVRelationalTuple<M>> defaultPredictionFunctionKey;
	private IPredictionFunction<M> defaultPredictionFunction;
	
	
	public PredictionFunctionContainer() {
		predictionFunctions = new HashMap<IPredicate<MVRelationalTuple<M>>, IPredictionFunction<M>>();
		defaultPredictionFunctionKey = new TruePredicate<MVRelationalTuple<M>>();
		defaultPredictionFunctionKey.init();
		defaultPredictionFunction = null;
	}
	
	public PredictionFunctionContainer(PredictionFunctionContainer<M> container) {
		predictionFunctions = new HashMap<IPredicate<MVRelationalTuple<M>>, IPredictionFunction<M>>(container.getMap());
		defaultPredictionFunctionKey = container.getDefaultPredictionFunctionKey();
		defaultPredictionFunction = container.getDefaultPredictionFunction();
	}
	
	/**
	 * Returns the associated prediction function from that predicate as key.
	 * Note: also the default prediction function key can be handled this way.
	 * @param key
	 * @return
	 */
	public IPredictionFunction<M> get(IPredicate<MVRelationalTuple<M>> key) {
		if(key.equals(defaultPredictionFunctionKey)) {
			return defaultPredictionFunction;
		}
		return predictionFunctions.get(key);
	}
	
	/**
	 * Sets the default prediction function. Passing in some SDFExpressions which then are used to construct the prediction function.
	 * @param defaultPredictionFunction
	 */
	public void setDefaultPredictionFunction(PredictionExpression[] defaultPredictionFunction) {
		IPredictionFunction<M> predFct = new LinearPredictionFunction<M>();
		predFct.setExpressions(defaultPredictionFunction);
		this.defaultPredictionFunction = predFct;
	}
	
	public IPredictionFunction<M> getDefaultPredictionFunction() {
		return defaultPredictionFunction;
	}
	
	public IPredicate<MVRelationalTuple<M>> getDefaultPredictionFunctionKey() {
		return defaultPredictionFunctionKey;
	}
	
	/**
	 * Returns the life map upon the predicates and associated prediction functions. 
	 * (so every change made on this map are reflected in this container.)
	 * Note: the default prediction function and key are treated differently, and are not contained in this map.
	 * @return 
	 */
	public Map<IPredicate<MVRelationalTuple<M>>, IPredictionFunction<M>> getMap() {
		return predictionFunctions;
	}
	
	/**
	 * Puts one new prediction function into the map. Passing in some SDFExpressions which then are used to construct the prediction function 
	 * and the associated predicate used as key.
	 * @param expressions
	 * @param predicate
	 */
	public void setPredictionFunction(PredictionExpression[] expressions, IPredicate<MVRelationalTuple<M>> predicate) {
		if (this.predictionFunctions.containsKey(predicate)) {
			throw new IllegalArgumentException("predictionFunction already exists: " + expressions);
		}
		
		IPredictionFunction<M> predFct = new LinearPredictionFunction<M>();
		predFct.setExpressions(expressions);
		predicate.init();
		this.predictionFunctions.put(predicate, predFct);
	}
	
	@Override
	public Iterator<IPredicate<MVRelationalTuple<M>>> iterator() {
		return predictionFunctions.keySet().iterator();
	}
	
	public PredictionFunctionContainer<M> clone() {
		return new PredictionFunctionContainer<M>(this);
	}
}
