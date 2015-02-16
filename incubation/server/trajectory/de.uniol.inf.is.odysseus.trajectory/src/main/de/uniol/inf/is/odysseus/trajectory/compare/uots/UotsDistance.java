package de.uniol.inf.is.odysseus.trajectory.compare.uots;

import java.util.Map;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.map.HashedMap;
import org.javatuples.Unit;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.compare.ISpatialDistance;
import de.uniol.inf.is.odysseus.trajectory.compare.data.ITrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.data.UotsData;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.graph.NetGraph;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraDistance;

public class UotsDistance implements ISpatialDistance<UotsData> {

	
	private final static Map<NetGraph, UotsDistance> INSTANCES = new HashedMap<>();
	
	public static final UotsDistance getInstanceFor(NetGraph graph) {
		UotsDistance instance = INSTANCES.get(graph);
		if(instance == null) {
			INSTANCES.put(graph, instance = new UotsDistance(graph));
		}
		return instance;
	}
	
	
	private final DijkstraDistance<Point, Unit<Double>> dijkstraDistance;
	
	private UotsDistance(final NetGraph graph) {
		this.dijkstraDistance = new DijkstraDistance<>(graph.getReducedGraph(), new Transformer<Unit<Double>, Number>() {
			@Override
			public Number transform(Unit<Double> arg0) {
				return arg0.getValue0() / graph.getDiagonalLength();
			}
		}, true);
	}
	
	@Override
	public double getDistance(ITrajectory<UotsData, ?> queryTrajectory, ITrajectory<UotsData, ?> dataTrajectory) {
		double distance = 0;
		for(final Point ec : queryTrajectory.getData().getGraphPoints()) {
			double minDistance = Double.MAX_VALUE;
			for(final Point point : dataTrajectory.getData().getGraphPoints()) {
				double nextDistance = this.dijkstraDistance.getDistance(ec, point).doubleValue();
				if(nextDistance < minDistance) {
					minDistance = nextDistance;
				}
			}
			distance += minDistance;
		}
		return distance;
	}
}
