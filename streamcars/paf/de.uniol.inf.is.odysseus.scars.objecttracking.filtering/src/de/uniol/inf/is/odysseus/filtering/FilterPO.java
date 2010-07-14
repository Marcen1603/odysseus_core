package de.uniol.inf.is.odysseus.filtering;


import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

public class FilterPO<M extends IProbability> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>>  {

	private IGainFunction<M> gainFunction;
	
	private ICorrectStateCovarianceFunction<M> correctStateCovarianceFunction;
	
	private ICorrectStateEstimateFunction<M> correctStateEstimateFunction;
	
	private MVRelationalTuple oldTuple;
	
	private MVRelationalTuple newTuple;
	
	
	public FilterPO() {
	super();	
	}
	
	public FilterPO(FilterAO<M> filterAO) {
		this.gainFunction = filterAO.getGainfunction();
		this.correctStateCovarianceFunction = filterAO.getCorrectStateCovarianceFunction();
		this.correctStateEstimateFunction = filterAO.getCorrectStateEstimate();
	}
	public FilterPO(FilterPO<M> copy) {
	super(copy);
	}
	
	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		// TODO Auto-generated method stub
	
	// output modell, etc ??
	Object matrixes[] = null;
	Object Gain = null;
	
	// zuerst den gain berechnen 
	if (port == 0) oldTuple= object;
	Gain = gainFunction.computeGain(oldTuple, matrixes);
	
	// Messwerte
	if (port == 1) {
		
		newTuple = object; 
		// update Covariance
		oldTuple = correctStateCovarianceFunction.computeStateCovariance(oldTuple, Gain, matrixes);
		// update State
		oldTuple = correctStateEstimateFunction.computeStateEstimate(oldTuple,newTuple, Gain, matrixes);
		
		// transfer to broker
		transfer(object);
	}
	
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}

	public int hashcode() {
		return 0;
	}

	public boolean equals(Object obj) {
		return false;
		
	}
}
