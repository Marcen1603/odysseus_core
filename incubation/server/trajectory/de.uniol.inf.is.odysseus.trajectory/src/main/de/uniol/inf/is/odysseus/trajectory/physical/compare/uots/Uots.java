package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import java.util.Map;

import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.ITrajectoryCompareAlgorithm;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.ITupleToRawTrajectoryConverter;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.RawTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.TupleToRawTrajectoryConverterFactory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.graph.GraphBuilderFactory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.mapmatch.IMapMatcher;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.mapmatch.MapMatcherFactory;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class Uots implements ITrajectoryCompareAlgorithm {
	
	private final static String MAP_FILE_KEY = "mapfile";
	private final static String MAP_MATCHER_KEY = "mapmatching";
	

	private final int k;
	
	private final UotsTrajectory queryTrajectory;
	
	private final int utmZone;
	
	private final UndirectedSparseGraph<Point, LineSegment> graph;
	
	private final ITupleToRawTrajectoryConverter tupleToRawTrajectoryConverter;
	private final IMapMatcher mapMatcher;
	private final IDistanceService distanceService;
	
	public Uots(final int k, final RawTrajectory queryTrajectory, final int utmZone, final Map<String, String> options) {
		this.k = k;
		
		this.utmZone = utmZone;
		
		this.tupleToRawTrajectoryConverter = TupleToRawTrajectoryConverterFactory.getInstance().create();
		
		// Get the options
		this.graph = GraphBuilderFactory.getInstance().load(options.get(MAP_FILE_KEY), utmZone);
		this.mapMatcher = MapMatcherFactory.getInstance().create(options.get(MAP_MATCHER_KEY));
		
		this.queryTrajectory = this.mapMatcher.map(queryTrajectory, graph);
		
		this.distanceService = DistanceServiceFactory.getInstance().create(this.graph);
	}

	@Override
	public Tuple<ITimeInterval> getKNearest(Tuple<ITimeInterval> incoming) {
		
		final RawTrajectory t = this.tupleToRawTrajectoryConverter.convert(incoming, this.utmZone);
		final UotsTrajectory uotsTrajectory = this.mapMatcher.map(t, this.graph);
		
		double distance = 0;//this.distanceService.getDistance(this.queryTrajectory, uotsTrajectory, Double.MAX_VALUE);
		
		Tuple<ITimeInterval> result = new Tuple<ITimeInterval>(
					new Object[] {
							uotsTrajectory.getRawTrajectory().getId(),
							1,
							1,
							1,
							new Object[] {
									"Hallo" + distance, "werner"
							}
					},
					true
				);
		return result;
	}
	
	@Override
	public int getK() {
		return this.k;
	}
}
