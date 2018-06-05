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

import de.uniol.inf.is.odysseus.trajectory.compare.data.IRawTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData.GridCellList;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdQueryTrajectory;

/**
 * Abstract base implementation of an <tt>IRasterizer</tt>. Subclasses only need to 
 * rasterize the lines between the points of an <tt>IRawTrajectory</tt> based on the
 * passed <i>cellSize</i>.
 * 
 * @author marcus
 *
 */
public abstract class AbstractRasterizer implements IRasterizer {

	@Override
	public OwdDataTrajectory rasterize(final RawDataTrajectory trajectory, final double cellSize) throws IllegalArgumentException {
		if(trajectory == null) {
			throw new IllegalArgumentException("trajectory is null");
		}
		if(cellSize <= 0) {
			throw new IllegalArgumentException("cellSize is less or equal 0");
		}
		return new OwdDataTrajectory(trajectory,
				new OwdData(this.getGraphPoints(trajectory, cellSize)));
	}

	@Override
	public OwdQueryTrajectory rasterize(final RawQueryTrajectory trajectory, final Map<String, String> textualAttributes, final double cellSize) 
			throws IllegalArgumentException {
		if(trajectory == null) {
			throw new IllegalArgumentException("trajectory is null");
		}
		if(cellSize <= 0) {
			throw new IllegalArgumentException("cellSize is less or equal 0");
		}
		return new OwdQueryTrajectory(trajectory, new OwdData(this.getGraphPoints(trajectory, cellSize)), textualAttributes);
	}
	
	/**
	 * Creates and returns a <tt>GridCellList</tt> by rasterizing the passed <tt>IRawTrajectory</tt>.
	 * 
	 * @param trajectory the <tt>IRawTrajectory</tt> which's raw point will be rasterized
	 * @param cellSize the width and height of a cell in meters
	 * @return a <tt>GridCellList</tt> by rasterizing the passed <tt>IRawTrajectory</tt>
	 * @throws IllegalArgumentException if <tt>trajectory == null</tt> or <tt>cellSize <= 0</tt>
	 */
	protected abstract GridCellList getGraphPoints(final IRawTrajectory trajectory, final double cellSize) throws IllegalArgumentException;
}
