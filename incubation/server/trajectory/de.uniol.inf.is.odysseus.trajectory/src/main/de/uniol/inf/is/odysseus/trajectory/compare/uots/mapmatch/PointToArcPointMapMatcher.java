package de.uniol.inf.is.odysseus.trajectory.compare.uots.mapmatch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.javatuples.Pair;

import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IRawTrajectory;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class PointToArcPointMapMatcher extends AbstractMapMatcher {
	
	@Override
	protected final List<Point> getGraphPoints(IRawTrajectory trajectory, UndirectedSparseGraph<Point, LineSegment> graph) {
		//ersten finden
		final Iterator<Point> rawIt = trajectory.getPoints().iterator();
		Point rawPoint = rawIt.next();
		
		final Iterator<LineSegment> graphEdgeIt = graph.getEdges().iterator();
		LineSegment minEdge = graphEdgeIt.next();
		double minDistance = minEdge.distance(rawPoint.getCoordinate());
		
		while(graphEdgeIt.hasNext()) {
			LineSegment nextGraphEdge = graphEdgeIt.next();
			final double nextDistance = nextGraphEdge.distance(rawPoint.getCoordinate());
			if(nextDistance < minDistance) {
				minEdge = nextGraphEdge;
				minDistance = nextDistance;
			}
		}
		
		edu.uci.ics.jung.graph.util.Pair<Point> pointPair = graph.getEndpoints(minEdge);
		Point minGraphPoint = rawPoint.distance(pointPair.getFirst()) < rawPoint.distance(pointPair.getSecond()) ?
				pointPair.getFirst() : pointPair.getSecond();
		
		final LinkedHashSet<Point> graphPoints = new LinkedHashSet<Point>();
		
		while(rawIt.hasNext()) {
			final Pair<Point, Double> result = 
					this.search(rawPoint, rawPoint = rawIt.next(), minGraphPoint, minDistance, graph);
			

			graphPoints.add(result.getValue0());
			minDistance = result.getValue1();
		}
		
		return new ArrayList<>(graphPoints);
	}

	private Pair<Point, Double> search(Point lastRawPoint, Point rawPoint, Point lastGraphPoint, double currDistance, 
			UndirectedSparseGraph<Point, LineSegment> graph) {	
		this.globalGraphEdge = null;
		this.globalMinDistance = Double.MAX_VALUE;
		
		this.search(rawPoint, 
				lastRawPoint,
				lastGraphPoint,
				currDistance,
				currDistance + rawPoint.distance(lastRawPoint),
				new HashSet<Point>(),
				new HashSet<LineSegment>(),
				graph);
		
		edu.uci.ics.jung.graph.util.Pair<Point> p = graph.getEndpoints(this.globalGraphEdge);
		
		final Point result = rawPoint.distance(p.getFirst()) < rawPoint.distance(p.getSecond()) ?
				p.getFirst() : p.getSecond();
		return new Pair<Point, Double>(result, this.globalMinDistance);
	}
	
	private LineSegment globalGraphEdge;
	private double globalMinDistance;
	
	private void search(Point lastRawPoint, Point rawPoint, Point currGraphPoint, double currDistance, double maxDistance, 
			Set<Point> visitedGraphPoints, Set<LineSegment> visitedGraphEdges,
			UndirectedSparseGraph<Point, LineSegment> graph) {
		
		if(visitedGraphPoints.contains(currGraphPoint)) {
			return;
		}
		if(currDistance > maxDistance) {
			return;
		}
		
		for(final LineSegment edge : graph.getOutEdges(currGraphPoint)) {
			if(!visitedGraphEdges.contains(edge)) {
				final double distance = edge.distance(rawPoint.getCoordinate());
				if(distance < this.globalMinDistance) {
					this.globalGraphEdge = edge;
					this.globalMinDistance = distance;
				}
				visitedGraphEdges.add(edge);
			}
		}
		
		visitedGraphPoints.add(currGraphPoint);
		
		for(final Point neigbor : graph.getNeighbors(currGraphPoint)) {
			this.search(rawPoint, 
					lastRawPoint,
					neigbor, 
					lastRawPoint.distance(neigbor) , 
					maxDistance, 
					visitedGraphPoints, 
					visitedGraphEdges, 
					graph);
		}
	}
}
