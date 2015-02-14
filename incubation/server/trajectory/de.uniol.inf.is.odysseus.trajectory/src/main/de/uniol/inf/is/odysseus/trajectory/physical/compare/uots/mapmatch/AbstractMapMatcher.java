package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.mapmatch;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.physical.compare.IRawTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.RawDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.UotsQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.UotsQueryTrajectoryData;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.UotsTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.graph.NetGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public abstract class AbstractMapMatcher implements IMapMatcher {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractMapMatcher.class);
	
	
	@Override
	public final UotsTrajectory map(RawDataTrajectory trajectory, NetGraph graph) {
		UotsTrajectory t = new UotsTrajectory(
				trajectory, 
				this.extractJunctions(this.getGraphPoints(trajectory, graph.getComplexGraph()), graph));
		return t;
	}
	
	@Override
	public final UotsQueryTrajectory map(RawQueryTrajectory trajectory, NetGraph graph) {
		return new UotsQueryTrajectory(new UotsQueryTrajectoryData(
				this.extractJunctions(this.getGraphPoints(trajectory, graph.getComplexGraph()), graph)));
	}
	
	private List<Point> extractJunctions(List<Point> points, NetGraph graph) {
		LinkedHashSet<Point> result = new LinkedHashSet<>();
		
		for(final Point p : points) {
			result.add(graph.getJunction(p));
		}
		
		return new ArrayList<Point>(result);
	}
	
	
	protected abstract List<Point> getGraphPoints(IRawTrajectory trajectory, UndirectedSparseGraph<Point, LineSegment> graph);
}
