package de.uniol.inf.is.odysseus.objecttracking.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;

public class ApplicationTime implements IApplicationTime{

	List<ITimeInterval> intervals;
	
	public ApplicationTime(){
		this.intervals = new ArrayList<ITimeInterval>();
	}
	
	public ApplicationTime(ApplicationTime original){
		this.intervals = new ArrayList<ITimeInterval>();
		for(ITimeInterval interval : original.intervals){
			this.intervals.add(interval.clone());
		}
	}
	
	public ApplicationTime clone(){
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
	public Collection<ITimeInterval> getAllApplicationTimeIntervals() {
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
	
}
