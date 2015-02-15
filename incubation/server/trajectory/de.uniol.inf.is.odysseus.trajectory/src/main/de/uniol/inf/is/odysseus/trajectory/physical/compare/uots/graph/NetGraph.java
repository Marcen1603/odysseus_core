package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.graph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.javatuples.Pair;
import org.javatuples.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class NetGraph {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(NetGraph.class);
	
	
	private final UndirectedSparseGraph<Point, LineSegment> complexGraph;
	
	private final UndirectedSparseGraph<Point, Unit<Double>> reducedGraph;
	
	private final double diagonalLength;
		
	private Map<Point, Point> pointToJunctions = new HashMap<>();
	
	public NetGraph(UndirectedSparseGraph<Point, LineSegment> complexGraph) {
		this.complexGraph = complexGraph;
		this.keepHighestIsolatedGraph();
		this.reducedGraph = new UndirectedSparseGraph<>();		
		this.diagonalLength = this.buildReducedGraph();
	}
	
	/**
	 * 
	 */
	private final void keepHighestIsolatedGraph() {
		final Set<Point> visited = new HashSet<>();
		final PriorityQueue<Set<Point>> sets = new PriorityQueue<Set<Point>>(10, new Comparator<Set<Point>>() {
			@Override
			public int compare(Set<Point> o1, Set<Point> o2) {
				return o1.size() - o2.size();
			}
		});
		
		for(final Point point : this.complexGraph.getVertices()) {
			if(!visited.contains(point)) {
				sets.add(this.createSubset(point, visited));
			}
		}
		
		while(sets.size() > 1) {
			for(final Point point : sets.poll()) {
				this.complexGraph.removeVertex(point);
			}
		}
		
		LOGGER.info("Keep only largest subgraph with vertex count: " + this.complexGraph.getVertexCount());
	}
	
	private final Set<Point> createSubset(final Point point, final Set<Point> visited) { 
		final HashSet<Point> subgraphVisited = new HashSet<>();
		this.createSubset(point, subgraphVisited, visited);
		return subgraphVisited;
	}
		
	private final void createSubset(final Point point, final Set<Point> subgraphVisited, final Set<Point> visited) {
		visited.add(point);
		subgraphVisited.add(point);
		
		for(final Point neighbor : this.complexGraph.getNeighbors(point)) {
			if(!subgraphVisited.contains(neighbor)) {
				this.createSubset(neighbor, subgraphVisited, visited);
			}
		}
	}
	
	
	/**
	 * 
	 */
	private final double buildReducedGraph() {
		
	    for(final LineSegment ls : this.complexGraph.getEdges()) {
	        this.reducedGraph.addEdge(new Unit<>(ls.getLength()), this.complexGraph.getEndpoints(ls), EdgeType.UNDIRECTED);
	    }
	    
		for(final Point point : this.complexGraph.getVertices()) {
			if(this.reducedGraph.getNeighborCount(point) == 2) {

				final Iterator<Unit<Double>> it = this.reducedGraph.getIncidentEdges(point).iterator();
				final Unit<Double> edge1 = it.next();
				final Unit<Double> edge2 = it.next();
			
				final Point point1 = this.reducedGraph.getOpposite(point, edge1);
				final Point point2 = this.reducedGraph.getOpposite(point, edge2);
				
				if(this.reducedGraph.findEdge(point1, point2) == null) {
					Pair<Point, Double> p1 = this.findJunction(point, edge1, edge1.getValue0(), Double.MAX_VALUE);
					final Pair<Point, Double> p2 = this.findJunction(point, edge2, edge2.getValue0(), p1.getValue1());
					if(p2 != null) {
						p1 = p2;
					}
					
					this.pointToJunctions.put(point, p1.getValue0());	
					
					this.reducedGraph.removeVertex(point);
					
					this.reducedGraph.addEdge(new Unit<>(edge1.getValue0() + edge2.getValue0()), point1, point2, EdgeType.UNDIRECTED);
				}
			} 
		}

		LOGGER.info("Simple Graph created: reduced vertex count from " + this.complexGraph.getVertexCount() 
				+ " to " + this.reducedGraph.getVertexCount());
		
		
		final Iterator<Point> it = this.reducedGraph.getVertices().iterator();
		Point point = it.next();
		
		double maxLeft = point.getX();
		double maxRight = maxLeft;
		double maxTop = point.getY();
		double maxBottom = maxTop;
		
		while(it.hasNext()) {
			final Point next = it.next();
			
			if(next.getX() < maxLeft) {
				maxLeft = next.getX();
			} else if(next.getX() > maxRight) {
				maxRight = next.getX();
			}
			
			if(next.getY() < maxBottom) {
				maxBottom = next.getY();
			} else if(next.getY() > maxTop) {
				maxTop = next.getY();
			}
		}
		
		final GeometryFactory gf = new GeometryFactory();
		
		return gf.createPoint(new Coordinate(maxLeft, maxTop)).distance(gf.createPoint(new Coordinate(maxRight, maxBottom)));
	}
	
	private Pair<Point, Double> findJunction(Point point, Unit<Double> edge, double distance, double maxDistance) {
	
		if(distance > maxDistance) {
			return null;
		}
		
		final Point nextPoint = this.reducedGraph.getOpposite(point, edge);
		
		if(this.reducedGraph.getNeighborCount(nextPoint) != 2) {
			return new Pair<Point, Double>(nextPoint, distance);
		}
		
		final Iterator<Unit<Double>> it = this.reducedGraph.getOutEdges(nextPoint).iterator();
		Unit<Double> nextEdge = it.next();
		if(nextEdge == edge) {
			nextEdge = it.next();
		}
		return this.findJunction(nextPoint, nextEdge, distance + edge.getValue0(), maxDistance);
	}
	
	public Point getJunction(Point point) {
		Point junction = this.pointToJunctions.get(point);
		if(junction == null) {
			this.pointToJunctions.put(point, junction = point);
		}
		return junction;
	}

	public UndirectedSparseGraph<Point, LineSegment> getComplexGraph() {
		return this.complexGraph;
	}

	public UndirectedSparseGraph<Point, Unit<Double>> getReducedGraph() {
		return this.reducedGraph;
	}

	public double getDiagonalLength() {
		return this.diagonalLength;
	}
}
