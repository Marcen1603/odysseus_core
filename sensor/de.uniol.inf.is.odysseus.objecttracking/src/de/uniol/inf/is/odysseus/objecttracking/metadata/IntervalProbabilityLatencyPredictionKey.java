package de.uniol.inf.is.odysseus.objecttracking.metadata;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.latency.Latency;

public class IntervalProbabilityLatencyPredictionKey<K> extends TimeInterval implements IPredictionFunctionKey<K>, IProbability, ILatency{

	IPredictionFunctionKey<K> predFctKey;
	IProbability prob;
	ILatency lat;
	
	public IntervalProbabilityLatencyPredictionKey(){
		super();
		this.predFctKey = new PredictionFunctionKey<K>();
		this.prob = new Probability();
		this.lat = new Latency();
	}
	
	public IntervalProbabilityLatencyPredictionKey(IntervalProbabilityLatencyPredictionKey<K> copy){
		super(copy);
		this.predFctKey = (IPredictionFunctionKey)copy.predFctKey.clone();
		this.prob = (IProbability)copy.prob.clone();
		this.lat = (ILatency)copy.lat.clone();
	}
	
	public IntervalProbabilityLatencyPredictionKey(IPredictionFunctionKey<K> predFctKey, IProbability prob, ILatency lat){
		super();
		this.predFctKey = predFctKey;
		this.prob = prob;
		this.lat = lat;
	}
	
	@Override
	public K getPredictionFunctionKey() {
		return this.predFctKey.getPredictionFunctionKey();
	}

	@Override
	public void setPredictionFunctionKey(K key) {
		this.predFctKey.setPredictionFunctionKey(key);
		
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
		return this.lat.getLatency();
	}

	@Override
	public long getLatencyEnd() {
		return this.lat.getLatencyEnd();
	}

	@Override
	public long getLatencyStart() {
		return this.lat.getLatencyStart();
	}

	@Override
	public void setLatencyEnd(long timestamp) {
		this.lat.setLatencyEnd(timestamp);
		
	}

	@Override
	public void setLatencyStart(long timestamp) {
		this.lat.setLatencyStart(timestamp);
		
	}
	
	public IntervalProbabilityLatencyPredictionKey<K> clone(){
		return new IntervalProbabilityLatencyPredictionKey<K>(this);
	}

}
