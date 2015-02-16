package de.uniol.inf.is.odysseus.trajectory.compare.uots.mapmatch;

import java.util.Map;

import de.uniol.inf.is.odysseus.trajectory.compare.data.RawIdTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.data.UotsDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.data.UotsQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.graph.NetGraph;


public interface IMapMatcher {

	public UotsDataTrajectory map(final RawIdTrajectory trajectory, NetGraph graph);
	
	public UotsQueryTrajectory map(final RawQueryTrajectory trajectory, final Map<String, String> textualAttributes, NetGraph graph);
}
