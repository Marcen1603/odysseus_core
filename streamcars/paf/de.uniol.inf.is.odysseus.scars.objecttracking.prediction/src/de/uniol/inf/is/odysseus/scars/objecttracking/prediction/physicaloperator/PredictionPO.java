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
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata.PredictionFunctionContainer;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;

public class PredictionPO<M extends IProbability & ITimeInterval & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>>> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	
	private MVRelationalTuple<M> currentTime;
	private MVRelationalTuple<M> currentScan;
	
	private int[] objListPath;
	
	private ISweepArea<MVRelationalTuple<M>> scanBuffer;

	private PredictionFunctionContainer<M> predictionFunctions;
	
	public PredictionPO() {
	}
	
	public PredictionPO(PredictionPO<M> copy) {
		super(copy);
		this.predictionFunctions = new PredictionFunctionContainer<M>(copy.predictionFunctions);
		objListPath = new int[copy.objListPath.length];
		System.arraycopy(copy.objListPath, 0, this.objListPath, 0, copy.objListPath.length);
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
		scanBuffer = new DefaultTISweepArea<MVRelationalTuple<M>>();
		scanBuffer.setQueryPredicate(new TruePredicate<MVRelationalTuple<M>>());
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		if(port == 0) {
			currentTime = object.clone();
		} else if(port == 1) {
			scanBuffer.insert(object);
		}
	}
	
//	@SuppressWarnings("unchecked")
	private void predictData() {
		TupleHelper helper = new TupleHelper(currentScan);
		MVRelationalTuple<?> list = (MVRelationalTuple<?>)helper.getObject(objListPath, false);
		for(int index=0; index < list.getAttributeCount(); index++) {
			MVRelationalTuple<M> obj = list.getAttribute(index);
			IPredictionFunction<M> pf = predictionFunctions.get(obj.getMetadata().getPredictionFunctionKey());
			pf.predictData(currentScan, currentTime, index);
			M metadata = obj.getMetadata();
			pf.predictMetadata(metadata, currentScan, currentTime, index);
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
	}
	
	@Override
	public PredictionPO<M> clone() {
		return new PredictionPO<M>(this);
	}
}
