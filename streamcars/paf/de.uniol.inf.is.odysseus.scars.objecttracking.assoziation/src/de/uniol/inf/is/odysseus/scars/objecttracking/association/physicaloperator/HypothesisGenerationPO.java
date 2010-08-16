package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea.Order;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.PointInTimeSweepArea;
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
public class HypothesisGenerationPO<M extends IProbability & IConnectionContainer & ITimeInterval> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private String oldObjListPath;
	private String newObjListPath;

	private PointInTimeSweepArea<M> sweepPrediction;
	private PointInTimeSweepArea<M> sweepScanned;

	public HypothesisGenerationPO() {
		super();
		sweepPrediction = new PointInTimeSweepArea<M>();
		sweepScanned = new PointInTimeSweepArea<M>();
	}

	public HypothesisGenerationPO(HypothesisGenerationPO<M> copy) {
		super(copy);
		this.oldObjListPath = copy.getOldObjListPath();
		this.newObjListPath = copy.getNewObjListPath();
		this.sweepPrediction = (PointInTimeSweepArea<M>) copy.sweepPrediction.clone();
		this.sweepScanned =  (PointInTimeSweepArea<M>) copy.sweepScanned.clone();
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
	}

	/**
	 * port 0 = old;
	 * port 1 = new;
	 */
	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		if(port == 0) {
			this.sweepPrediction.insert(object);
		} else if(port == 1) {
			this.sweepScanned.insert(object);
		}

		this.sweepPrediction.purgeElements(object, Order.LeftRight);
		this.sweepScanned.purgeElements(object, Order.LeftRight);

		Iterator<MVRelationalTuple<M>> itPred = sweepPrediction.query(object, Order.LeftRight);
		Iterator<MVRelationalTuple<M>> itScan = sweepScanned.query(object, Order.LeftRight);

		if(itPred.hasNext() && itScan.hasNext()) {
			MVRelationalTuple<M> output = createOutputTuple(itScan.next(), itPred.next());
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
