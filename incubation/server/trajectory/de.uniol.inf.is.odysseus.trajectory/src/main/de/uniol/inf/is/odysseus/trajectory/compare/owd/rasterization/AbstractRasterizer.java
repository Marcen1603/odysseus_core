package de.uniol.inf.is.odysseus.trajectory.compare.owd.rasterization;

import java.util.Map;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IRawTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData.GridCellList;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdQueryTrajectory;

public abstract class AbstractRasterizer implements IRasterizer {

	@Override
	public OwdDataTrajectory rasterize(RawDataTrajectory trajectory, double cellSize) {
		return new OwdDataTrajectory(trajectory,
				new OwdData(this.getGraphPoints(trajectory, cellSize)));
	}

	@Override
	public OwdQueryTrajectory rasterize(RawQueryTrajectory trajectory, Map<String, String> textualAttributes, double cellSize) {
		return new OwdQueryTrajectory(trajectory, new OwdData(this.getGraphPoints(trajectory, cellSize)), textualAttributes);
	}
	
	protected abstract GridCellList getGraphPoints(final IRawTrajectory trajectory, double cellSize);
}
