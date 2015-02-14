package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.apache.commons.collections15.Transformer;
import org.javatuples.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.graph.NetGraph;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraDistance;

public class DijkstraDistanceService implements IDistanceService {

	@SuppressWarnings("unused")
	private final static Logger LOGGER = LoggerFactory.getLogger(DijkstraDistanceService.class);
	
	private final DijkstraDistance<Point, Unit<Double>> dijkstraDistance;

	private final Map<UotsQueryTrajectory, QueryTrajectoryData> trajectoryMap = new HashMap<>();
	
	
	DijkstraDistanceService(final NetGraph graph) {
		this.dijkstraDistance = new DijkstraDistance<>(graph.getReducedGraph(), new Transformer<Unit<Double>, Number>() {
			@Override
			public Number transform(Unit<Double> arg0) {
				return arg0.getValue0() / graph.getDiagonalLength();
			}
		}, true);
	}
	
	
	@Override
	public synchronized void addQueryTrajectory(UotsQueryTrajectory queryTrajectory, int k)
			throws IllegalArgumentException {
		
		if(this.trajectoryMap.containsKey(queryTrajectory)) {
			throw new IllegalArgumentException("queryTrajectory already added" );
		}
		this.trajectoryMap.put(queryTrajectory, new QueryTrajectoryData(k));
	}
	
	@Override
	public void removeTrajectory(UotsQueryTrajectory queryTrajectory,
			UotsTrajectory dataTrajectory) {
		this.remove(this.trajectoryMap.get(queryTrajectory), dataTrajectory);
	}
	
	private double getMinDistance(Point ec, UotsTrajectory dataTrajectory) {
		double minDistance = Double.MAX_VALUE;
		for(final Point point : dataTrajectory.getGraphPoints()) {
			double nextDistance = this.dijkstraDistance.getDistance(ec, point).doubleValue();
			if(nextDistance < minDistance) {
				minDistance = nextDistance;
			}
		}
		return minDistance;
	}
	
	@Override
	public List<UotsTrajectory> getDistance(UotsQueryTrajectory queryTrajectory, UotsTrajectory dataTrajectory) {
		
		final long start = System.currentTimeMillis();
		
		final QueryTrajectoryData qData = this.trajectoryMap.get(queryTrajectory);
		
			double distance = 0;
			for(Point ec : queryTrajectory.getData().getExpansionCenters()) {
				distance += this.getMinDistance(ec, dataTrajectory);
			}
			dataTrajectory.setDistance(2.0 / (1.0 + Math.pow(Math.E, -distance)) - 1);
			LOGGER.info(dataTrajectory.getDistance() + "|" + distance + " id: " + dataTrajectory.getRawTrajectory().getVehicleId());

			this.addToKNearest(qData, dataTrajectory);
		
		LOGGER.info("TIME: " + (System.currentTimeMillis() - start));
		
		return this.computeResult(qData);
	}
	
	
	private void remove(QueryTrajectoryData qData, UotsTrajectory dataTrajectory) {
		qData.kNearest.remove(dataTrajectory);
		if(qData.unfinished == dataTrajectory) {
			qData.unfinished = null;
		}
	}
		
	private void addToKNearest(QueryTrajectoryData qData, UotsTrajectory dataTrajectory) {
		qData.kNearest.add(dataTrajectory);
	}
	

	private List<UotsTrajectory> computeResult(QueryTrajectoryData qData) {
		final List<UotsTrajectory> result = new ArrayList<>(qData.k);
		final Iterator<UotsTrajectory> it = qData.kNearest.iterator();
		result.add(it.next());
		for (int i = 1; i < qData.k && it.hasNext(); i++) {
			result.add(it.next());
		}
		return result;
	}
	
	private final static class QueryTrajectoryData {
		
		private final int k;
		
		
		private UotsTrajectory unfinished;	
		private final PriorityQueue<UotsTrajectory> kNearest;
		
		private QueryTrajectoryData(final int k) {
			this.k = k;
			this.kNearest = new PriorityQueue<>(this.k, new Comparator<UotsTrajectory>() {
				@Override
				public int compare(UotsTrajectory o1, UotsTrajectory o2) {
					return (int)Math.signum(o1.getDistance() - o2.getDistance());
				}
			});
		}
	}
}
