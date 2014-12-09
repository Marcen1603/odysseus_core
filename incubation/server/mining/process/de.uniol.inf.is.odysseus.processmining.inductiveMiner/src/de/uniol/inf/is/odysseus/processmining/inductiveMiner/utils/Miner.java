package de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils;

import java.util.HashMap;


import de.uniol.inf.is.odysseus.processmining.common.AbstractLCTuple;
import de.uniol.inf.is.odysseus.processmining.common.InductiveMinerTransferTuple;

public class Miner {
	private HashMap<Object, AbstractLCTuple> activities;
	private HashMap<Object, AbstractLCTuple> directlyFollowRelations;
	private HashMap<Object, AbstractLCTuple> shortLoops;
	private HashMap<Object, AbstractLCTuple> startActivites;

	public Miner(InductiveMinerTransferTuple data){
		this.activities = data.getActivities();
		this.directlyFollowRelations = data.getDirectlyFollowRelations();
		this.shortLoops = data.getShortLoops();
		this.startActivites = data.getStartActivites();
	}
	
	
	

}
