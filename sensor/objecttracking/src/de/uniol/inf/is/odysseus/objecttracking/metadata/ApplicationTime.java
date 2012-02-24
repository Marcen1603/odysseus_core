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
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;

public class ApplicationTime implements IApplicationTime{

	private static final long serialVersionUID = -9089269401446854493L;

	List<ITimeInterval> intervals;
	
	public static int additionalPredicates = 4;
	
	public ApplicationTime(){
		this.intervals = new ArrayList<ITimeInterval>();
		
		// first, all elements are valid always
		// this interval will be restricted by the
		// selection and join predicates
		this.intervals.add(new TimeInterval(PointInTime.getZeroTime(), PointInTime.getInfinityTime()));
		
		// Evaluation
		for(int i = 0; i<additionalPredicates; i++){
			this.intervals.add(new TimeInterval(new PointInTime(i * 40000), new PointInTime((i+1) * 40000)));
		}
	}
	
	public ApplicationTime(ApplicationTime original) {
		this.intervals = new ArrayList<ITimeInterval>();
		for(ITimeInterval interval : original.intervals){
			this.intervals.add((ITimeInterval)interval.clone());
		}
	}
	
	@Override
	public ApplicationTime clone() {
		return new ApplicationTime(this);
	}
	
	@Override
	public void addAllApplicationIntervals(List<ITimeInterval> intervals) {
		this.intervals.addAll(intervals);
	}

	@Override
	public void addApplicationInterval(ITimeInterval interval) {
		this.intervals.add(interval);
	}

	@Override
	public void clearApplicationIntervals() {
		this.intervals.clear();
	}

	@Override
	public List<ITimeInterval> getAllApplicationTimeIntervals() {
		return this.intervals;
	}

	@Override
	public ITimeInterval getApplicationInterval(int pos) {
		return this.intervals.get(pos);
	}

	@Override
	public void removeApplicationInterval(ITimeInterval interval) {
		this.intervals.remove(interval);
	}

	@Override
	public ITimeInterval removeApplicationInterval(int pos) {
		return this.intervals.remove(pos);
	}
	
	@Override
	public String toString(){
		StringBuffer ret = new StringBuffer();
		for(ITimeInterval interval: this.intervals){
			ret.append(interval.toString()).append(";");
		}
		return ret.toString();
	}

	@Override
	public String csvToString() {
		StringBuffer ret = new StringBuffer();
		for(ITimeInterval interval: this.intervals){
			ret.append(interval.csvToString()).append(";");
		}
		return ret.toString();
	}
	
	@Override
	public String getCSVHeader() {
		StringBuffer ret = new StringBuffer();
		for(ITimeInterval interval: this.intervals){
			ret.append(interval.getCSVHeader()).append(";");
		}
		return ret.toString();
	}
	
	@Override
	public void setApplicationIntervals(List<ITimeInterval> intervals) {
		this.intervals = intervals;
		
	}
	
	/**
	 * This list intersects each interval from the left list with each interval from the right list.
	 * 
	 * @param leftRanges must be ordered ascending
	 * @param rightRanges must be ordered ascending
	 * @return A list of intersection intervals.
	 */
	public static List<ITimeInterval> intersectIntervals(List<ITimeInterval> leftRanges, List<ITimeInterval> rightRanges){
		List<ITimeInterval> resultRanges = new ArrayList<ITimeInterval>();
		outer:
		for(ITimeInterval leftInterval: leftRanges){
			for(ITimeInterval rightInterval: rightRanges){
				if(TimeInterval.totallyAfter(rightInterval, leftInterval)){
					continue outer;
				}
				
				ITimeInterval intersection = TimeInterval.intersection(leftInterval, rightInterval);
				if(intersection != null){
					resultRanges.add(intersection);
				}
			}
		}
		return resultRanges;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.ICSVToString#csvToString(boolean)
	 */
	@Override
	public String csvToString(boolean withMetada) {
		return this.csvToString();
	}
	
}
