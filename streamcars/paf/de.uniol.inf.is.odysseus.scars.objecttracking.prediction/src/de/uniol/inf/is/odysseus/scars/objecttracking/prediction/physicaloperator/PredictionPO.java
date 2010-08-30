package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.physicaloperator;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata.PredictionFunctionContainer;
import de.uniol.inf.is.odysseus.scars.util.PointInTimeSweepArea;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class PredictionPO<M extends IProbability & ITimeInterval & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>>> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

//	private MVRelationalTuple<M> currentTimeTuple;
//	private MVRelationalTuple<M> currentScanTuple;

	private int[] objListPath;
	private SchemaIndexPath currentTimeSchemaPath;
	private SchemaIndexPath currentScanTimeSchemaPath;
	private PointInTimeSweepArea<M> timeQueue;
	private PointInTimeSweepArea<M> scanQueue;

	private PredictionFunctionContainer<M> predictionFunctions;

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
		timeQueue = new PointInTimeSweepArea<M>();
		scanQueue = new PointInTimeSweepArea<M>();

	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		if (port == 0) {
			timeQueue.insert(object.clone());
//			currentTimeTuple = object.clone();
		} else if (port == 1) {
			scanQueue.insert(object.clone());
//			currentScanTuple = object.clone();
		}
		if(!timeQueue.isEmpty() && !scanQueue.isEmpty()) {
			transfer(predictData(timeQueue.poll(), scanQueue.poll()));
		}
		
//		if (currentTimeTuple != null && currentScanTuple != null) {
//			predictData();
//			MVRelationalTuple<M> tmp = currentScanTuple;
//			currentScanTuple = null;
//			transfer(tmp);
//		}
	}

	// @SuppressWarnings("unchecked")
	private MVRelationalTuple<M> predictData(MVRelationalTuple<M> time, MVRelationalTuple<M> scan) {
		
		if (scan != null) {
			TupleHelper helper = new TupleHelper(scan);
			MVRelationalTuple<?> list = (MVRelationalTuple<?>) helper.getObject(objListPath);
			for (int index = 0; index < list.getAttributeCount(); index++) {
				MVRelationalTuple<M> obj = list.getAttribute(index);
				IPredictionFunction<M> pf = predictionFunctions.get(obj.getMetadata().getPredictionFunctionKey());
				if (pf != null) {
					pf.predictData(scan, time, index);
					M metadata = obj.getMetadata();
					pf.predictMetadata(metadata, scan, time, index);
				} else {
					System.err.println("No PredictionFunction assigned (NO DEFAULT PREDICTION_FUNCTION)");
				}

			}
			TupleIndexPath scanTimeTPath = currentTimeSchemaPath.toTupleIndexPath(time);
			Long currentTimeValue = (Long) scanTimeTPath.getTupleObject();

			scan.getMetadata().getStart().setMainPoint(currentTimeValue);
			TupleIndexPath currentScanTimeTPath = currentScanTimeSchemaPath.toTupleIndexPath(scan);
			currentScanTimeTPath.setTupleObject(currentTimeValue);
		} else {
			System.err.println("Prediction: currentScanTuple was null!!");
		}
		return scan;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
	}

	@Override
	public PredictionPO<M> clone() {
		return new PredictionPO<M>(this);
	}
}
