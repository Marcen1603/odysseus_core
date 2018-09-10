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

package de.uniol.inf.is.odysseus.trajectory.compare.uots.data;

import java.util.List;

import com.vividsolutions.jts.geom.Point;

/**
 * An object which contains the necessary data structures for the <i>uots distance</i>.
 * It only consists of a <tt>List</tt> of <tt>Points</tt> that are part of a <tt>Graph's</tt>
 * <i>vertex set</i>.
 * 
 * @author marcus
 *
 */
public class UotsData {

	/** the <tt>List</tt> of <tt>Points</tt> that are part of a <tt>Graph's</tt> <i>vertex set</i> */
	private final List<Point> graphPoints;
	
	/**
	 * Creates an instance of <tt>UotsData</tt>.
	 * 
	 * @param graphPoints a <tt>List</tt> of <tt>Points</tt> that are part of a <tt>Graph's</tt> <i>vertex set</i>
	 * @throws IllegalArgumentException if <tt>graphPoints == null</tt>
	 */
	public UotsData(final List<Point> graphPoints) {
		if(graphPoints == null) {
			throw new IllegalArgumentException("graphPoints is null");
		}
		this.graphPoints = graphPoints;
	}

	/**
	 *  Returns the <tt>List</tt> of <tt>Points</tt> that are part of a <tt>Graph's</tt> <i>vertex set</i>.
	 *  
	 * @return the <tt>List</tt> of <tt>Points</tt> that are part of a <tt>Graph's</tt> <i>vertex set</i>
	 */
	public List<Point> getGraphPoints() {
		return this.graphPoints;
	}
}
