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

import java.util.Collections;
import java.util.List;

import com.vividsolutions.jts.geom.Point;

/**
 * Default implementation of <tt>IRawTrajectory</tt>. The <tt>List</tt> of
 * <tt>Points</tt> is passed to constructor and internally transformed to a
 * <i>unmodifiable</i> <tt>List</tt>.
 * 
 * @author marcus
 *
 */
public class RawQueryTrajectory implements IRawTrajectory {

	/** the points list */
	private final List<Point> points;
	
	/**
	 * Creates an <tt>RawQueryTrajectory</tt>. A new <i>unmodifiable</i> <tt>List</tt>
	 * of <tt>Points</tt> is created from the passed <tt>List</tt>. An 
	 * <tt>IllegalArgumentException</tt> will be thrown if <i>points</i> is <tt>null</tt>
	 * or has not <i>at least one</i> element.
	 * 
	 * @param points the <tt>Points</tt> from which the internal <i>unmodifiable</i> 
	 *        <tt>List</tt> will be created
	 * 
	 * @throws IllegalArgumentException if <tt>points</tt> is <tt>null</tt>
	 *         or has not <i>at least one</i> element
	 * 
	 */
	public RawQueryTrajectory(final List<Point> points) {
		if(points == null) {
			throw new IllegalArgumentException("Argument points is null");
		}
		if(points.size() == 0) {
			throw new IllegalArgumentException("points must have more than 0 elements");
		}
		this.points = Collections.unmodifiableList(points);
	}

	/**
	 * {@inheritDoc}
	 * The returned <tt>List</tt> is <i>unmodifiable</i>.
	 * 
	 * @return {@inheritDoc} as an <i>unmodifiable</i> <tt>List</tt>
	 */
	@Override
	public List<Point> getPoints() {
		return this.points;
	}


}
