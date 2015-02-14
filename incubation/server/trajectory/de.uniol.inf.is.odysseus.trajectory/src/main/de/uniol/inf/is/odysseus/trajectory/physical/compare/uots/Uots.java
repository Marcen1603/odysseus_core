package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.ITrajectoryCompareAlgorithm;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.RawDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.graph.GraphBuilderFactory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.graph.NetGraph;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.mapmatch.IMapMatcher;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.mapmatch.MapMatcherFactory;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class Uots implements ITrajectoryCompareAlgorithm<UotsTrajectory> {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(Uots.class);
	
	private final static String MAP_FILE_KEY = "mapfile";
	private final static String MAP_MATCHER_KEY = "mapmatching";
	

	private final int k;
	
	private final UotsQueryTrajectory queryTrajectory;
	
	private final NetGraph graph;
	
	private final IMapMatcher mapMatcher;
	private final IDistanceService distanceService;
	
	private final List<UotsTrajectory> trajectories = new LinkedList<>();
	
	
	
	public Uots(final int k, final RawQueryTrajectory queryTrajectory, final int utmZone, final Map<String, String> options) {
		this.k = k;
		
		// Get the options
		this.graph = GraphBuilderFactory.getInstance().load(options.get(MAP_FILE_KEY), utmZone);
		this.mapMatcher = MapMatcherFactory.getInstance().create(options.get(MAP_MATCHER_KEY));
		
		this.queryTrajectory = this.mapMatcher.map(queryTrajectory, graph);
		
		this.distanceService = DistanceServiceFactory.getInstance().create(this.graph);
		try {
			this.distanceService.addQueryTrajectory(this.queryTrajectory, this.k);
		} catch(IllegalArgumentException e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<UotsTrajectory> getKNearest(RawDataTrajectory incoming) {	
		final UotsTrajectory uotsTrajectory = this.mapMatcher.map(incoming, this.graph);
		return this.distanceService.getDistance(this.queryTrajectory, uotsTrajectory);
	}
	
	@Override
	public int getK() {
		return this.k;
	}

	@Override
	public void removeBefore(PointInTime time) {
		for(final UotsTrajectory traj : this.trajectories) {
			if(traj.getRawTrajectory().getInterval().getEnd().afterOrEquals(time)) {
				break;
			}
			this.distanceService.removeTrajectory(this.queryTrajectory, traj);
		}
	}
}
