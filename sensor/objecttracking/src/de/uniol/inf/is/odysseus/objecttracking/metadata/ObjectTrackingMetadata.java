/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.objecttracking.metadata;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.latency.Latency;

@SuppressWarnings({"unchecked","rawtypes"})
public class ObjectTrackingMetadata<K> implements IPredictionFunctionKey<K>, IProbability, ILatency, IApplicationTime, ITimeInterval{
    
    private static final long serialVersionUID = 1L;
    
    IPredictionFunctionKey<K> predFctKey;
	IProbability prob;
	ILatency lat;
	IApplicationTime appTime;
	ITimeInterval streamTime;
	
	@Override
	public int compareTo(ITimeInterval arg0) {
		return streamTime.compareTo(arg0);
	}

	@Override
	public PointInTime getEnd() {
		return streamTime.getEnd();
	}

	@Override
	public PointInTime getStart() {
		return streamTime.getStart();
	}

	@Override
	public void setEnd(PointInTime point) {
		streamTime.setEnd(point);
	}

	@Override
	public void setStart(PointInTime point) {
		streamTime.setStart(point);
	}

	@Override
	public void setStartAndEnd(PointInTime start, PointInTime end) {
		streamTime.setStartAndEnd(start, end);	
	}
	
	@Override
	public String toString(PointInTime baseTime) {
		return "ObjectTrackingMetadata: StreamTime = " + streamTime.toString(baseTime) + " | Latency = " + this.lat.getLatency();
	}

	public ObjectTrackingMetadata(){
		super();
		this.predFctKey = new PredictionFunctionKey<K>();
		this.prob = new Probability();
		this.lat = new Latency();
		this.appTime = new ApplicationTime();
		this.streamTime = new TimeInterval();
	}
	
    public ObjectTrackingMetadata(PointInTime start, PointInTime end) {
        super();
        this.predFctKey = new PredictionFunctionKey<K>();
        this.prob = new Probability();
        this.lat = new Latency();
        this.appTime = new ApplicationTime();
        this.streamTime = new TimeInterval(start, end);
     }

	public ObjectTrackingMetadata(ObjectTrackingMetadata<K> copy) {
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
		if(copy.streamTime != null){
			this.streamTime = (ITimeInterval)copy.streamTime.clone();
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
	
	@Override
	public ObjectTrackingMetadata<K> clone() {
		return new ObjectTrackingMetadata<K>(this);
	}
	
	@Override
	public String toString(){
		return "Latency: " + this.lat.toString() + 
			" | Cov: " + this.prob.toString() + 
			" | PredFctKey: " + this.predFctKey.toString() + 
			" | tTime: " + this.getStreamTime().toString() + 
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
	
	public void setStreamTime(ITimeInterval interval){
		this.streamTime = interval;
	}
	
	public ITimeInterval getStreamTime(){
		return this.streamTime;
	}

	@Override
	public int[] getMVAttributeIndices() {
		return prob.getMVAttributeIndices();
	}

	@Override
	public void setMVAttributeIndices(int[] indices) {
		prob.setMVAttributeIndices(indices);
	}

	@Override
	public ArrayList<int[]> getAttributePaths() {
		return prob.getAttributePaths();
	}

	@Override
	public int getIndexOfKovMatrix(int[] path) {
		return prob.getIndexOfKovMatrix(path);
	}

	@Override
	public void setAttributePaths(ArrayList<int[]> paths) {
		prob.setAttributePaths(paths);
	}

	@Override
	public String csvToString() {
		return null;
	}
	
	@Override
	public String getCSVHeader() {
		return null;
	}

	@Override
	public int getCovarianceIndex(String fullAttributeName) {
		return prob.getCovarianceIndex(fullAttributeName);
	}

	@Override
	public String getAttributeName(int index) {
		return prob.getAttributeName(index);
	}

	@Override
	public void setAttributeMapping(List<String> indices) {
		this.prob.setAttributeMapping(indices);
	}

	@Override
	public List<String> getAttributMapping() {
		return this.prob.getAttributMapping();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.ICSVToString#csvToString(boolean)
	 */
	@Override
	public String csvToString(boolean withMetada) {
		return this.csvToString();
	}
	
}
