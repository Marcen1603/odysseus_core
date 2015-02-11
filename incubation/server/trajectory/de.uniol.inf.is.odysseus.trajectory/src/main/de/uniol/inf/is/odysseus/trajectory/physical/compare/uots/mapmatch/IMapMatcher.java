package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.mapmatch;

import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.physical.compare.RawTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.UotsTrajectory;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;


public interface IMapMatcher {

	
	public UotsTrajectory map(final RawTrajectory trajectory, UndirectedSparseGraph<Point, LineSegment> graph);
}
