package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IObjectTrackingLatency;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.StreamCollector;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;

/**
 * <p>
 * Physical operator to join two inputstreams:
 * <ul>
 *  <li>the predicted objects from the <strong>prediction operator</strong> ({@link de.uniol.inf.is.odysseus.scars.objecttracking.prediction.physicaloperator.PredictionPO}) and</li>
 *  <li>the new detected objects.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * At this point both list should have the <strong>same timestamp</strong>. This operator initiates the connection list
 * in the metadata and changes the schema so that the next operator gets both lists in one stream as input.
 * </p>
 *
 * @author Volker Janz
 */
public class HypothesisGenerationPO<M extends IProbability & IConnectionContainer & ITimeInterval & IObjectTrackingLatency> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private String sourcePredictedObjListPath;
	private String sourceScannedObjListPath;
	
	private String outputPredictedObjListPath;
	private String outputScannedObjListPath;
	
	StreamCollector streamCollector;

	private SchemaHelper helper;
	private SchemaHelper helper2;
	private SchemaIndexPath timePathFromScannedData;
	private SchemaIndexPath carsFromscannedData;
	private SchemaIndexPath carsFromPredictedData;

	public HypothesisGenerationPO() {
		super();
	}

	public HypothesisGenerationPO(HypothesisGenerationPO<M> copy) {
		super(copy);
		this.sourcePredictedObjListPath = copy.getOldObjListPath();
		this.sourceScannedObjListPath = copy.getNewObjListPath();
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		streamCollector = new StreamCollector(getSubscribedToSource().size());
		helper = new SchemaHelper(getSubscribedToSource(0).getSchema());
		helper2 = new SchemaHelper(getSubscribedToSource(1).getSchema());

		timePathFromScannedData = helper.getSchemaIndexPath(helper.getStartTimestampFullAttributeName());
		carsFromscannedData = helper.getSchemaIndexPath(this.sourceScannedObjListPath);
		carsFromPredictedData = helper2.getSchemaIndexPath(this.sourcePredictedObjListPath);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		if( port == 0 ) // von dort sollte keine Punctuation kommen...
			throw new IllegalArgumentException("HypothesisGenerationPO recieved Punctuation on Port 0");

		// Punctuation aus Port 1 heißt, dass die Prediction nix hat
		streamCollector.recieve(timestamp, port);
		if( streamCollector.isReady() )
			send( streamCollector.getNext());
	}

	@SuppressWarnings("unchecked")
	private void send(List<Object> next) {
		Object obj0 = next.get(0); // Port 0
		Object obj1 = next.get(1); // Port 1

		// An Port 0 haben wir immer einen Tupel aus der Quelle/Selection
		MVRelationalTuple<M> scannedTuple = (MVRelationalTuple<M>)obj0;
		scannedTuple.getMetadata().setObjectTrackingLatencyStart();
		scannedTuple.getMetadata().setObjectTrackingLatencyStart("Association Generation");

		// Und an Port1 aus Prediction?
		if( obj1 instanceof MVRelationalTuple) {
			// Ein Tupel!
			MVRelationalTuple<M> predictedTuple = (MVRelationalTuple<M>)obj1;

			// Normale Function ausführen... wir haben Daten zum Senden
			MVRelationalTuple<M> output = createOutputTuple(scannedTuple, predictedTuple);
			output.getMetadata().setObjectTrackingLatencyEnd("Association Generation");
			output.getMetadata().setObjectTrackingLatencyEnd();
			transfer(output);

		} else {
			// Eine Punctuation! Prediction hat nix
			MVRelationalTuple<M> output = createOutputTuple(scannedTuple, null);
			output.getMetadata().setObjectTrackingLatencyEnd("Association Generation");
			output.getMetadata().setObjectTrackingLatencyEnd();
			transfer(output);
		}
	}

	/**
	 * port 0 = new;
	 * port 1 = old;
	 */
	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		synchronized (this) {
			streamCollector.recieve(object, port);
			if( streamCollector.isReady()) {
				send(streamCollector.getNext());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private MVRelationalTuple<M> createOutputTuple(MVRelationalTuple<M> scannedObject, MVRelationalTuple<M> predictedObject) {
		Object[] association = new Object[3];

		// get timestamp path from scanned data
		association[0] = timePathFromScannedData.toTupleIndexPath(scannedObject).getTupleObject();

		// get scanned objects
		TupleIndexPath path = carsFromscannedData.toTupleIndexPath((MVRelationalTuple<M>) scannedObject);
		replaceMetaDataNames(path, this.sourceScannedObjListPath, this.outputScannedObjListPath);
		association[1] = new MVRelationalTuple<M>((MVRelationalTuple<M>) path.getTupleObject());
		

		// get predicted objects
		if(predictedObject == null) {
			association[2] = new MVRelationalTuple<M>(0);
		} else {
			path = carsFromPredictedData.toTupleIndexPath(predictedObject);
			replaceMetaDataNames(path, this.sourceScannedObjListPath, this.outputPredictedObjListPath);
			association[2] =  new MVRelationalTuple<M>((MVRelationalTuple<M>) path.getTupleObject());
		}

		MVRelationalTuple<M> base = new MVRelationalTuple<M>(1);
		base.setMetadata((M)scannedObject.getMetadata().clone());
		if(predictedObject != null) {
			base.getMetadata().getOperatorLatencies().put("Prediction Assign", predictedObject.getMetadata().getOperatorLatencies().get("Prediction Assign"));
			base.getMetadata().getOperatorLatencies().put("Prediction", predictedObject.getMetadata().getOperatorLatencies().get("Prediction"));
		}
		base.setAttribute(0, new MVRelationalTuple<M>(association));

		return base;
	}

	private void replaceMetaDataNames(TupleIndexPath tupleIndexPath, String sourceName, String outputName) {
		for (TupleInfo car : tupleIndexPath) {
			@SuppressWarnings("unchecked")
			MVRelationalTuple<M> carObject = (MVRelationalTuple<M>) car.tupleObject;
			List<String> newAttributeMapping = new ArrayList<String>(carObject.getMetadata().getAttributMapping());
			
			for (int i = 0; i < newAttributeMapping.size(); i++) {
				newAttributeMapping.set(i, newAttributeMapping.get(i).replace(sourceName, outputName));
			}
			carObject.getMetadata().setAttributeMapping(newAttributeMapping);
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new HypothesisGenerationPO<M>(this);
	}

	public String getOldObjListPath() {
		return this.sourcePredictedObjListPath;
	}

	public String getNewObjListPath() {
		return this.sourceScannedObjListPath;
	}

	public void setSourcePredictedObjListPath(String oldObjListPath) {
		this.sourcePredictedObjListPath = oldObjListPath;
	}

	public void setSourceScannedObjListPath(String newObjListPath) {
		this.sourceScannedObjListPath = newObjListPath;
	}

	public void setOutputPredictedObjListPath(String outputPredictedObjListPath) {
		this.outputPredictedObjListPath = outputPredictedObjListPath;
	}
	
	public void setOutputScannedObjListPath(String outputScannedObjListPath) {
		this.outputScannedObjListPath = outputScannedObjListPath;
	}
}
