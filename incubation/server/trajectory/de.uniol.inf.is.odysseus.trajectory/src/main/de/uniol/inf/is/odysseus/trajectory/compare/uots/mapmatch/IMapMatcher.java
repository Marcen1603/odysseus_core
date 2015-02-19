package de.uniol.inf.is.odysseus.trajectory.compare.uots.mapmatch;

import java.util.Map;

import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.data.UotsDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.data.UotsQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.graph.NetGraph;

/**
 * 
 * @author marcus
 *
 */
public interface IMapMatcher {

	public UotsDataTrajectory map(final RawDataTrajectory trajectory, final NetGraph graph);
	
	public UotsQueryTrajectory map(final RawQueryTrajectory trajectory, final Map<String, String> textualAttributes, final NetGraph graph);
}
