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

package de.uniol.inf.is.odysseus.trajectory.compare.util;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

/**
 * Interface for converting a <tt>Coordinate</tt> represented in <i>EPSG:4326</i> spatial format
 * to an <tt>Point</tt> in a coordinate system specified by the implementation.
 * 
 * @author marcus
 *
 */
public interface IPointCreator {

	/**
	 * Creates and returns a <tt>Point</tt> from the passed
	 * <tt>Coordinate</tt> in <i>EPSG:4326</i> spatial format.
	 * 
	 * @param coordinate the <tt>Coordinate</tt> in <i>EPSG:4326</i> spatial format
	 * @return the Point in a specified coordinate system from the passed <tt>Coordinate</tt>
	 * @throws IllegalArgumentException if <tt>coordinate == null</tt>
	 */
	public Point createPoint(final Coordinate coordinate) throws IllegalArgumentException;
}
