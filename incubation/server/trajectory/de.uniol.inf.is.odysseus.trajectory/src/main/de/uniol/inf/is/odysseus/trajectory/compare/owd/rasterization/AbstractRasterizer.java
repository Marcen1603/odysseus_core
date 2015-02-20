package de.uniol.inf.is.odysseus.trajectory.compare.owd.rasterization;

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
	public OwdDataTrajectory rasterize(RawDataTrajectory trajectory, double cellSize) throws IllegalArgumentException {
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
	public OwdQueryTrajectory rasterize(RawQueryTrajectory trajectory, Map<String, String> textualAttributes, double cellSize) 
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
