/*
 *Copyright 2015 Marcus Behrendt
 * 
 \* Licensed under the Apache License, Version 2.0 (the "License");
 \* you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
**/package de.uniol.inf.is.odysseus.trajectory.compare.owd.rasterization;

import java.util.Map;

import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdQueryTrajectory;

/**
 * An object which converts a <tt>RawDataTrajectory</tt> or a <tt>RawQueryTrajectory</tt> to
 * an <tt>OwdDataTrajectory</tt> or an <tt>OwdQueryTrajectory</tt>, respectively by rasterizing
 * the lines between their raw points.
 * 
 * @author marcus
 *
 */
public interface IRasterizer {

	/**
	 * Rasterizes the lines between the raw points of the passed <tt>RawDataTrajectory</tt> based on the
	 * <tt>cellSize</tt>.
	 * 
	 * @param trajectory the <tt>RawDataTrajectory</tt> which's raw point will be rasterized
	 * @param cellSize the width and height of a cell in meters
	 * @return the resulting <tt>OwdDataTrajectory</tt> by rasterizing the points
	 * @throws IllegalArgumentException if <tt>trajectory == null</tt> or <tt>cellSize <= 0</tt>
	 */
	public OwdDataTrajectory rasterize(final RawDataTrajectory trajectory, final double cellSize) throws IllegalArgumentException;
	
	/**
	 * Rasterizes the lines between the raw points of the passed <tt>RawDataTrajectory</tt> based on the
	 * <tt>cellSize</tt>. Additionally, the textual attributes has to be passed since a 
	 * <tt>RawQueryTrajectory</tt> has none.
	 * 
	 * @param trajectory the <tt>RawDataTrajectory</tt> which's raw point will be rasterized
	 * @param textualAttributes the textual attributes the resulting <tt>OwdQueryTrajectory</tt> will contain
	 * @param cellSize the width and height of a cell in meters
	 * @return the resulting <tt>OwdDataTrajectory</tt> by rasterizing the points
	 * @throws IllegalArgumentException if <tt>trajectory == null</tt> or <tt>cellSize <= 0</tt>
	 */
	public OwdQueryTrajectory rasterize(final RawQueryTrajectory trajectory, final Map<String, String> textualAttributes, final double cellSize);
}
