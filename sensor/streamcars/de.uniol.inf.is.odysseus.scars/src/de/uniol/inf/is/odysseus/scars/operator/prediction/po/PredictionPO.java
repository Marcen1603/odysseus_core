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

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleHelper;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.scars.metadata.PredictionFunctionContainer;
import de.uniol.inf.is.odysseus.scars.util.helper.PortSync;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SchemaHelper;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SchemaIndexPath;

public class PredictionPO<M extends ITimeIntervalProbabilityObjectTrackingLatencyPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>>> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {
	
	private int[] objListPath;
	private SchemaIndexPath currentTimeSchemaPath;
	private SchemaIndexPath currentScanTimeSchemaPath;

	private PredictionFunctionContainer<M> predictionFunctions;

	private PortSync streamCollector;

	public PredictionPO() {
	}

	public PredictionPO(PredictionPO<M> copy) {
		super(copy);
		this.predictionFunctions = new PredictionFunctionContainer<M>(copy.predictionFunctions);
		objListPath = new int[copy.objListPath.length];
		System.arraycopy(copy.objListPath, 0, this.objListPath, 0, copy.objListPath.length);
		this.currentTimeSchemaPath = copy.currentTimeSchemaPath;
		this.currentScanTimeSchemaPath = copy.currentScanTimeSchemaPath;
	}

	public void setPredictionFunctions(PredictionFunctionContainer<M> predictionFunctions) {
		this.predictionFunctions = predictionFunctions;
	}

	public void setObjectListPath(int[] objListPath) {
		this.objListPath = objListPath;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		SDFAttributeList sourceTimeSchema = this.getSubscribedToSource(0).getTarget().getOutputSchema();
		SDFAttributeList scanSchema = this.getSubscribedToSource(1).getTarget().getOutputSchema();
		SchemaHelper helper1 = new SchemaHelper(sourceTimeSchema);
		currentTimeSchemaPath = helper1.getSchemaIndexPath(helper1.getStartTimestampFullAttributeName());
		SchemaHelper helper2 = new SchemaHelper(scanSchema);
		currentScanTimeSchemaPath = helper2.getSchemaIndexPath(helper2.getStartTimestampFullAttributeName());

		streamCollector = new PortSync(getSubscribedToSource().size());
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		synchronized (this) {
			streamCollector.recieve(object, port);
			if( streamCollector.isReady() ) {
				send(streamCollector.getNext());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void send( List<Object> objects ) {
		Object obj0 = objects.get(0); // Port 0 - Timetupel/Punctuation
		Object obj1 = objects.get(1); // Port 1 - Brokertupel/Punctuation

		// An Port 0 sind nur Tupel.. (s. processPunctuation())
		// trotzdem Checken
		if( obj0 instanceof MVRelationalTuple) {
			// Port 0 hat Tupel
			MVRelationalTuple<M> currentTimeTuple = ((MVRelationalTuple<M>)obj0).clone();
			if( obj1 instanceof MVRelationalTuple ) {
				// Port 1 hat Tupel
				MVRelationalTuple<M> currentScanTuple = ((MVRelationalTuple<M>)obj1).clone();

				currentScanTuple.getMetadata().setObjectTrackingLatencyStart();
				currentScanTuple.getMetadata().setObjectTrackingLatencyStart("Prediction");

				MVRelationalTuple<M> predictedTuple = predictData(currentTimeTuple, currentScanTuple);
				predictedTuple.getMetadata().setObjectTrackingLatencyEnd();
				predictedTuple.getMetadata().setObjectTrackingLatencyEnd("Prediction");
				transfer( predictedTuple );

			} else {
				// Port 1 hat Punctuation
				// --> Broker hat (noch) nix
				// --> wir haben auch (noch) nix
				PointInTime pt = new PointInTime(currentTimeTuple.getMetadata().getStart().getMainPoint());
				sendPunctuation(pt);
			}
		} else {
			// theoretisch sollten wir hier nie landen...
			throw new IllegalArgumentException("Prediction has Punctuation on port 0");
		}
	}

	// @SuppressWarnings("unchecked")
	@SuppressWarnings("unchecked")
	private MVRelationalTuple<M> predictData(MVRelationalTuple<M> currentTimeTuple, MVRelationalTuple<M> currentScanTuple) {

		if (currentScanTuple != null) {
			TupleHelper helper = new TupleHelper(currentScanTuple);
			List<Object> list = (List<Object>) helper.getObject(objListPath);
			for (int index = 0; index < list.size(); index++) {
				MVRelationalTuple<M> obj = (MVRelationalTuple<M>)list.get(index);
				IPredictionFunction<M> pf = predictionFunctions.get(obj.getMetadata().getPredictionFunctionKey());
				if (pf != null) {
					pf.predictData(currentScanTuple, currentTimeTuple, index);
					M metadata = obj.getMetadata();
					pf.predictMetadata(metadata, currentScanTuple, currentTimeTuple, index);
				} else {
					System.err.println("No PredictionFunction assigned (NO DEFAULT PREDICTION_FUNCTION)");
				}

			}
			TupleIndexPath scanTimeTPath = TupleIndexPath.fromSchemaIndexPath(currentTimeSchemaPath, currentTimeTuple);
			Long currentTimeValue = (Long) scanTimeTPath.getTupleObject();

			//currentScanTuple.getMetadata().getStart().setMainPoint(currentTimeValue);
			currentScanTuple.getMetadata().setStart(new PointInTime(currentTimeValue));
			TupleIndexPath currentScanTimeTPath = TupleIndexPath.fromSchemaIndexPath(currentScanTimeSchemaPath, currentScanTuple);
			currentScanTimeTPath.setTupleObject(currentTimeValue);
		} else {
			System.err.println("Prediction: currentScanTuple was null!!");
		}
		return currentScanTuple;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		synchronized (this) {
			if( port == 0) // Wir ignorieren Punctuations von der Zeitseite aus. Die Tupel selbst geben uns die Zeit.
				return;

			streamCollector.recieve(timestamp, port);
			if( streamCollector.isReady())
				send(streamCollector.getNext());
		}
	}

	@Override
	public PredictionPO<M> clone() {
		return new PredictionPO<M>(this);
	}
}
