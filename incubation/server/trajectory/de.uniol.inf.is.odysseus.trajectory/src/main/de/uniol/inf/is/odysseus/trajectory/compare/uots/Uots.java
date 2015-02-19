package de.uniol.inf.is.odysseus.trajectory.compare.uots;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.trajectory.compare.AbstractTrajectoryCompareAlgoritm;
import de.uniol.inf.is.odysseus.trajectory.compare.ISpatialDistance;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.IQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawIdTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.data.UotsData;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.graph.GraphBuilderFactory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.graph.NetGraph;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.mapmatch.IMapMatcher;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.mapmatch.MapMatcherFactory;

public class Uots extends AbstractTrajectoryCompareAlgoritm<IDataTrajectory<UotsData>, UotsData> {
	

	private final static Logger LOGGER = LoggerFactory.getLogger(Uots.class);
	
	private final static String MAP_FILE_KEY = "mapfile";
	private final static String MAP_MATCHER_KEY = "mapmatching";

	private NetGraph graph;
	private IMapMatcher mapMatcher;
		
	public Uots(Map<String, String> options, int k,
			RawQueryTrajectory queryTrajectory,
			Map<String, String> textualAttributes, int utmZone, double lambda) {
		
		super(options, k, queryTrajectory, textualAttributes, utmZone, lambda);
	}
	

	@Override
	protected IQueryTrajectory<UotsData> convert(RawQueryTrajectory queryTrajectory, final Map<String, String> textualAttributes,
			int utmZone, final Map<String, String> options) {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Map query trajectory to graph points.");
		}
		this.graph = GraphBuilderFactory.getInstance().load(options.get(MAP_FILE_KEY), utmZone);
		this.mapMatcher = MapMatcherFactory.getInstance().create(options.get(MAP_MATCHER_KEY));
		return this.mapMatcher.map(queryTrajectory, textualAttributes, graph);
	}

	@Override
	protected IDataTrajectory<UotsData> convert(RawIdTrajectory queryTrajectory) {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Map data trajectory to graph points.");
		}
		return this.mapMatcher.map(queryTrajectory, graph);
	}


	@Override
	protected ISpatialDistance<UotsData> createDistanceService() {
		return UotsDistance.getInstanceFor(this.graph);
	}
}
