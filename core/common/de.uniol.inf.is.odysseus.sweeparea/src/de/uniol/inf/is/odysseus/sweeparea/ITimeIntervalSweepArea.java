/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.sweeparea;

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public interface ITimeIntervalSweepArea<T> extends ISweepArea<T>{

	/**
	 * Removes all elements that have an end-timestamp which is before the given time
	 * @param time a given time
	 */
	void purgeElementsBefore(PointInTime time);

	/**
	 * This method returns and removes all elements from the sweep area that
	 * have a time interval that lies totally before the passed interval.
	 *
	 * @param validity All elements with a time interval that lies totally before
	 *            this interval will be returned and removed from this
	 *            sweeparea.
	 *
	 * @return an iterator of all extracted elements
	 */
	Iterator<T> extractElementsBefore(PointInTime time);

	/**
	 * This method returns and removes all elements from the sweep area that
	 * have a time interval that lies totally before the passed interval.
	 *
	 * @param validity All elements with a time interval that lies totally before
	 *            this interval will be returned and removed from this
	 *            sweeparea.
	 *
	 * @return an iterator of all extracted elements
	 */
	List<T> extractElementsBeforeAsList(PointInTime time);


	/**
	 * Delivers the newest element without removing
	 *
	 * @return
	 */
	T peekLast();

	/**
	 * This method remove from the sweep area all elements starting before a distinct point in time
	 * this is not the same as overlaps, because it also delivers elements with end time stamp
	 * before time
	 * @param time
	 * @return
	 */
	Iterator<T> extractElementsStartingBefore(PointInTime time);

	/**
	 * This method returns from the sweep area all elements starting before a distinct point in time
	 * this is not the same as overlaps, because it also delivers elements with end time stamp
	 * before time
	 * @param time
	 * @return
	 */
	Iterator<T> queryElementsStartingBefore(PointInTime validity);


	/**
	 * This method remove from the sweep area all elements starting before or with a distinct point in time
	 * this is not the same as overlaps, because it also delivers elements with end time stamp
	 * before time
	 * @param time
	 * @return
	 */
	Iterator<T> extractElementsStartingBeforeOrEquals(PointInTime time);

	/**
	 * This method remove from the sweep area all elements that have the same start point
	 * as time
	 * @param time
	 * @return
	 */
	Iterator<T> extractElementsStartingEquals(PointInTime validity);


	Iterator<T> queryOverlaps(ITimeInterval interval);
	Iterator<T> extractOverlaps(ITimeInterval t);

	List<T> queryOverlapsAsList(ITimeInterval interval);
	List<T> queryOverlapsAsListExtractOutdated(ITimeInterval interval, List<T> outdated);
	List<T> extractOverlapsAsList(ITimeInterval t);

	List<T> queryContains(PointInTime point);

	PointInTime getMaxStartTs();
	PointInTime getMinStartTs();
	PointInTime getMaxEndTs();

	@Override
	ITimeIntervalSweepArea<T> clone();

	/**
	 * For Debug-Purposes
	 *
	 * @param baseTime
	 * @return the current Content of the SweepArea with baseTime as origin of
	 *         all points of time
	 */
	String getSweepAreaAsString(String tab, int max, boolean tail);



}
