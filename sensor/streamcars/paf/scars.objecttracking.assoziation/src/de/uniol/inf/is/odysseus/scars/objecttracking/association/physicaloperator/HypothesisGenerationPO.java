package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import java.util.ArrayList;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea.Order;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
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
	
	private long timestamp = -1;

	private PointInTimeSweepArea<M> sweepPrediction;
	private PointInTimeSweepArea<M> sweepScanned;
	
	private PointInTimeSweepArea<M> sweepPunctuation;
	
	public HypothesisGenerationPO() {
		super();
		sweepPrediction = new PointInTimeSweepArea<M>();
		sweepScanned = new PointInTimeSweepArea<M>();
		sweepPunctuation = new PointInTimeSweepArea<M>();
	}

	public HypothesisGenerationPO(HypothesisGenerationPO<M> copy) {
		super(copy);
		this.oldObjListPath = copy.getOldObjListPath();
		this.newObjListPath = copy.getNewObjListPath();
		this.sweepPrediction = (PointInTimeSweepArea<M>) copy.sweepPrediction.clone();
		this.sweepScanned =  (PointInTimeSweepArea<M>) copy.sweepScanned.clone();
		this.sweepPunctuation =  (PointInTimeSweepArea<M>) copy.sweepPunctuation.clone();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		if(port == 0 && timestamp.getMainPoint() > this.timestamp) {
			this.timestamp = timestamp.getMainPoint();
			MVRelationalTuple<M> tuple = new MVRelationalTuple<M>(0);
			StreamCarsMetaData<M> metaData = new StreamCarsMetaData<M>();
			metaData.setStart(timestamp);
			tuple.setMetadata((M)metaData);
			this.sweepPunctuation.insert(tuple);
		}
	}

	/**
	 * port 0 = old;
	 * port 1 = new;
	 */
	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		if(port == 0) {
			this.sweepPrediction.insert(object);
			this.sweepScanned.purgeElements(object, Order.LeftRight);
		} else if(port == 1) {
			this.sweepScanned.insert(object);
			this.sweepPrediction.purgeElements(object, Order.LeftRight);
		}

		Iterator<MVRelationalTuple<M>> itPred = sweepPrediction.query(object, Order.LeftRight);
		Iterator<MVRelationalTuple<M>> itScan = sweepScanned.query(object, Order.LeftRight);

		if(itPred.hasNext() && itScan.hasNext()) {
			MVRelationalTuple<M> scan = itScan.next();
			MVRelationalTuple<M> pred = itPred.next();
			MVRelationalTuple<M> output = createOutputTuple(scan, pred);
			sweepScanned.remove(scan);
			sweepPrediction.remove(pred);
			transfer(output);
			PointInTime time = output.getMetadata().getStart();
			Iterator<MVRelationalTuple<M>> punctuations = sweepPunctuation.punctuationQuery(output);
			PointInTime punctuation = null; 
			long maxPunctuation = 0;
			while (punctuations.hasNext()) {
				MVRelationalTuple<M> mvRelationalTuple = (MVRelationalTuple<M>) punctuations
						.next();
				PointInTime pt = mvRelationalTuple.getMetadata().getStart();
				if(pt.getMainPoint() > maxPunctuation) {
					maxPunctuation = pt.getMainPoint();
					punctuation = pt;
				}
			}
			if(punctuation != null) {
				sendPunctuation(punctuation);
			}
		}
	}

	private MVRelationalTuple<M> createOutputTuple(MVRelationalTuple<M> scannedObject, MVRelationalTuple<M> predictedObject) {
		SchemaHelper helper = new SchemaHelper(getSubscribedToSource(0).getSchema());
		SchemaHelper helper2 = new SchemaHelper(getSubscribedToSource(1).getSchema());

		Object[] association = new Object[3];

		// get timestamp path from scanned data
		SchemaIndexPath path = helper.getSchemaIndexPath(helper.getStartTimestampFullAttributeName());
		association[0] = path.toTupleIndexPath(scannedObject).getTupleObject();

		// get scanned objects
		path = helper.getSchemaIndexPath(this.newObjListPath);
		association[1] = path.toTupleIndexPath(scannedObject).getTupleObject();

		// get predicted objects
		path = helper2.getSchemaIndexPath(this.oldObjListPath);
		association[2] =  path.toTupleIndexPath(predictedObject).getTupleObject();

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

	public void testProcessNext(MVRelationalTuple<M> object, int port) {
		process_next(object, port);
	}

}
