package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window.helper;

import de.uniol.inf.is.odysseus.base.PointInTime;

/** Allgemeine Klasse fuer Fensterberechnungen. 
 * @author Marco Grawunder
 *
 */

public class WindowCalculator {
	
	
	static public PointInTime calcJumpingWindowEnd(PointInTime startTimestamp, long windowSize){
		return new PointInTime((startTimestamp.getMainPoint()/windowSize + 1) * windowSize);
	}
	
	
	static public PointInTime calcSlidingWindowEnd(PointInTime startTimestamp, long windowSize){
		return startTimestamp.sum(windowSize, 0);
	}
	
	
	static public PointInTime calcSlidingDeltaWindowEnd(PointInTime startTimestamp, long windowAdvance, long windowSize){
		return new PointInTime(startTimestamp.getMainPoint()/windowAdvance * windowAdvance + windowSize);
	}


}
