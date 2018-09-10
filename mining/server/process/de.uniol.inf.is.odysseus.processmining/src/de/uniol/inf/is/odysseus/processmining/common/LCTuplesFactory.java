package de.uniol.inf.is.odysseus.processmining.common;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * Tuple Factory for Process Mining adapted Lossy Counter 
 * @author Philipp Geers
 *
 */
public class LCTuplesFactory<T extends IMetaAttribute>{
	/**
	 * Factory for creating the tuples of the adapted lossy counting algorithm
	 * @param <T>
	 * @param type
	 * @return
	 */
	
	// Uses SuppressWarnings(rawtypes) for static methods
	@SuppressWarnings("rawtypes")
	public static AbstractLCTuple createLCTuple(LCTupleType type) {
		AbstractLCTuple tuple = null;
		
		switch(type){
		case Activity:
			return new ActivityTuple();
		case Case:
			return new CaseTuple();
		case DirectlyFollowRelation:
			return new DFRTuple();
		case DirectlyFollowRelationLoop:
			return new DirectlyFollowLoopTuple();
		default:
			break;
		}
		return tuple;
	}
	
	@SuppressWarnings("rawtypes")
	public static AbstractLCTuple createCaseTuple(String id, String activity, Integer maxError) {
		AbstractLCTuple tuple = new CaseTuple();
		tuple.setActivity(activity);
		tuple.setMaxError(maxError);
		((CaseTuple)tuple).setCaseID(id);
		return tuple;
	}
	
	@SuppressWarnings("rawtypes")
	public static AbstractLCTuple createDFRTuple(String activity, String followingActivity, Integer maxError) {
		AbstractLCTuple tuple = new DFRTuple();
		tuple.setActivity(activity);
		tuple.setMaxError(maxError);
		((DFRTuple)tuple).setFollowActivity(followingActivity);
		return tuple;
	}
	
	@SuppressWarnings("rawtypes")
	public static AbstractLCTuple createActivityTuple(String activity, Integer maxError) {
		AbstractLCTuple tuple = new ActivityTuple();
		tuple.setActivity(activity);
		tuple.setMaxError(maxError);
		return tuple;
	}
	@SuppressWarnings("rawtypes")
	public static AbstractLCTuple createDFRLoopTuple(String id, String activity, String followingActivity, Integer maxError){
		AbstractLCTuple<?> tuple = new DirectlyFollowLoopTuple();
		((DirectlyFollowLoopTuple)tuple).setCaseID(id);
		((DirectlyFollowLoopTuple)tuple).setFollowActivity(followingActivity);
		tuple.setActivity(activity);
		tuple.setMaxError(maxError);
		return tuple;
	}


}
