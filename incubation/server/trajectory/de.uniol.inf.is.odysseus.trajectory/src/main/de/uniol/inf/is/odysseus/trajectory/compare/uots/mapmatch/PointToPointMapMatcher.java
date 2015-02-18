package de.uniol.inf.is.odysseus.trajectory.compare.uots.mapmatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.index.strtree.GeometryItemDistance;
import com.vividsolutions.jts.index.strtree.ItemDistance;
import com.vividsolutions.jts.index.strtree.STRtree;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IRawTrajectory;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class PointToPointMapMatcher extends AbstractMapMatcher {

	private final static Logger LOGGER = LoggerFactory.getLogger(PointToPointMapMatcher.class);

	private final static PointToPointMapMatcher INSTANCE = new PointToPointMapMatcher();
	
	public static PointToPointMapMatcher getInstance() {
		return INSTANCE;
	}
	
	private static final ItemDistance ITEM_DISTANCE = new GeometryItemDistance();
	
	
	private volatile Map<UndirectedSparseGraph<Point, LineSegment>, STRtree> strTrees = new ConcurrentHashMap<>();
	
	
	private PointToPointMapMatcher() {}

	
	@Override
	protected List<Point> getGraphPoints(final IRawTrajectory trajectory, final UndirectedSparseGraph<Point, LineSegment> graph) {

		STRtree strTree = this.strTrees.get(graph);
		if(strTree == null) {
			synchronized (graph) {
				strTree = this.strTrees.get(graph);
				if(strTree == null) {
					strTree = new STRtree();
		
					for(final Point graphPoint : graph.getVertices()) {
						strTree.insert(graphPoint.getEnvelopeInternal(), graphPoint);
					}
			
					strTree.build();
					this.strTrees.put(graph, strTree);
			
					if(LOGGER.isDebugEnabled()) {
						LOGGER.debug("New STRtree build for Graph " + graph.hashCode());
					}
				}
			}
		}

		final LinkedHashSet<Point> graphPoints = new LinkedHashSet<Point>();

		for(final Point point : trajectory.getPoints()) {
			graphPoints.add((Point)strTree.nearestNeighbour(point.getEnvelopeInternal(), point, ITEM_DISTANCE));
		}

		return new ArrayList<Point>(graphPoints);
	}
}
