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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IRawTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.data.UotsData;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.data.UotsDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.data.UotsQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.graph.NetGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

/**
 * Abstract base implementation of an <tt>IMapMatcher</tt>. Subclasses only need to 
 * map the points of an <tt>IRawTrajectory</tt> to the <i>complex graph's vertices</i> of a
 * <tt>Netgraph</tt>. These vertices then are mapped to the <i>nearest vertices</i> in
 * the <i>reduced graph</i>.
 * 
 * @author marcus
 *
 */
public abstract class AbstractMapMatcher implements IMapMatcher {

	/** Logger for debugging purposes */
	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractMapMatcher.class);
	
	/**
	 * Beware class from being instantiated.
	 */
	protected AbstractMapMatcher() {}
	
	@Override
	public final UotsDataTrajectory map(final RawDataTrajectory trajectory, final NetGraph graph) throws IllegalArgumentException {
		if(trajectory == null) {
			throw new IllegalArgumentException("trajectory is null.");
		}
		if(graph == null) {
			throw new IllegalArgumentException("graph is null.");
		}
		return new UotsDataTrajectory(
				trajectory, 
				new UotsData(
						this.getReducedGraphPoints(this.getGraphPoints(trajectory, graph.getComplexGraph()), graph)));
	}
	
	
	@Override
	public final UotsQueryTrajectory map(final RawQueryTrajectory trajectory, final Map<String, String> textualAttributes, final NetGraph graph) {
		if(trajectory == null) {
			throw new IllegalArgumentException("trajectory is null.");
		}
		if(graph == null) {
			throw new IllegalArgumentException("graph is null.");
		}
		return new UotsQueryTrajectory(
					trajectory,
					new UotsData(
							this.getReducedGraphPoints(
									this.getGraphPoints(trajectory, graph.getComplexGraph()), 
									graph)
					),
					textualAttributes
				);
	}
	
	/**
	 * Expects a <tt>List</tt> of points whose are mapped to vertices of a <tt>NetGraph's</tt> <i>complex graph</i> and
	 * returns the associated vertices in the <i>reduced graph</i>.
	 * 
	 * @param points the <tt>List</tt> of points whose are mapped to vertices of a <tt>NetGraph's</tt> <i>complex graph</i>
	 * @param graph the <tt>NetGraph</tt> which is utilized to associate the points of the <i>complex graph</i> with points
	 *        in the <i>reduced graph</i>
	 * @return the associated vertices in the <tt>NetGraph's</tt> <i>reduced graph</i>
	 * @throws IllegalArgumentException if <tt>points == null</tt>
	 */
	private List<Point> getReducedGraphPoints(final List<Point> points, final NetGraph graph) throws IllegalArgumentException {
		if(points == null) {
			throw new IllegalArgumentException("points is null.");
		}
		
		final LinkedHashSet<Point> result = new LinkedHashSet<>();
		
		for(final Point p : points) {
			result.add(graph.getJunction(p));
		}
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Graph points reduced to junctions.");
		}
		
		return new ArrayList<Point>(result);
	}
	
	/**
	 * Creates and returns a <tt>List</tt> of points that are mapped from the trajectory's <i>raw points</i>
	 * to <i>vertices</i> in the passed <tt>UndirectedSparseGraph</tt>.
	 * 
	 * @param trajectory the <tt>IRawTrajectory</tt> which raw points will be mapped to vertices in the graph
	 * @param graph the <tt>UndirectedSparseGraph</tt> to which's vertices get mapped by the raw points of the trajectory
	 * @return a <tt>List</tt> of points that are mapped from the trajectory's <i>raw points</i>
	 *         to <i>vertices</i> in the passed <tt>UndirectedSparseGraph</tt>
	 * @throws IllegalArgumentException if <tt>trajectory == null</tt> or <tt>graph == null</tt>
	 */
	protected abstract List<Point> getGraphPoints(final IRawTrajectory trajectory, final UndirectedSparseGraph<Point, LineSegment> graph) 
		throws IllegalArgumentException;
}
