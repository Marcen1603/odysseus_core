package de.uniol.inf.is.odysseus.trajectory.compare.uots.mapmatch;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IRawTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawIdTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.data.UotsData;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.data.UotsDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.data.UotsQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.graph.NetGraph;

public abstract class AbstractMapMatcher implements IMapMatcher {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractMapMatcher.class);
	
	protected AbstractMapMatcher() {}
	
	@Override
	public UotsDataTrajectory map(RawIdTrajectory trajectory, NetGraph graph) {
		return new UotsDataTrajectory(
				trajectory, 
				new UotsData(
						this.extractJunctions(this.getGraphPoints(trajectory, graph), graph)));
	}
	
	
	@Override
	public final UotsQueryTrajectory map(RawQueryTrajectory trajectory, final Map<String, String> textualAttributes, NetGraph graph) {
		return new UotsQueryTrajectory(
				trajectory,
				new UotsData(this.extractJunctions(this.getGraphPoints(trajectory, graph), graph)),
				textualAttributes
				);
	}
	
	private final List<Point> extractJunctions(List<Point> points, NetGraph graph) {
		final LinkedHashSet<Point> result = new LinkedHashSet<>();
		
		for(final Point p : points) {
			result.add(graph.getJunction(p));
		}
		
		return new ArrayList<Point>(result);
	}
	
	
	protected abstract List<Point> getGraphPoints(IRawTrajectory trajectory, NetGraph graph);
}
