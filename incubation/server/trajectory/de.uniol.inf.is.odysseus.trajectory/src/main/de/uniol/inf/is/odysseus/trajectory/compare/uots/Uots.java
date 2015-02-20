package de.uniol.inf.is.odysseus.trajectory.compare.uots;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.trajectory.compare.AbstractTrajectoryCompareAlgoritm;
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

public class Uots extends AbstractTrajectoryCompareAlgoritm<IConvertedDataTrajectory<UotsData>, UotsData> {
	

	private final static Logger LOGGER = LoggerFactory.getLogger(Uots.class);
	
	private final static String MAP_FILE_KEY = "mapfile";
	private final static String MAP_MATCHER_KEY = "mapmatching";

	private NetGraph graph;
	private IMapMatcher mapMatcher;
		
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
