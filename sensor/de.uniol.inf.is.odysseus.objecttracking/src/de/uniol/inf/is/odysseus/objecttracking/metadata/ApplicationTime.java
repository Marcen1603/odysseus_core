package de.uniol.inf.is.odysseus.objecttracking.metadata;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;

public class ApplicationTime implements IApplicationTime{

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
	
	public ApplicationTime(ApplicationTime original) throws CloneNotSupportedException{
		this.intervals = new ArrayList<ITimeInterval>();
		for(ITimeInterval interval : original.intervals){
			this.intervals.add(interval.clone());
		}
	}
	
	public ApplicationTime clone()throws CloneNotSupportedException{
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
	public boolean isApplictionTimeValid() {
		for(ITimeInterval interval: this.intervals){
			if(!interval.isValid()){
				return false;
			}
		}
		return true;
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
		String ret = "";
		for(ITimeInterval interval: this.intervals){
			ret += interval.toString() + ";";
		}
		return ret;
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
	
}
