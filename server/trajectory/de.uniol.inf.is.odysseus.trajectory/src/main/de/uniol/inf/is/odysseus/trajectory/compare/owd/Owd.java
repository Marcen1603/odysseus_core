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

package de.uniol.inf.is.odysseus.trajectory.compare.owd;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.trajectory.compare.AbstractTrajectoryCompareAlgorithm;
import de.uniol.inf.is.odysseus.trajectory.compare.ISpatialDistance;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.rasterization.AdvancedBresenhamRasterizer;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.rasterization.IRasterizer;

/**
 * Implementation of <tt>AbstractTrajectoryCompareAlgoritm</tt> which is set up to 
 * calculate OWD distances.
 * 
 * @author marcus
 *
 */
public class Owd extends AbstractTrajectoryCompareAlgorithm<IConvertedDataTrajectory<OwdData>, OwdData> {

	/** Logger for debugging purposes */
	private final static Logger LOGGER = LoggerFactory.getLogger(Owd.class);
	
	private final static String GRID_CELL_SIZE_KEY = "gridcellsize";
	private final static String GRID_WIDTH_KEY = "gridwidth";
	private final static String GRID_HEIGHT_SIZE_KEY = "gridheight";
	
	/** the size of a <tt>GridCell</tt> */
	private double gridCellSize;
	
	/** the diagonal length of the grid */
	private double diagonalLength;
	
	/** for rasterizing trajectories */
	private IRasterizer rasterizer;
	
	/**
	 * Creates an instance of <tt>Owd</tt>.
	 * 
	 * @param options the options for the algorithm
	 * @param k the k-nearest trajectories to find
	 * @param queryTrajectory the raw query trajectory
	 * @param textualAttributes textual attributes of the query trajectory
	 * @param utmZone the UTM zone of the trajectories
	 * @param lambda the importance between spatial and textual distance
	 * @return a new <tt>ITrajectoryCompareAlgorithm</tt>
	 */
	public Owd(final Map<String, String> options, final int k,
			final RawQueryTrajectory queryTrajectory,
			final Map<String, String> textualAttributes, final int utmZone, final double lambda) {
		
		super(options, k, queryTrajectory, textualAttributes, utmZone, lambda);
	}

	@Override
	protected IConvertedQueryTrajectory<OwdData> convert(final RawQueryTrajectory trajectory,
			final Map<String, String> textualAttributes, final int utmZone,
			final Map<String, String> options) {
		
		this.gridCellSize = Double.parseDouble(options.get(GRID_CELL_SIZE_KEY));
		if(this.gridCellSize <= 0) {
			throw new IllegalArgumentException("gridCellSize must be positive.");
		}
		final double gridWidth = Double.parseDouble(options.get(GRID_WIDTH_KEY));
		if(gridWidth <= 0) {
			throw new IllegalArgumentException("gridWidth must be positive.");
		}
		final double gridHeight = Double.parseDouble(options.get(GRID_HEIGHT_SIZE_KEY));
		if(gridHeight <= 0) {
			throw new IllegalArgumentException("gridWidth must be positive.");
		}
		this.diagonalLength = Math.sqrt(Math.pow(gridWidth, 2) + Math.pow(gridHeight, 2));
		
		this.rasterizer = new AdvancedBresenhamRasterizer();
		return this.rasterizer.rasterize(trajectory, textualAttributes, this.gridCellSize);
	}

	@Override
	protected IConvertedDataTrajectory<OwdData> convert(final RawDataTrajectory dataTrajectory) {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Covert data trajectory to owd representation.");
		}
		return this.rasterizer.rasterize(dataTrajectory, this.gridCellSize);
	}

	@Override
	protected ISpatialDistance<OwdData> createDistanceService() {
		return new OwdDistance(this.gridCellSize, this.diagonalLength);
	}
}