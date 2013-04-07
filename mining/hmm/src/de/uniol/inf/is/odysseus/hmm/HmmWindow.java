package de.uniol.inf.is.odysseus.hmm;

import java.util.ArrayList;
import java.util.Iterator;


public class HmmWindow {
	
	public int numStates;
	
	ArrayList<HmmWindowGroup> groups;
	
	public HmmWindow(int numStates) {
		groups = new ArrayList<HmmWindowGroup>();
		this.numStates = numStates;
	}

	
//	public void checkTimestamps(int timeWindow, int timestamp) {
//		Iterator<HmmObservationAlphaRow> it = alphas.iterator();
//		while(it.hasNext()) {
//			HmmObservationAlphaRow obs = it.next();
//			if((timestamp - obs.timestamp) > timeWindow) {
//				it.remove();
//			} else {
//				break;
//			}
//		}
//	}
	
	public void addGroup(HmmWindowGroup pGroup){
		groups.add(pGroup);
	}
	
	public void sweapOldItems(){
		
	}
	
	
	public int getNumStates() {
		return numStates;
	}

	public void setNumStates(int numStates) {
		this.numStates = numStates;
	}
}
