package de.uniol.inf.is.odysseus.trajectory.compare.uots.mapmatch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IRawTrajectory;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class PointToPointMapMatcher extends AbstractMapMatcher {

	private final static Logger LOGGER = LoggerFactory.getLogger(PointToPointMapMatcher.class);

	@Override
	protected List<Point> getGraphPoints(IRawTrajectory trajectory,
			UndirectedSparseGraph<Point, LineSegment> graph) {
		// ersten finden
		final Iterator<Point> rawIt = trajectory.getPoints().iterator();
		Point rawPoint = rawIt.next();

		final Iterator<Point> graphVertexIt = graph.getVertices().iterator();
		Point minGraphPoint = graphVertexIt.next();
		double minDistance = rawPoint.distance(minGraphPoint);

		while (graphVertexIt.hasNext()) {
			final Point nextGraphPoint = graphVertexIt.next();
			final double nextDistance = rawPoint.distance(nextGraphPoint);
			if (nextDistance < minDistance) {
				minGraphPoint = nextGraphPoint;
				minDistance = nextDistance;
			}
		}

		final LinkedHashSet<Point> graphPoints = new LinkedHashSet<Point>();

		while (rawIt.hasNext()) {
			graphPoints.add(minGraphPoint = this.search(rawPoint,
					rawPoint = rawIt.next(), minGraphPoint, graph));
			
			for(Point test : graph.getVertices()) {
				if(test.distance(rawPoint) < minGraphPoint.distance(rawPoint)) {
					if(test != minGraphPoint) {
						LOGGER.error("FukkdKDKkdkdkdk");
					}
				}
			}
		}
		
		
		return new ArrayList<Point>(graphPoints);
	}

	private Point search(Point lastRawPoint, Point rawPoint, Point lastGraphPoint,
			UndirectedSparseGraph<Point, LineSegment> graph) {
		
		this.globalGraphPoint = null;
		this.globalMinDistance = Double.MAX_VALUE;
		
		this.search(lastRawPoint, 
				rawPoint,
				lastGraphPoint,
				0,
				rawPoint.distance(lastGraphPoint) * 100,
				new HashSet<Point>(),
				graph);
		
		return this.globalGraphPoint;
	}
	
	private Point globalGraphPoint;
	private double globalMinDistance;
	
	private void search(Point lastRawPoint, Point rawPoint, Point currGraphPoint, double currDistance, double maxDistance, Set<Point> visitedGraphPoints,
			UndirectedSparseGraph<Point, LineSegment> graph) {
		
		visitedGraphPoints.add(currGraphPoint);

		final double distance = rawPoint.distance(currGraphPoint);
		if(distance < this.globalMinDistance) {
			this.globalGraphPoint = currGraphPoint;
			this.globalMinDistance = distance;
		}
		
		if(currDistance > maxDistance) {
			return;
		}
		
		for(final Point neigbor : graph.getNeighbors(currGraphPoint)) {
			if(!visitedGraphPoints.contains(neigbor)) {
				this.search(lastRawPoint, rawPoint, neigbor, lastRawPoint.distance(neigbor) , maxDistance, visitedGraphPoints, graph);
			}
		}
	}
}
