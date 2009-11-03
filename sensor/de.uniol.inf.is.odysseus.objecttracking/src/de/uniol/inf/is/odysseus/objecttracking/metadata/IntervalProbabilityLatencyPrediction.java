package de.uniol.inf.is.odysseus.objecttracking.metadata;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.latency.Latency;
import de.uniol.inf.is.odysseus.metadata.base.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * This is a metadata item, that implements TimeInterval, IProbability and IPredictionFunction.
 * It works only for relational tuples.
 */
public class IntervalProbabilityLatencyPrediction<T extends MetaAttributeContainer<M>, M extends IProbability> extends TimeInterval implements IPredictionFunction<T, M>, IProbability, ILatency{

	/** Since we use probabilities in this metadata,
	 * the prediction function should also consider
	 * the probabilities.
	 * 
	 */
	private IPredictionFunction<T, M> predFct;
	public IPredictionFunction<T, M> getPredFct() {
		return predFct;
	}

	public void setPredFct(IPredictionFunction<T, M> predFct) {
		this.predFct = predFct;
	}

	public IProbability getProb() {
		return prob;
	}

	public void setProb(IProbability prob) {
		this.prob = prob;
	}

	private IProbability prob;
	private ILatency latency;
	
	public IntervalProbabilityLatencyPrediction(){
		this.prob = new Probability();
		this.latency = new Latency();
		// TODO prüfen, ob diese eine Klasse ausreicht
		// wenn man verschiedene Klassen für PredictionFunctions braucht, dann
		// muss man irgendwie einen Unterscheidungsmechanismus
		// einbauen, der entscheidet, wann welches Objekt
		// erzeugt wird
		this.predFct = new LinearProbabilityPredictionFunction();
	}
	
	public IntervalProbabilityLatencyPrediction(IntervalProbabilityLatencyPrediction copy){
		super(copy);
		this.prob = (IProbability)copy.prob.clone();
		this.latency = (ILatency)copy.latency.clone();
		this.predFct = (IPredictionFunction)copy.predFct.clone();
	}
	
	public IntervalProbabilityLatencyPrediction(IPredictionFunction<T, M> predFct, IProbability prob, ILatency latency){
		this.predFct = predFct;
		this.prob = prob;
		this.latency = latency;
	}
	
	@Override
	public T predictData(SDFAttributeList schema,
			T object, PointInTime t) {
		// if there is no prediction function, return
		// return the original schema
		if(this.predFct == null){
			return (T)object.clone();
		}
		
		return this.predFct.predictData(schema, object, t);
	}
	
	public M predictMetadata(SDFAttributeList schema, T object, PointInTime t){
		// if there is no prediction function,
		// return the original metadata
		if(this.predFct == null){
			return (M)object.getMetadata().clone();
		}
		
		return this.predFct.predictMetadata(schema, object, t);
	}
	
	public T predictAll(SDFAttributeList schema, T object, PointInTime t){
		return this.predFct.predictAll(schema, object, t);
	}

	@Override
	public double[][] getCovariance() {
		return this.prob.getCovariance();
	}

	@Override
	public void setCovariance(double[][] sigma) {
		this.prob.setCovariance(sigma);
	}

	@Override
	public long getLatency() {
		// TODO Auto-generated method stub
		return this.latency.getLatency();
	}

	@Override
	public long getLatencyEnd() {
		// TODO Auto-generated method stub
		return this.latency.getLatencyEnd();
	}

	@Override
	public long getLatencyStart() {
		// TODO Auto-generated method stub
		return this.latency.getLatencyStart();
	}

	@Override
	public void setLatencyEnd(long timestamp) {
		this.latency.setLatencyEnd(timestamp);
	}

	@Override
	public void setLatencyStart(long timestamp) {
		this.latency.setLatencyStart(timestamp);
		
	}

	@Override
	public SDFExpression[] getPredictionFunction() {
		// TODO Auto-generated method stub
		if(this.predFct == null){
			return null;
		}
		return this.predFct.getPredictionFunction();
	}
	
	@Override
	public int[][] getVariables(){
		if(this.predFct != null){
			return this.predFct.getVariables();
		}
		return null;
	}
	
	@Override
	public void setVariables(int[][] vars){
		if(this.predFct != null){
			this.predFct.setVariables(vars);
		}
	}

	@Override
	public void setPredictionFunction(SDFExpression[] expressions) {
		this.predFct.setPredictionFunction(expressions);
		
	}

	@Override
	public void setTimeInterval(ITimeInterval timeInterval) {
		this.predFct.setTimeInterval(timeInterval);
		
	}
	
	@Override
	public IntervalProbabilityLatencyPrediction clone(){
		return new IntervalProbabilityLatencyPrediction(this);
	}

	@Override
	public void initVariables() {
		if(this.predFct != null){
			this.predFct.initVariables();
		}
		
	}

}
