/*
 * Copyright 2015 Marcus Behrendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
**/

package de.uniol.inf.is.odysseus.trajectory.compare.uots.graph;

import java.io.Serializable;
import java.util.Collection;
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
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * A <tt>NetGraph</tt> encapsulated two instances of an <tt>UndirectedSparseGraph<tt>. 
 * One is a complex and the other one a reduced <tt>UndirectedSparseGraph<tt>. 
 * 
 * A <tt>NetGraph</tt> is created from a complex <tt>UndirectedSparseGraph</tt> with 
 * <tt>Points</tt> as <i>vertices</i> and <tt>LineSegments</tt> as <i>edges</i>. 
 * 
 * <p>Firstly the complex <tt>UndirectedSparseGraph</tt> will be processed in a way that only
 * the <i>largest set of connected vertices</i> will be kept. In this way it is guaranteed that
 * each vertex can be reached by another vertex.</p>
 * 
 * <p>Secondly a new <tt>UndirectedSparseGraph</tt> will be built from the complex 
 * <tt>UndirectedSparseGraph</tt> as the reduced <tt>UndirectedSparseGraph</tt>. Instead 
 * of <tt>LineSegments</tt> it will have <tt>Units</tt> of <tt>Double</tt> as <i>edges</i> 
 * which value are equal to the lengths of the <tt>LineSegments</tt>.
 * After that each vertex of from the reduced graph that has exactly <i>two edges will be
 * removed</i>. For each removed vertex the nearest adjacent vertex with exeact one
 * or more than two edges will be stored.
 * 
 * @author marcus
 *
 */
public class NetGraph {
	
	/** Logger for debugging purposes */
	private final static Logger LOGGER = LoggerFactory.getLogger(NetGraph.class);
	
	/** used for comparing sizes of <tt>Collections</tt> */
	private final static CollectionSizeComparator COLLECTION_SIZE_COMPARATOR = 
			new CollectionSizeComparator();
	
	/** the complex <tt>UndirectedSparseGraph</tt> */
	private final UndirectedSparseGraph<Point, LineSegment> complexGraph;
	
	/** the reduced <tt>UndirectedSparseGraph</tt> */
	private final UndirectedSparseGraph<Point, Unit<Double>> reducedGraph;
	
	/** the diagonal length of the <tt>NetGraph</tt> in <i>meters</i> */
	private final double diagonalLength;
	
	/** the bounds of the <tt>NetGraph</tt> in the <i>UTM Coordinate System</i> */
	private final Pair<Coordinate, Coordinate> bounds;
		
	/** <tt>Map</tt> for finding junctions for removed vertices  */
	private final Map<Point, Point> pointToJunctions = new HashMap<>();
	
	/**
	 * Creates an instance of <tt>NetGraph</tt>.
	 * 
	 * @param complexGraph the complex <tt>UndirectedSparseGraph</tt> 
	 *        from which this <tt>NetGraph</tt> is built
	 */
	public NetGraph(final UndirectedSparseGraph<Point, LineSegment> complexGraph) {
		this.complexGraph = complexGraph;
		this.keepHighestIsolatedGraph();
		this.reducedGraph = new UndirectedSparseGraph<>();		
		this.bounds = this.buildReducedGraph();
		this.diagonalLength = this.bounds.getValue0().distance(this.bounds.getValue1());
	}
	
