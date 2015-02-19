package de.uniol.inf.is.odysseus.trajectory.compare.owd;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.trajectory.compare.AbstractTrajectoryCompareAlgoritm;
import de.uniol.inf.is.odysseus.trajectory.compare.ISpatialDistance;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.rasterization.AdvancedBresenhamRasterizer;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.rasterization.IRasterizer;


public class Owd extends AbstractTrajectoryCompareAlgoritm<IConvertedDataTrajectory<OwdData>, OwdData> {

	private final static Logger LOGGER = LoggerFactory.getLogger(Owd.class);
	
	private final static String GRID_CELL_SIZE_KEY = "gridcellsize";
	private final static String GRID_WIDTH_KEY = "gridwidth";
	private final static String GRID_HEIGHT_SIZE_KEY = "gridheight";
	
	private double gridCellSize;
	private double diagonalLength;
	
	private IRasterizer rasterizer;
	
	public Owd(Map<String, String> options, int k,
			RawQueryTrajectory queryTrajectory,
			Map<String, String> textualAttributes, int utmZone, double lambda) {
		
		super(options, k, queryTrajectory, textualAttributes, utmZone, lambda);
	}

	@Override
	protected IConvertedQueryTrajectory<OwdData> convert(RawQueryTrajectory trajectory,
			Map<String, String> textualAttributes, int utmZone,
			Map<String, String> options) {
		
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
	protected IConvertedDataTrajectory<OwdData> convert(RawDataTrajectory dataTrajectory) {
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