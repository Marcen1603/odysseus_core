package de.uniol.inf.is.odysseus.hmm;

import java.util.ArrayList;
import java.util.Iterator;


public class HmmWindow {
	
//	private int numStates;
	private int timewindow;
	
	ArrayList<HmmWindowGroup> groups;
	
	public HmmWindow(int timewindow) {
		this.groups = new ArrayList<HmmWindowGroup>();
//		this.numStates = numStates;
		this.timewindow = timewindow;
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
	
	public void sweapOldItems(long currentTimestamp){
		Iterator<HmmWindowGroup> it = groups.iterator();
		while(it.hasNext()) {
			HmmWindowGroup hwg = it.next();
			if((currentTimestamp - hwg.getTimestamp()) > timewindow) {
				it.remove();
			} else {
				break;
			}
		}
	}
	
}
