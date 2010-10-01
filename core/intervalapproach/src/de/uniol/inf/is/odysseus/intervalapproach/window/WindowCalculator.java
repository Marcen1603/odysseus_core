package de.uniol.inf.is.odysseus.intervalapproach.window;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;

/** Allgemeine Klasse fuer Fensterberechnungen. Diese Berechnungen sind fuer POS/NEG und Intervall-
 * Ansatz identisch.
 * @author Marco Grawunder
 *
 */

public class WindowCalculator {
	
	static public PointInTime calcJumpingWindowEnd(ITimeInterval time, long windowSize) {
		// integer division is used, to determine the closest point
		// in time x*windowSize with start < end
		return new PointInTime((time.getStart().getMainPoint()/ windowSize + 1)* windowSize);
	}
	
	static public PointInTime calcJumpingWindowEnd(PointInTime startTimestamp, long windowSize){
		return new PointInTime((startTimestamp.getMainPoint()/windowSize + 1) * windowSize);
	}
	
	static public PointInTime calcSlidingWindowEnd(ITimeInterval time, long windowSize) {
		return time.getStart().sum(windowSize, 0);
	}
	
	static public PointInTime calcSlidingWindowEnd(PointInTime startTimestamp, long windowSize){
		return startTimestamp.sum(windowSize, 0);
	}
	
	static public PointInTime calcSlidingDeltaWindowEnd(ITimeInterval time, long windowAdvance, long windowSize){
		return new PointInTime(time.getStart().getMainPoint()/windowAdvance * windowAdvance + windowSize);
	}
	
	static public PointInTime calcSlidingDeltaWindowEnd(PointInTime startTimestamp, long windowAdvance, long windowSize){
		return new PointInTime(startTimestamp.getMainPoint()/windowAdvance * windowAdvance + windowSize);
	}


}
