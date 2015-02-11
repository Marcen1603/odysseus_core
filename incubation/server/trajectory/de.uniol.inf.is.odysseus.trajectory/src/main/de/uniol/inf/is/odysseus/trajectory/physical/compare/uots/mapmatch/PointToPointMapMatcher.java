package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.mapmatch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.physical.compare.RawTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.UotsTrajectory;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class PointToPointMapMatcher implements IMapMatcher {

	private final static Logger LOGGER = LoggerFactory.getLogger(PointToPointMapMatcher.class);
	
	
	@Override
	public UotsTrajectory map(RawTrajectory trajectory,
			UndirectedSparseGraph<Point, LineSegment> graph) {
		
		//ersten finden
		final Iterator<Point> rawIt = trajectory.getPoints().iterator();
		Point rawPoint = rawIt.next();
		
		final Iterator<Point> graphVertexIt = graph.getVertices().iterator();
		Point minGraphPoint = graphVertexIt.next();
		double minDistance = rawPoint.distance(minGraphPoint);
		
		while(graphVertexIt.hasNext()) {
			final Point nextGraphPoint = graphVertexIt.next();
			final double nextDistance = rawPoint.distance(nextGraphPoint);
			if(nextDistance < minDistance) {
				minGraphPoint = nextGraphPoint;
				minDistance = nextDistance;
			}
		}
		
		final LinkedHashSet<Point> graphPoints = new LinkedHashSet<Point>();
		
		while(rawIt.hasNext()) {
			final Pair<Point, Double> result = 
					this.search(rawPoint, rawPoint = rawIt.next(), minGraphPoint, minDistance, graph);
			graphPoints.add(result.getValue0());
			minDistance = result.getValue1();
		}
		
		
		return new UotsTrajectory(trajectory, new ArrayList<>(graphPoints));
	}

	private Pair<Point, Double> search(Point lastRawPoint, Point rawPoint, Point lastGraphPoint, double currDistance, 
			UndirectedSparseGraph<Point, LineSegment> graph) {
		
		this.globalGraphPoint = null;
		this.globalMinDistance = Double.MAX_VALUE;
		
		this.search(rawPoint, 
				lastRawPoint,
				lastGraphPoint,
				currDistance,
				currDistance + rawPoint.distance(lastRawPoint),
				new HashSet<Point>(),
				graph);
		
		return new Pair<Point, Double>(this.globalGraphPoint, this.globalMinDistance);
	}
	
	private Point globalGraphPoint;
	private double globalMinDistance;
	
	private void search(Point lastRawPoint, Point rawPoint, Point currGraphPoint, double currDistance, double maxDistance, Set<Point> visitedGraphPoints,
			UndirectedSparseGraph<Point, LineSegment> graph) {
		
		if(visitedGraphPoints.contains(currGraphPoint)) {
			return;
		}
		if(currDistance > maxDistance) {
			return;
		}
		final double distance = rawPoint.distance(currGraphPoint);
		if(distance < this.globalMinDistance) {
			this.globalGraphPoint = currGraphPoint;
			this.globalMinDistance = distance;
		}
		
		visitedGraphPoints.add(currGraphPoint);
		
		for(final Point neigbor : graph.getNeighbors(currGraphPoint)) {
			this.search(rawPoint, lastRawPoint, neigbor, lastRawPoint.distance(neigbor) , maxDistance, visitedGraphPoints, graph);
		}
	}
}
