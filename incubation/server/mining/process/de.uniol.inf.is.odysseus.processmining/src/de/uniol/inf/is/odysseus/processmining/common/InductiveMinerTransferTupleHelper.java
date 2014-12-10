package de.uniol.inf.is.odysseus.processmining.common;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

@SuppressWarnings("unchecked")
public class InductiveMinerTransferTupleHelper<T extends IMetaAttribute> {

	private final static int ACTIVITY_MAP = 0;
	private final static int DFR_MAP = 1;
	private final static int SHORTLOOPS = 2;
	private final static int STARTS = 3;
	private final static int ENDINGS = 4;

	
	public HashMap<Object, AbstractLCTuple<T>> getActivities(Tuple<T> tuple) {
		return (HashMap<Object, AbstractLCTuple<T>>) tuple
				.getAttribute(ACTIVITY_MAP);
	}

	public void setActivities(Tuple<T> tuple,
			HashMap<Object, AbstractLCTuple<T>> activities) {
		tuple.setAttribute(ACTIVITY_MAP, activities);
	}

	public HashMap<Object, AbstractLCTuple<T>> getDirectlyFollowRelations(
			Tuple<T> tuple) {
		return (HashMap<Object, AbstractLCTuple<T>>) tuple.getAttribute(DFR_MAP);
	}

	public void setDirectlyFollowRelations(Tuple<T> tuple,
			HashMap<Object, AbstractLCTuple<T>> directlyFollowRelations) {
		tuple.setAttribute(DFR_MAP, directlyFollowRelations);
	}

	public HashMap<Object, AbstractLCTuple<T>> getShortLoops(Tuple<T> tuple) {
		return (HashMap<Object, AbstractLCTuple<T>>) tuple
				.getAttribute(SHORTLOOPS);
	}

	public void setShortLoops(Tuple<T> tuple,
			HashMap<Object, AbstractLCTuple<T>> shortLoops) {
		tuple.setAttribute(SHORTLOOPS, shortLoops);
	}

	public HashMap<Object, AbstractLCTuple<T>> getStartActivites(Tuple<T> tuple) {
		return (HashMap<Object, AbstractLCTuple<T>>) tuple.getAttribute(STARTS);
	}

	public void setStartActivites(Tuple<T> tuple,
			HashMap<Object, AbstractLCTuple<T>> startActivites) {
		tuple.setAttribute(STARTS, startActivites);
	}

	public HashMap<Object, AbstractLCTuple<T>> getEndActivities(Tuple<T> tuple) {
		return (HashMap<Object, AbstractLCTuple<T>>) tuple.getAttribute(ENDINGS);
	}

	public void setEndActivities(Tuple<T> tuple,
			HashMap<Object, AbstractLCTuple<T>> endActivities) {
		tuple.setAttribute(ENDINGS, endActivities);
	}
}
