package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.mapmatch;

import de.uniol.inf.is.odysseus.trajectory.physical.compare.RawDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.UotsQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.UotsTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.graph.NetGraph;


public interface IMapMatcher {

	
	public UotsTrajectory map(final RawDataTrajectory trajectory, NetGraph graph);
	public UotsQueryTrajectory map(final RawQueryTrajectory trajectory, NetGraph graph);
}
