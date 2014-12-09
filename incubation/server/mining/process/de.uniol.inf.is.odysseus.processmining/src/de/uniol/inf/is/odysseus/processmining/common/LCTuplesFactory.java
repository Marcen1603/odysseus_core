package de.uniol.inf.is.odysseus.processmining.common;

/**
 * Tuple Factory for Process Mining adapted Lossy Counter 
 * @author Philipp Geers
 *
 */
public class LCTuplesFactory{
	/**
	 * Factory for creating the tuples of the adapted lossy counting algorithm
	 * @param type
	 * @return
	 */
	public static AbstractLCTuple createLCTuple(LCTupleType type) {
		AbstractLCTuple tuple = null;
		
		switch(type){
		case Activity:
			tuple = new ActivityTuple();
			break;
		case Case:
			tuple = new CaseTuple();
			break;
		case DirectlyFollowRelation:
			tuple = new DFRTuple();
			break;
		case DirectlyFollowRelationLoop:
			tuple = new DirectlyFollowLoopTuple();
			break;
		default:
			break;
		}
		return tuple;
	}
	
	public static AbstractLCTuple createCaseTuple(String id, String activity, Integer maxError) {
		AbstractLCTuple tuple = new CaseTuple();
		tuple.setActivity(activity);
		tuple.setMaxError(maxError);
		((CaseTuple)tuple).setCaseID(id);
		return tuple;
	}
	
	public static AbstractLCTuple createDFRTuple(String activity, String followingActivity, Integer maxError) {
		AbstractLCTuple tuple = new DFRTuple();
		tuple.setActivity(activity);
		tuple.setMaxError(maxError);
		((DFRTuple)tuple).setFollowActivity(followingActivity);
		return tuple;
	}
	
	public static AbstractLCTuple createActivityTuple(String activity, Integer maxError) {
		AbstractLCTuple tuple = new ActivityTuple();
		tuple.setActivity(activity);
		tuple.setMaxError(maxError);
		return tuple;
	}
	public static AbstractLCTuple createDFRLoopTuple(String id, String activity, String followingActivity, Integer maxError){
		AbstractLCTuple tuple = new DirectlyFollowLoopTuple();
		((DirectlyFollowLoopTuple)tuple).setCaseID(id);
		((DirectlyFollowLoopTuple)tuple).setFollowActivity(followingActivity);
		tuple.setActivity(activity);
		tuple.setMaxError(maxError);
		return tuple;
	}


}
