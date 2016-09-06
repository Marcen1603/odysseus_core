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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.trajectory.compare.AbstractTrajectoryCompareAlgorithm;
import de.uniol.inf.is.odysseus.trajectory.compare.ISpatialDistance;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IConvertedQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.data.UotsData;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.graph.GraphBuilderFactory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.graph.NetGraph;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.mapmatch.IMapMatcher;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.mapmatch.MapMatcherFactory;

/**
 * Implementation of <tt>AbstractTrajectoryCompareAlgoritm</tt> which is set up to 
 * calculate UOTS distances.
 * 
 * @author marcus
 *
 */
public class Uots extends AbstractTrajectoryCompareAlgorithm<IConvertedDataTrajectory<UotsData>, UotsData> {
	
	/** Logger for debugging purposes */
	private final static Logger LOGGER = LoggerFactory.getLogger(Uots.class);
	
	private final static String MAP_FILE_KEY = "mapfile";
	private final static String MAP_MATCHER_KEY = "mapmatching";

	/** the <tt>NetGraph</tt> */
	private NetGraph graph;
	
	/** for map-matching */
	private IMapMatcher mapMatcher;

	/**
	 * Creates an instance of <tt>Uots</tt>.
	 * 
	 * @param options the options for the algorithm
	 * @param k the k-nearest trajectories to find
	 * @param queryTrajectory the raw query trajectory
	 * @param textualAttributes textual attributes of the query trajectory
	 * @param utmZone the UTM zone of the trajectories
	 * @param lambda the importance between spatial and textual distance
	 * @return a new <tt>ITrajectoryCompareAlgorithm</tt>
	 */
	public Uots(final Map<String, String> options, final int k,
			final RawQueryTrajectory queryTrajectory,
			final Map<String, String> textualAttributes, final int utmZone, final double lambda) {
		
		super(options, k, queryTrajectory, textualAttributes, utmZone, lambda);
	}
	

	@Override
	protected IConvertedQueryTrajectory<UotsData> convert(final RawQueryTrajectory queryTrajectory, final Map<String, String> textualAttributes,
			final int utmZone, final Map<String, String> options) {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Map query trajectory to graph points.");
		}
		this.graph = GraphBuilderFactory.getInstance().load(options.get(MAP_FILE_KEY), utmZone);
		this.mapMatcher = MapMatcherFactory.getInstance().create(options.get(MAP_MATCHER_KEY));
		return this.mapMatcher.map(queryTrajectory, textualAttributes, this.graph);
	}

	@Override
	protected IConvertedDataTrajectory<UotsData> convert(final RawDataTrajectory queryTrajectory) {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Map data trajectory to graph points.");
		}
		return this.mapMatcher.map(queryTrajectory, this.graph);
	}


	@Override
	protected ISpatialDistance<UotsData> createDistanceService() {
		return UotsDistance.getInstanceFor(this.graph);
	}
}
