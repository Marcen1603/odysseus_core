package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;

/**
 * 
 * G is a helper class which contains a min-priority queue list which holds the  
 * group identifier for each non-empty group. Its order is ascending: the 
 * group with the smallest start timestamp first. 
 * 
 * @author Jendrik Poloczek
 * 
 */
public class PriorityQueueG {
	
	/*
	 * groupsQueue stores sweep areas in ascending order of the smallest
	 * timestamp of a PartialNest
	 */
	
	List<DefaultTISweepArea<PartialNest<TimeInterval>>> groupsQueue;	
	Map<DefaultTISweepArea<PartialNest<TimeInterval>>, Integer> gToId;
	
	public PriorityQueueG(){
		this.groupsQueue = 
			new ArrayList<DefaultTISweepArea<PartialNest<TimeInterval>>>();
		this.gToId = 
			new HashMap<DefaultTISweepArea<PartialNest<TimeInterval>>, 
			Integer>();
	};
	
	public PriorityQueueG(PriorityQueueG g){
		this.groupsQueue.addAll(g.groupsQueue);
		this.gToId.putAll(g.gToId);
	}
	
	synchronized public void insert(
			DefaultTISweepArea<PartialNest<TimeInterval>> sa,
			Integer groupID) {
		this.groupsQueue.add(sa);
		this.gToId.put(sa, groupID);
	}

	public boolean isEmpty() {
		return this.groupsQueue.size() == 0;
	}

	synchronized public Integer min() {
		if (isEmpty())
			return -1;

		// die Elemente der queue k�nnen sich �ndern ...
		// TODO: Nicht sehr effizient... Sortieren ist gar nicht
		// notwendig!!
		Collections.sort(this.groupsQueue);
		return gToId.get(this.groupsQueue.get(0));
	}

	synchronized public void removeLastMin() {
		if (isEmpty())
			return;
		gToId.remove(this.groupsQueue.remove(0));
	}
	
	@Override
	public PriorityQueueG clone(){
		return new PriorityQueueG(this);
	}
}
