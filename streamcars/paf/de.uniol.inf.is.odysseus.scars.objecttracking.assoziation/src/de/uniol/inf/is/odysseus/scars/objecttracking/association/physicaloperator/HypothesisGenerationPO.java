package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;

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
public class HypothesisGenerationPO<M extends IProbability & IConnectionContainer> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private MVRelationalTuple<M> predictedObject;
	private MVRelationalTuple<M> scannedObject;

	private String oldObjListPath;
	private String newObjListPath;

	public HypothesisGenerationPO() {
	}

	public HypothesisGenerationPO(HypothesisGenerationPO<M> copy) {
		super(copy);
		this.predictedObject = copy.predictedObject;
		this.scannedObject = copy.scannedObject;
		this.oldObjListPath = copy.getOldObjListPath();
		this.newObjListPath = copy.getNewObjListPath();
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
	}

	/**
	 * port 0 = old;
	 * port 1 = new;
	 */
	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		if(this.predictedObject == null || this.scannedObject == null) {
			switch(port) {
				/*
				 *  WICHTIG: Im sekund�ren Zyklus muss das, was aus dem prim�ren Zyklus
				 *  aus der HypothesisSelection in diesen Operator kommt auf Port 1
				 *  ankommen!!!!!!!!!
				 *
				 */
				case 0: this.predictedObject = object;
				case 1: this.scannedObject = object;
			}
		} else {
			MVRelationalTuple<M> output = createOutputTuple(this.scannedObject, this.predictedObject);
			this.predictedObject = null;
			this.scannedObject = null;
			transfer(output);
		}
	}
	
	private MVRelationalTuple<M> createOutputTuple(MVRelationalTuple<M> scannedObject, MVRelationalTuple<M> predictedObject) {
		SchemaHelper helper = new SchemaHelper(getSubscribedToSource(0).getSchema());
		
		Object[] association = new Object[3];
		
		// get timestamp path from scanned data
		SchemaIndexPath path = helper.getSchemaIndexPath(helper.getStartTimestampAttribute());
		association[0] = path.toTupleIndexPath(scannedObject).getTupleObject();
		
		// get scanned objects
		path = helper.getSchemaIndexPath(this.newObjListPath);
		association[1] = path.toTupleIndexPath(scannedObject).getTupleObject();
		
		// get predicted objects
		path = helper.getSchemaIndexPath(this.oldObjListPath);
		association[2] =  path.toTupleIndexPath(predictedObject).getTupleObject();
		
		return new MVRelationalTuple<M>(association);
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

	public void testProcessNext(MVRelationalTuple<M> object, int port) {
		process_next(object, port);
	}

}
