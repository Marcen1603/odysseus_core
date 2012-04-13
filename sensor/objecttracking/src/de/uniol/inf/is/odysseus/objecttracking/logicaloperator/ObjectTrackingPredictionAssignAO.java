/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.objecttracking.logicaloperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.LinearProbabilityPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFSchemaExtended;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFSchemaMetadataTypes;
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class ObjectTrackingPredictionAssignAO<T> extends UnaryLogicalOp{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1917550706455197576L;

//	private Map<IPredicate<? super T>, SDFExpression[]> predictionFunctions;
	private Map<IPredicate<? super T>, IPredictionFunction> predictionFunctions;
	
	/**
	 * The default prediction function only has be separated from
	 * the other prediction functions in this operator. In all other
	 * operators, they can be put together into the hashmap. This is,
	 * because only in this operator, the predicates will be evaluated.
	 * In all the other operators, the predicates will be used as keys,
	 * but they will not be evaluated.
	 */
	private IPredictionFunction defaultPredictionFunction;
	
	private SDFSchemaExtended outputSchema;
	
	/** 
	 * The first measurement attribute has not necessarily to be the
	 * first attribute in the schema and so on. So, we have to find the measurement
	 * attribute positions.
	 * 
	 * This array contains the positions of the measurement values.
	 * restrictList[0] contains the position of the first measurement attribute in the
	 * schema, restrictList[1] contains the position of the second measurement attribute
	 * in the schema and so on.
	 */
	private int[] restrictList;
	
	public IPredictionFunction getDefaultPredictionFunction() {
		return defaultPredictionFunction;
	}

	public void setDefaultPredictionFunction(SDFExpression[] defaultPredictionFunction) {
		IPredictionFunction predFct = new LinearProbabilityPredictionFunction();
		predFct.setExpressions(defaultPredictionFunction);
		
		int[][] vars = new int[defaultPredictionFunction.length][];
		for(int u = 0; u<defaultPredictionFunction.length; u++){
			SDFExpression expression = defaultPredictionFunction[u];
			if(expression != null)
				vars[u] = expression.getAttributePositions();
		}
		
		predFct.setVariables(vars);
		this.defaultPredictionFunction = predFct;
	}

	public ObjectTrackingPredictionAssignAO() {
		super();
		this.predictionFunctions = new HashMap<IPredicate<? super T>, IPredictionFunction>();
		this.defaultPredictionFunction = null;
	}
	
	public ObjectTrackingPredictionAssignAO(ObjectTrackingPredictionAssignAO predictionAO) {
		super(predictionAO);
		this.predictionFunctions = new HashMap<IPredicate<? super T>, IPredictionFunction>();
		Set<Entry<IPredicate<? super T>, IPredictionFunction>> entries = predictionAO.predictionFunctions.entrySet();
		for(Map.Entry<IPredicate<? super T>, IPredictionFunction> curEntry: entries){
			this.predictionFunctions.put(curEntry.getKey(), curEntry.getValue());
		}
	}

	public Map<IPredicate<? super T>, IPredictionFunction> getPredictionFunctions() {
		return predictionFunctions;
	}

	// has never been used
	public void setPredictionFunctions(Map<IPredicate<? super T>, IPredictionFunction> predictionFunctions) {
		this.predictionFunctions = predictionFunctions;
	}
	
	public void setPredictionFunction(SDFExpression[] expressions, IPredicate<? super T> predicate) {
		if (this.predictionFunctions.containsKey(predicate)) {
			throw new IllegalArgumentException("predictionFunction already exists: " + expressions);
		}
		
		IPredictionFunction predFct = new LinearProbabilityPredictionFunction();
		predFct.setExpressions(expressions);
		
		int[][] vars = new int[expressions.length][];
		for(int u = 0; u<expressions.length; u++){
			SDFExpression expression = expressions[u];
			if(expression != null)
				vars[u] = expression.getAttributePositions();
		}
		
		predicate.init();
		
		predFct.setVariables(vars);
		this.predictionFunctions.put(predicate, predFct);
	}

	@Override
	public ObjectTrackingPredictionAssignAO clone() {
		return new ObjectTrackingPredictionAssignAO(this);
	}
	
	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		this.outputSchema = new SDFSchemaExtended(this.getInputSchema().getAttributes());
		HashMap<IPredicate<? super T>, IPredictionFunction> outPredFcts = new HashMap<IPredicate<? super T>, IPredictionFunction>();
		for(Entry<IPredicate<? super T>, IPredictionFunction> entry : this.predictionFunctions.entrySet()){
			outPredFcts.put(entry.getKey().clone(), entry.getValue().clone());
		}
		outPredFcts.put(new TruePredicate(), this.defaultPredictionFunction);
		this.outputSchema.setMetadata(SDFSchemaMetadataTypes.PREDICTION_FUNCTIONS, this.predictionFunctions);
		
		return this.outputSchema;
	}
	
}
