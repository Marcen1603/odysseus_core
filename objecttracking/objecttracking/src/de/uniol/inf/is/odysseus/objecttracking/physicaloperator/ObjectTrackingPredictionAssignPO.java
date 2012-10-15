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
package de.uniol.inf.is.odysseus.objecttracking.physicaloperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingPredictionAssignAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;

/**
 * This operator sets the prediction functions to stream elements
 * according to some predicates.
 * 
 * @author Andre Bolles
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class ObjectTrackingPredictionAssignPO<T extends IStreamObject<M>, M extends IPredictionFunctionKey<IPredicate>> extends AbstractPipe<T, T>{

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
