/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.scars.metadata;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;




/**
 * This class deals as container for the prediction functions, needed for the prediction and filtering operators. 
 * Typically it is passed to the operators as metadata of the SDFSchema, in special to the SDFSchemaExtended, 
 * which has has Key-Value-Pairs, and so for cna be requested with the enum key SDFSchemaMetadataTypes.PREDICTION_FUNCTIONS.
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
public class PredictionFunctionContainer<M extends IProbability> implements Iterable<IPredicate<MVTuple<M>>>{
	
	private Map<IPredicate<MVTuple<M>>, IPredictionFunction<M>> predictionFunctions;
	private IPredicate<MVTuple<M>> defaultPredictionFunctionKey;
	private IPredictionFunction<M> defaultPredictionFunction;
	
	
	public PredictionFunctionContainer() {
		predictionFunctions = new HashMap<IPredicate<MVTuple<M>>, IPredictionFunction<M>>();
		defaultPredictionFunctionKey = new TruePredicate<MVTuple<M>>();
		defaultPredictionFunctionKey.init();
		defaultPredictionFunction = null;
	}
	
	public PredictionFunctionContainer(PredictionFunctionContainer<M> container) {
		predictionFunctions = new HashMap<IPredicate<MVTuple<M>>, IPredictionFunction<M>>(container.getMap());
		defaultPredictionFunctionKey = container.getDefaultPredictionFunctionKey();
		defaultPredictionFunction = container.getDefaultPredictionFunction();
	}
	
	/**
	 * Returns the associated prediction function from that predicate as key.
	 * Note: also the default prediction function key can be handled this way.
	 * @param key
	 * @return
	 */
	public IPredictionFunction<M> get(IPredicate<MVTuple<M>> key) {
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
	
	public IPredicate<MVTuple<M>> getDefaultPredictionFunctionKey() {
		return defaultPredictionFunctionKey;
	}
	
	/**
	 * Returns the life map upon the predicates and associated prediction functions. 
	 * (so every change made on this map are reflected in this container.)
	 * Note: the default prediction function and key are treated differently, and are not contained in this map.
	 * @return 
	 */
	public Map<IPredicate<MVTuple<M>>, IPredictionFunction<M>> getMap() {
		return predictionFunctions;
	}
	
	/**
	 * Puts one new prediction function into the map. Passing in some SDFExpressions which then are used to construct the prediction function 
	 * and the associated predicate used as key.
	 * @param expressions
	 * @param predicate
	 */
	public void setPredictionFunction(PredictionExpression[] expressions, IPredicate<MVTuple<M>> predicate) {
		if (this.predictionFunctions.containsKey(predicate)) {
			throw new IllegalArgumentException("predictionFunction already exists: " + expressions);
		}
		
		IPredictionFunction<M> predFct = new LinearPredictionFunction<M>();
		predFct.setExpressions(expressions);
		predicate.init();
		this.predictionFunctions.put(predicate, predFct);
	}
	
	@Override
	public Iterator<IPredicate<MVTuple<M>>> iterator() {
		return predictionFunctions.keySet().iterator();
	}
	
	@Override
	public PredictionFunctionContainer<M> clone() {
		return new PredictionFunctionContainer<M>(this);
	}
}
