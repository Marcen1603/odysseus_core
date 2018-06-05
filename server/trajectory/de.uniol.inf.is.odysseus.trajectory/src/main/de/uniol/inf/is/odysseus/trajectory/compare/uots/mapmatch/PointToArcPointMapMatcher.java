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

package de.uniol.inf.is.odysseus.trajectory.compare.uots.mapmatch;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.index.strtree.ItemBoundable;
import com.vividsolutions.jts.index.strtree.ItemDistance;
import com.vividsolutions.jts.index.strtree.STRtree;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IRawTrajectory;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * An implementation of <tt>AbstractMapMatcher</tt>. Trajectories raw points
 * are firstly associated with their <i>nearest graph edges in Euclidean space</i>
 * and secondly mapped to the <i>nearest end point of the associated graph edge</i>.
 * 
 * @author marcus
 *
 */
public class PointToArcPointMapMatcher extends AbstractMapMatcher {
	
	/** Logger for debugging purposes */
	private final static Logger LOGGER = LoggerFactory.getLogger(PointToArcPointMapMatcher.class);

	/** the singleton instance */
	private final static PointToArcPointMapMatcher INSTANCE = new PointToArcPointMapMatcher();
	
	/**
	 * Returns the <tt>PointToArcPointMapMatcher</tt> as an eager singleton.
	 * 
	 * @return the <tt>PointToArcPointMapMatcher</tt> as an eager singleton
	 */
	public static PointToArcPointMapMatcher getInstance() {
		return INSTANCE;
	}
	
	/**
	 * the distance function how two measure the distance between a <tt>Point</tt> and a <tt>LineSegment</tt>
	 * in an <tt>STRtree</tt>
	 */
	private static final ItemDistance ITEM_DISTANCE = new ItemDistance() {
		
		@Override
		public double distance(final ItemBoundable item1, final ItemBoundable item2) {
			return ((LineSegment)item1.getItem()).distance(((Point)item2.getItem()).getCoordinate());
		}
	};
	
	/**
	 * For each instance of an <tt>UndirectedSparseGraph</tt> an <tt>STRtree</tt> will be hold
	 */
	private final Map<UndirectedSparseGraph<Point, LineSegment>, STRtree> strTrees = new ConcurrentHashMap<>();
	
	/**
	 * Beware this class from being instantiated because it is a <i>singleton</i>.
	 */
	private PointToArcPointMapMatcher() { }
	
	/**
	 * {@inheritDoc}
	 * <p>A trajectory's raw point is firstly associated <i>with its nearest edge</i> in the graph and
	 * secondly mapped to the <i>nearest end point of the associated graph edge</i>. Here the
	 * distance function is applied in Euclidean space.</p>
	 * 
	 */
	@Override
	protected List<Point> getGraphPoints(final IRawTrajectory trajectory, final UndirectedSparseGraph<Point, LineSegment> graph) {
		
		STRtree strTree = this.strTrees.get(graph);
		if(strTree == null) {
			synchronized(graph) {
				if((strTree = this.strTrees.get(graph)) == null) {
					this.strTrees.put(graph, strTree = new STRtree());
				
					for(final LineSegment ls : graph.getEdges()) {
						strTree.insert(new Envelope(ls.getCoordinate(0), ls.getCoordinate(1)), ls);
					}
					strTree.build();
				
					if(LOGGER.isDebugEnabled()) {
						LOGGER.debug("New STRtree build for Graph " + graph.hashCode());
					}
				}
			}
		}
		
		final LinkedHashSet<Point> graphPoints = new LinkedHashSet<Point>();

		for(final Point point : trajectory.getPoints()) {
			final Pair<Point> endpoints = graph.getEndpoints(
							(LineSegment)strTree.nearestNeighbour(point.getEnvelopeInternal(), 
							point, 
							ITEM_DISTANCE)
			);
			
			graphPoints.add(
					point.distance(endpoints.getFirst()) < point.distance(endpoints.getSecond()) ? 
							endpoints.getFirst() : endpoints.getSecond());
		}
		
		return new ArrayList<>(graphPoints);
	}
}
