package de.uniol.inf.is.odysseus.trajectory.compare.owd.rasterization;

import java.util.Map;

import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdQueryTrajectory;

public interface IRasterizer {

	public OwdDataTrajectory rasterize(final RawDataTrajectory trajectory, final double cellSize);
	
	public OwdQueryTrajectory rasterize(final RawQueryTrajectory trajectory, final Map<String, String> textualAttributes, final double cellSize);
}
