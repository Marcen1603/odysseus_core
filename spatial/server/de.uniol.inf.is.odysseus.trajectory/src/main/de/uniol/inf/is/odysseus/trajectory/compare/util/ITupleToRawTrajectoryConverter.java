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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;

/**
 * An object which can convert a <tt>Tuple</tt> that represents a <i>trajectory</i> to
 * a <tt>RawDataTrajectory</tt>. Implementations have to decide which schema to use
 * for a <tt>Tuple</tt>.
 * 
 * @author marcus
 *
 */
public interface ITupleToRawTrajectoryConverter {

	/**
	 * Converts and returns a <tt>RawDataTrajectory</tt> from a <tt>Tuple</tt> 
	 * representing a <i>trajectory</i> within the passed <tt>utmZone</tt>. 
	 * 
	 * @param tuple the <tt>Tuple</tt> representing a <i>trajectory</i>
	 * @param utmZone the <tt>utm zone</tt> to in which the to be created <tt>RawDataTrajectory</tt>
	 *        is supposed to be
	 * 
	 * @return a <tt>RawDataTrajectory</tt> from the passed <tt>Tuple</tt> 
	 *         representing a <i>trajectory</i>
	 *         
	 * @throws IllegalArgumentException if <tt>tuple == null</tt> or <tt>utmZone</tt> is
	 *         out of bounds
	 */
	public RawDataTrajectory convert(final Tuple<ITimeInterval> tuple, final int utmZone) throws IllegalArgumentException;
}
