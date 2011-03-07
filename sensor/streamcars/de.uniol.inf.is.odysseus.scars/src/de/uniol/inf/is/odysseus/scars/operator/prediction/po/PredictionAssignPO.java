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
package de.uniol.inf.is.odysseus.scars.operator.prediction.po;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.scars.metadata.IObjectTrackingLatency;
import de.uniol.inf.is.odysseus.scars.metadata.PredictionFunctionContainer;
import de.uniol.inf.is.odysseus.scars.util.helper.TupleHelper;

public class PredictionAssignPO<M extends ITimeInterval & IProbability & IObjectTrackingLatency & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>>> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

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
		this.pathToList = new int[copy.pathToList.length];
		System.arraycopy(copy.pathToList, 0, this.pathToList, 0, copy.pathToList.length);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		object.getMetadata().setObjectTrackingLatencyStart();
		object.getMetadata().setObjectTrackingLatencyStart("Prediction Assign");
		TupleHelper helper = new TupleHelper(object);
		Object listObj = helper.getObject(pathToList);

		if(listObj instanceof MVRelationalTuple<?>) {
			Object[] objList = ((MVRelationalTuple<?>) listObj).getAttributes();
			if( objList.length == 0 ) {
				sendPunctuation(object.getMetadata().getStart().clone());
				return;
			}
			for(Object mvObj : objList ) {
				evaluatePredicateKey((MVRelationalTuple<M>)mvObj);
			}
		}
		object.getMetadata().setObjectTrackingLatencyEnd("Prediction Assign");
		object.getMetadata().setObjectTrackingLatencyEnd();
		transfer(object);
//		return;

	}

	private void evaluatePredicateKey(MVRelationalTuple<M> tuple) {
		for(IPredicate<MVRelationalTuple<M>> pred : predictionFunctions) {
			if(pred.evaluate(tuple)) {
				tuple.getMetadata().setPredictionFunctionKey(pred);
				return;
			}
		}
		tuple.getMetadata().setPredictionFunctionKey(predictionFunctions.getDefaultPredictionFunctionKey());
//		System.err.println("No Predicate can be assigned and evaluated to the current tuple (DEFAULT_PREDICTION_FUNCTION)");
		return;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
	}

	@Override
	public PredictionAssignPO<M> clone() {
		return new PredictionAssignPO<M>(this);
	}

}