	/**
	 * Keeps only the <i>largest set of connected vertices</i> for
	 * the complex <tt>UndirectedSparseGraph</tt>.
	 */
	private final void keepHighestIsolatedGraph() {
		final Set<Point> visited = new HashSet<>();
		final PriorityQueue<Set<Point>> sets = 
				new PriorityQueue<Set<Point>>(10, COLLECTION_SIZE_COMPARATOR);
		
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
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Keep only largest subgraph with vertex count: " + this.complexGraph.getVertexCount());
		}
	}
	
	/**
	 * Method for visiting transitive neighbors of a vertex.
	 * @param point point the starting vertex which's transitive neighbors will be visited
	 * @param visited a <tt>Set</tt> of already visited vertices of graph
	 * @return
	 */
	private final Set<Point> createSubset(final Point point, final Set<Point> visited) { 
		final HashSet<Point> subgraphVisited = new HashSet<>();
		this.createSubset(point, subgraphVisited, visited);
		return subgraphVisited;
	}
	
	/**
	 * Recursive method for visiting transitive neighbors of a vertex.
	 * 
	 * @param point the starting vertex which's transitive neighbors will be visited
	 * @param subgraphVisited a <tt>Set</tt> of already visited neighbors of the subgraph
	 * @param visited a <tt>Set</tt> of already visited vertices of graph
	 */
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
	 * Builds the reduced <tt>UndirectedSparseGraph</tt> from the complex <tt>UndirectedSparseGraph</tt>.
	 */
	private final Pair<Coordinate, Coordinate> buildReducedGraph() {
		
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
		
		final Iterator<Point> it = this.reducedGraph.getVertices().iterator();
		final Point point = it.next();
		
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
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Simple Graph created: reduced vertex count from " + this.complexGraph.getVertexCount() 
					+ " to " + this.reducedGraph.getVertexCount());
		}
		
				
		return new Pair<>(new Coordinate(maxLeft, maxBottom), new Coordinate(maxRight, maxTop));
	}
	
	/**
	 * Recursive method for finding a junction point of a removed vertex in one direction.
	 * 
	 * @param point the current vertex
	 * @param edge indicates in which direction shall be searched
	 * @param distance indicates how far has been searched
	 * @param maxDistance the maximal distance to search
	 * @return the junction
	 */
	private Pair<Point, Double> findJunction(final Point point, final Unit<Double> edge, final double distance, final double maxDistance) {
	
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
	
	/**
	 * Returns the nearest <tt>Point</tt> with exactly one or more than two edges to passed
	 * <tt>Point</tt>.
	 * 
	 * @param point the <tt>Point</tt> to find its nearest junction
	 * @return the nearest junction to the passed <tt>Point</tt>
	 */
	public Point getJunction(final Point point) {
		Point junction = this.pointToJunctions.get(point);
		if(junction == null) {
			this.pointToJunctions.put(point, junction = point);
		}
		return junction;
	}

	/**
	 * Returns the complex <tt>UndirectedSparseGraph</tt>.
	 * 
	 * @return the complex <tt>UndirectedSparseGraph</tt>
	 */
	public UndirectedSparseGraph<Point, LineSegment> getComplexGraph() {
		return this.complexGraph;
	}

	/**
	 * Returns the reduced <tt>UndirectedSparseGraph</tt>.
	 * 
	 * @return the reduced <tt>UndirectedSparseGraph</tt>
	 */
	public UndirectedSparseGraph<Point, Unit<Double>> getReducedGraph() {
		return this.reducedGraph;
	}
	
	/**<
	 * Returns the bounds of the <tt>NetGraph</tt> in the <i>UTM Coordinate System</i>.
	 * 
	 * @return the bounds of the <tt>NetGraph</tt> in the <i>UTM Coordinate System</i>
	 */
	public Pair<Coordinate, Coordinate> getBounds() {
		return this.bounds;
	}

	/**
	 * Returns the diagonal length of the <tt>NetGraph</tt> in <i>meters</i>.
	 * 
	 * @return the diagonal length of the <tt>NetGraph</tt> in <i>meters</i>
	 */
	public double getDiagonalLength() {
		return this.diagonalLength;
	}
	
	/**
	 * Special implementation of <tt>Comparator</tt> to compare the size of two <tt>Collections</tt>.
	 * This class is utilized two keep the <i>largest set of connected vertices</i> in the complex
	 * <tt>UndirectedSparseGraph</tt>.
	 * 
	 * @author marcus
	 *
	 */
	private static final class CollectionSizeComparator implements Comparator<Collection<?>>, Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 534559323981351379L;

		@Override
		public int compare(final Collection<?> o1, final Collection<?> o2) {
			return o1.size() - o2.size();
		}
	}
}
