/*
 * Copyright 2015 Marcus Behrendt
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
 *
**/

package de.uniol.inf.is.odysseus.trajectory.compare.data;

import java.util.List;

import com.vividsolutions.jts.geom.Point;

/**
 * Interface for a trajectory which only consists of a <tt>List</tt> of raw
 * <tt>Points</tt> in <i>UTM-coordinates</i> . The points in the
 * <tt>List</tt> are <i>chronologically ordered</i> so that the measurement 
 * time of the first point is before the measurement time of the last point
 * in the list. The <tt>List</tt> must at least consist of <i>2</i> 
 * <tt>Points</tt>.
 * 
 * @author marcus
 *
 */
public interface IRawTrajectory {
	
	/**
	 * Returns the by <i>measurement time ascending chronologically ordered</i>
	 * <tt>Points</tt> of this trajectory. The <tt>Points</tt> are represented as
	 * the <iUTM-coordinates</i>.
	 * 
	 * @return the <tt>Points</tt> of this trajectory
	 */
	public List<Point> getPoints();
}
