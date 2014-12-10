package de.uniol.inf.is.odysseus.processmining.common;

import java.util.HashMap;
import com.google.common.collect.Maps;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class InductiveMinerTransferTuple extends Tuple {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7748619300306184037L;
	private final int ACTIVITY_MAP = 0;
	private final int DFR_MAP = 1;
	private final int SHORTLOOPS = 2;
	private final int STARTS = 3;
	private final int ENDINGS = 4;
	
	public InductiveMinerTransferTuple(){
		super(5, true);
	}
	
	public InductiveMinerTransferTuple( InductiveMinerTransferTuple copy ) {
		super(copy);
		
		this.activities = Maps.newHashMap(copy.activities);
		this.directlyFollowRelations = Maps.newHashMap(copy.directlyFollowRelations);
		this.shortLoops = Maps.newHashMap(copy.shortLoops);
		this.startActivites = Maps.newHashMap(copy.startActivites);
		this.endActivities = Maps.newHashMap(copy.endActivities);
	}
	
	private HashMap<Object, AbstractLCTuple> activities = Maps.newHashMap();
	private HashMap<Object, AbstractLCTuple> directlyFollowRelations = Maps.newHashMap();
	private HashMap<Object, AbstractLCTuple> shortLoops = Maps.newHashMap();
	private HashMap<Object, AbstractLCTuple> startActivites = Maps.newHashMap();
	private HashMap<Object, AbstractLCTuple> endActivities = Maps.newHashMap();
	
	public HashMap<Object, AbstractLCTuple> getActivities() {
		return (HashMap<Object, AbstractLCTuple>) this.getAttribute(ACTIVITY_MAP);
	}
	public void setActivities(HashMap<Object, AbstractLCTuple> activities) {
		this.setAttribute(ACTIVITY_MAP, activities);
	}
	public HashMap<Object, AbstractLCTuple> getDirectlyFollowRelations() {
		return (HashMap<Object, AbstractLCTuple>) this.getAttribute(DFR_MAP);
	}
	public void setDirectlyFollowRelations(
			HashMap<Object, AbstractLCTuple> directlyFollowRelations) {
		this.setAttribute(DFR_MAP, directlyFollowRelations);
	}
	public HashMap<Object, AbstractLCTuple> getShortLoops() {
		return (HashMap<Object, AbstractLCTuple>) this.getAttribute(SHORTLOOPS);
	}
	public void setShortLoops(HashMap<Object, AbstractLCTuple> shortLoops) {
		this.setAttribute(SHORTLOOPS, shortLoops);
	}
	public HashMap<Object, AbstractLCTuple> getStartActivites() {
		return (HashMap<Object, AbstractLCTuple>) this.getAttribute(STARTS);
	}
	public void setStartActivites(HashMap<Object, AbstractLCTuple> startActivites) {
		this.setAttribute(STARTS, startActivites);
	}
	public HashMap<Object, AbstractLCTuple> getEndActivities() {
		return (HashMap<Object, AbstractLCTuple>) this.getAttribute(ENDINGS);
	}
	public void setEndActivities(HashMap<Object, AbstractLCTuple> endActivities) {
		this.setAttribute(ENDINGS, endActivities);
	}
	public InductiveMinerTransferTuple getDeepClone(){
		InductiveMinerTransferTuple clone = new InductiveMinerTransferTuple();
		clone.setActivities(Maps.newHashMap(this.activities));
		clone.setEndActivities(Maps.newHashMap(this.endActivities));
		clone.setDirectlyFollowRelations(Maps.newHashMap(this.directlyFollowRelations));
		clone.setShortLoops(Maps.newHashMap(this.shortLoops));
		clone.setStartActivites(Maps.newHashMap(this.startActivites));
		
		return clone;
	}

	@Override
	public InductiveMinerTransferTuple clone() {
		return new InductiveMinerTransferTuple(this);
	}
}
