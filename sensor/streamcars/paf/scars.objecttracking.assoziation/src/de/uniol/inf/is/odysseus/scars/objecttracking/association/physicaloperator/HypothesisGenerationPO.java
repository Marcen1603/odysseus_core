package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.StreamCollector;

/**
 * The Hypothesis Generation has two inputstreams:
 * 1 - predicted objects from the prediction operator
 * 2 - the new detected objects
 * Both list now have the same timestamp. The Hypothesis Generation Operator initiates the connection list
 * in the metadata and changes the schema so that the next operator gets both lists (new and old) as input.
 *
 * @author Volker Janz
 *
 */
public class HypothesisGenerationPO<M extends IProbability & IConnectionContainer & ITimeInterval> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private String oldObjListPath;
	private String newObjListPath;
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
		this.oldObjListPath = copy.getOldObjListPath();
		this.newObjListPath = copy.getNewObjListPath();
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		streamCollector = new StreamCollector(getSubscribedToSource().size());
		helper = new SchemaHelper(getSubscribedToSource(0).getSchema());
		helper2 = new SchemaHelper(getSubscribedToSource(1).getSchema());
		
		timePathFromScannedData = helper.getSchemaIndexPath(helper.getStartTimestampFullAttributeName());
		carsFromscannedData = helper.getSchemaIndexPath(this.newObjListPath);
		carsFromPredictedData = helper2.getSchemaIndexPath(this.oldObjListPath);
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
		
		// Und an Port1 aus Prediction?
		if( obj1 instanceof MVRelationalTuple) {
			// Ein Tupel!
			MVRelationalTuple<M> predictedTuple = (MVRelationalTuple<M>)obj1;
			
			// Normale Function ausführen... wir haben Daten zum Senden
			MVRelationalTuple<M> output = createOutputTuple(scannedTuple, predictedTuple);
			transfer(output);
			
		} else {
			// Eine Punctuation! Prediction hat nix
			MVRelationalTuple<M> output = createOutputTuple(scannedTuple, null);
			transfer(output);
		}
	}

	/**
	 * port 0 = new;
	 * port 1 = old;
	 */
	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		streamCollector.recieve(object, port);
		if( streamCollector.isReady())
			send(streamCollector.getNext());
	}

	private MVRelationalTuple<M> createOutputTuple(MVRelationalTuple<M> scannedObject, MVRelationalTuple<M> predictedObject) {
		Object[] association = new Object[3];

		// get timestamp path from scanned data
		association[0] = timePathFromScannedData.toTupleIndexPath(scannedObject).getTupleObject();

		// get scanned objects
		association[1] = carsFromscannedData.toTupleIndexPath(scannedObject).getTupleObject();

		// get predicted objects
		if(predictedObject == null) {
			association[2] = new MVRelationalTuple<M>(0);
		} else {
			association[2] =  carsFromPredictedData.toTupleIndexPath(predictedObject).getTupleObject();
		}

		MVRelationalTuple<M> base = new MVRelationalTuple<M>(1);
		base.setMetadata(scannedObject.getMetadata());
		base.setAttribute(0, new MVRelationalTuple<M>(association));

		return base;
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
		return this.oldObjListPath;
	}

	public String getNewObjListPath() {
		return this.newObjListPath;
	}

	public void setOldObjListPath(String oldObjListPath) {
		this.oldObjListPath = oldObjListPath;
	}

	public void setNewObjListPath(String newObjListPath) {
		this.newObjListPath = newObjListPath;
	}
}
