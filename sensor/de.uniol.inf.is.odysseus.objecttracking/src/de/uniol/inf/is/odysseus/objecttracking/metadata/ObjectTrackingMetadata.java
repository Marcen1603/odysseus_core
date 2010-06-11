package de.uniol.inf.is.odysseus.objecttracking.metadata;

import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.latency.Latency;

public class ObjectTrackingMetadata<K> extends TimeInterval implements IPredictionFunctionKey<K>, IProbability, ILatency, IApplicationTime{

	IPredictionFunctionKey<K> predFctKey;
	IProbability prob;
	ILatency lat;
	IApplicationTime appTime;
	
	public ObjectTrackingMetadata(){
		super();
		this.predFctKey = new PredictionFunctionKey<K>();
		this.prob = new Probability();
		this.lat = new Latency();
		this.appTime = new ApplicationTime();
	}
	
	public ObjectTrackingMetadata(ObjectTrackingMetadata<K> copy) {
		super(copy);
		if(copy.predFctKey != null){
			this.predFctKey = (IPredictionFunctionKey)copy.predFctKey.clone();
		}
		if(copy.prob != null){
			this.prob = (IProbability)copy.prob.clone();
		}
		
		if(copy.lat != null){
			this.lat = (ILatency)copy.lat.clone();
		}
		
		if(copy.appTime != null){
			this.appTime = (IApplicationTime)copy.appTime.clone();
		}
	}
	
	public ObjectTrackingMetadata(IPredictionFunctionKey<K> predFctKey, IProbability prob, ILatency lat, IApplicationTime appTime){
		super();
		this.predFctKey = predFctKey;
		this.prob = prob;
		this.lat = lat;
		this.appTime = appTime;
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
	
	public ObjectTrackingMetadata<K> clone() {
		return new ObjectTrackingMetadata<K>(this);
	}
	
	@Override
	public String toString(){
		return "Latency: " + this.lat.toString() + 
			" | Cov: " + this.prob.toString() + 
			" | PredFctKey: " + this.predFctKey.toString() + 
			" | tTime: " + super.toString() + 
			" | appTime: " + this.appTime.toString();
	}

	@Override
	public void addAllApplicationIntervals(List<ITimeInterval> intervals) {
		this.appTime.addAllApplicationIntervals(intervals);
		
	}

	@Override
	public void addApplicationInterval(ITimeInterval interval) {
		this.appTime.addApplicationInterval(interval);
		
	}

	@Override
	public void clearApplicationIntervals() {
		this.appTime.clearApplicationIntervals();
		
	}

	@Override
	public List<ITimeInterval> getAllApplicationTimeIntervals() {
		return this.appTime.getAllApplicationTimeIntervals();
	}

	@Override
	public ITimeInterval getApplicationInterval(int pos) {
		return this.appTime.getApplicationInterval(pos);
	}

	@Override
	public boolean isApplictionTimeValid() {
		return this.appTime.isApplictionTimeValid();
	}

	@Override
	public void removeApplicationInterval(ITimeInterval interval) {
		this.appTime.removeApplicationInterval(interval);
	}

	@Override
	public ITimeInterval removeApplicationInterval(int pos) {
		return this.appTime.removeApplicationInterval(pos);
	}

	@Override
	public void setApplicationIntervals(List<ITimeInterval> intervals) {
		this.appTime.setApplicationIntervals(intervals);
		
	}

}
