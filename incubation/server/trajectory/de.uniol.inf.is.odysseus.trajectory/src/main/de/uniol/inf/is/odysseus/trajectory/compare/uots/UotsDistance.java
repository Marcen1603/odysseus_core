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

package de.uniol.inf.is.odysseus.trajectory.compare.uots;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections15.Transformer;
import org.javatuples.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.compare.ISpatialDistance;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.data.UotsData;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.graph.NetGraph;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraDistance;

/**
 * An implementation of <tt>ISpatialDistance</tt> which calculates the distance 
 * between a </tt>UotsQueryTrajectory</tt> and a </tt>UotsDataTrajectory</tt> based
 * on the <i>UOTS distance function</i>
 * 
 * @author marcus
 *
 */
public class UotsDistance implements ISpatialDistance<UotsData> {

	/** for debugging */
	@SuppressWarnings("unused")
	private final static Logger LOGGER = LoggerFactory.getLogger(UotsDistance.class);
	
	/** the multiton instances */
	private volatile static Map<NetGraph, UotsDistance> INSTANCES = new ConcurrentHashMap<>();
	
	/**
	 * Returns the <tt>UotsDistance</tt> as a lazy singleton for the passed <i>graph</i>.
	 * 
	 * @return the <tt>UotsDistance</tt> as a lazy singleton for the passed <i>graph</i>
	 */
	public static final UotsDistance getInstanceFor(final NetGraph graph) {
		UotsDistance instance = INSTANCES.get(graph);
		if(instance == null) {
			synchronized(INSTANCES) {
				if((instance = INSTANCES.get(graph)) == null) {
					INSTANCES.put(graph, instance = new UotsDistance(graph));
				}
			}
		}
		return instance;
	}
	
	/** Transformer for getting edge weights */
	private final static GraphPathLengthTransformer TRANSFORMER = new GraphPathLengthTransformer();
	
	/** for finding shortest paths */
	private final DijkstraDistance<Point, Unit<Double>> dijkstraDistance;
	
	/** the diagonal length of the graph */
	private final double diagonalLength;

	/**
	 * Beware this class from being instantiated because it is a <i>multiton</i>.
	 */
	private UotsDistance(final NetGraph graph) {
		this.dijkstraDistance = new DijkstraDistance<>(graph.getReducedGraph(), TRANSFORMER, true);
		this.diagonalLength = graph.getDiagonalLength();
	}
	
	/**
	 * {@inheritDoc}
	 * Here the distance calculation between a </tt>UotsQueryTrajectory</tt> and a </tt>UotsDataTrajectory</tt> 
	 * based on the <i>UOTS distance function</i>
	 * 
	 */
	@Override
	public double getDistance(final IConvertedTrajectory<UotsData, ?> queryTrajectory, final IConvertedTrajectory<UotsData, ?> dataTrajectory) {
		double distance = 0;
		for(final Point ec : queryTrajectory.getData().getGraphPoints()) {
			double minDistance = Double.MAX_VALUE;
			for(final Point point : dataTrajectory.getData().getGraphPoints()) {
				final double nextDistance = this.dijkstraDistance.getDistance(ec, point).doubleValue();
				if(nextDistance < minDistance) {
					minDistance = nextDistance;
				}
			}
			distance += minDistance;
		}
		return (distance / queryTrajectory.getData().getGraphPoints().size()) / this.diagonalLength;
	}
	
	/**
	 * Class for transforming an edge to a <tt>Number</tt>.
	 * @author marcus
	 *
	 */
	private final static class GraphPathLengthTransformer implements Transformer<Unit<Double>, Number> {
		@Override
		public Number transform(final Unit<Double> arg0) {
			return arg0.getValue0();
		}
	}
}
