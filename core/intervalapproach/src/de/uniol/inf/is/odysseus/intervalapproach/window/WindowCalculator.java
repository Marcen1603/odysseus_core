/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.intervalapproach.window;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
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
		return time.getStart().sum(windowSize);
	}
	
	static public PointInTime calcSlidingWindowEnd(PointInTime startTimestamp, long windowSize){
		return startTimestamp.sum(windowSize);
	}
	
	static public PointInTime calcSlidingDeltaWindowEnd(ITimeInterval time, long windowAdvance, long windowSize){
		return new PointInTime(time.getStart().getMainPoint()/windowAdvance * windowAdvance + windowSize);
	}
	
	static public PointInTime calcSlidingDeltaWindowEnd(PointInTime startTimestamp, long windowAdvance, long windowSize){
		return new PointInTime(startTimestamp.getMainPoint()/windowAdvance * windowAdvance + windowSize);
	}


}
