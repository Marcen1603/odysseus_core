package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.graph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.util.ObjectWrapper;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class NetGraph {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(NetGraph.class);
	
	
	private final UndirectedSparseGraph<Point, LineSegment> complexGraph;
	
	private final UndirectedSparseGraph<Point, ObjectWrapper<Double>> simpleGraph;
	
	
	public NetGraph(UndirectedSparseGraph<Point, LineSegment> complexGraph) {
		this.complexGraph = complexGraph;
		this.simpleGraph = new UndirectedSparseGraph<>();		
	    this.reduce();
	}
	
	private Map<Point, Point> pointToJunctions = new HashMap<>();
	
	private final void reduce() {
	    for(final LineSegment ls : this.complexGraph.getEdges()) {
	        this.simpleGraph.addEdge(new ObjectWrapper<>(ls.getLength()), this.complexGraph.getEndpoints(ls), EdgeType.UNDIRECTED);
	    }
  
		for(final Point point : this.complexGraph.getVertices()) {
			if(this.simpleGraph.getNeighborCount(point) == 2) {

				final Iterator<ObjectWrapper<Double>> it = this.simpleGraph.getOutEdges(point).iterator();
				final ObjectWrapper<Double> edge1 = it.next();
				final ObjectWrapper<Double> edge2 = it.next();
					
				try {
					Pair<Point, Double> p1 = this.findJunction(point, point, edge1, edge1.getWrapped(), Double.MAX_VALUE);
					Pair<Point, Double> p2 = this.findJunction(point, point, edge2, edge2.getWrapped(), p1.getValue1());
					if(p2 == null) {
						p2 = p1;
					}
					this.pointToJunctions.put(point, p2.getValue0());
				} catch(Exception e) {
					this.pointToJunctions.put(point, point);
					continue;
				}
				
				final Point point1 = this.simpleGraph.getOpposite(point, edge1);
				final Point point2 = this.simpleGraph.getOpposite(point, edge2);
				
				this.simpleGraph.removeVertex(point);
				this.simpleGraph.addEdge(new ObjectWrapper<>(edge1.getWrapped() + edge2.getWrapped()), point1, point2, EdgeType.UNDIRECTED);
			}
		}
		
		LOGGER.info("Simple Graph created: reduced vertex count from " + this.complexGraph.getVertexCount() 
				+ " to " + this.simpleGraph.getVertexCount());
	}
	
	private Pair<Point, Double> findJunction(Point start, Point point, ObjectWrapper<Double> edge, double distance, double maxDistance) 
			throws Exception {
		
		if(distance > maxDistance) {
			return null;
		}

		final Point nextPoint = this.simpleGraph.getOpposite(point, edge);
		
		if(nextPoint == start) {
			throw new Exception("cycle");
		}
		
		if(this.simpleGraph.getNeighborCount(nextPoint) != 2) {
			return new Pair<>(point, distance);
		}
		
		final Iterator<ObjectWrapper<Double>> it = this.simpleGraph.getOutEdges(nextPoint).iterator();
		ObjectWrapper<Double> nextEdge = it.next();
		if(nextEdge == edge) {
			nextEdge = it.next();
		}
		return this.findJunction(start, nextPoint, nextEdge, distance + edge.getWrapped(), maxDistance);
	}
	
	public Point getJunction(Point point) {
		final Point junction = this.pointToJunctions.get(point);
		return junction != null ? junction : point;
	}

	public UndirectedSparseGraph<Point, LineSegment> getComplexGraph() {
		return this.complexGraph;
	}

	public UndirectedSparseGraph<Point, ObjectWrapper<Double>> getSimpleGraph() {
		return this.simpleGraph;
	}
	
	
}
