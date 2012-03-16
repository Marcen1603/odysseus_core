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

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.objecttracking.MVTuple;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleHelper;
import de.uniol.inf.is.odysseus.scars.metadata.PredictionFunctionContainer;

public class PredictionAssignPO<M extends ITimeIntervalProbabilityObjectTrackingLatencyPredictionFunctionKey<IPredicate<MVTuple<M>>>> extends AbstractPipe<MVTuple<M>, MVTuple<M>> {

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
	protected void process_next(MVTuple<M> object, int port) {
		object.getMetadata().setObjectTrackingLatencyStart();
		object.getMetadata().setObjectTrackingLatencyStart("Prediction Assign");
		TupleHelper helper = new TupleHelper(object);
		Object listObj = helper.getObject(pathToList);

		if(listObj instanceof List) {
			Object[] objList = ((List<Object>) listObj).toArray();
			if( objList.length == 0 ) {
				sendPunctuation(object.getMetadata().getStart().clone());
				return;
			}
			for(Object mvObj : objList ) {
				evaluatePredicateKey((MVTuple<M>)mvObj);
			}
		}
		object.getMetadata().setObjectTrackingLatencyEnd("Prediction Assign");
		object.getMetadata().setObjectTrackingLatencyEnd();
		transfer(object);
//		return;

	}

	private void evaluatePredicateKey(MVTuple<M> tuple) {
		for(IPredicate<MVTuple<M>> pred : predictionFunctions) {
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
