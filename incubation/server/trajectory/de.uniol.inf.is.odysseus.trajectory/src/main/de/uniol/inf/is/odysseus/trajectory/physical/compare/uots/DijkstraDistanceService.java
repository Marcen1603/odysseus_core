package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.buffer.PriorityBuffer;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Multiset;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.physical.compare.RawTrajectory;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraDistance;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class DijkstraDistanceService implements IDistanceService {

	@SuppressWarnings("unused")
	private final static Logger LOGGER = LoggerFactory.getLogger(DijkstraDistanceService.class);
	
	private final DijkstraDistance<Point, LineSegment> dijkstraDistance;


	private final Map<UotsTrajectory, QueryTrajectoryData> trajectoryMap = new HashMap<>();
	
	
	DijkstraDistanceService(final UndirectedSparseGraph<Point, LineSegment> graph) {
		this.dijkstraDistance = new DijkstraDistance<>(graph, new Transformer<LineSegment, Number>() {
			@Override
			public Number transform(LineSegment ls) {
				return ls.getLength();
			}
		}, true);
	}
	
	
	@Override
	public synchronized void addQueryTrajectory(UotsTrajectory queryTrajectory, int k)
			throws IllegalArgumentException {
		
		if(this.trajectoryMap.containsKey(queryTrajectory)) {
			throw new IllegalArgumentException("queryTrajectory already added" );
		}
		this.trajectoryMap.put(queryTrajectory, new QueryTrajectoryData(k));
	}
	
	@Override
	public void removeTrajectory(UotsTrajectory queryTrajectory,
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
	
	private void race(QueryTrajectoryData qData, UotsTrajectory t1, UotsTrajectory t2) {
		
		if(t1 == null || t2 == null) {
			return;
		}
		
		if(t1.getDistance() > t2.getDistance()) {
			final UotsTrajectory t = t1;
			t1 = t2;
			t2 = t;
		}

		while(true) {
			while(t1.getIterator().hasNext()) {
				t1.setDistance(t1.getDistance()
						+ this.getMinDistance(t1.getIterator().next(), t1));
				
				if(t1.getDistance() > t2.getDistance()) {
					break;
				}
			}
			
			if(t1.getIterator().hasNext()) {
				//ende erreicht
				break;
			}

			final UotsTrajectory t = t1;
			t1 = t2;
			t2 = t;
		}
		
		// Ausrechnen
		while(t2.getIterator().hasNext()) {
			t2.setDistance(t2.getDistance()
					+ this.getMinDistance(t2.getIterator().next(), t2));
			
			if(t2.getDistance() > t1.getDistance()) {
				break;
			}
		}
		
		if(t1.getDistance() < t2.getDistance()) {
			this.addToKNearest(qData, t1);
			qData.unfinished = t2;
		} else {
			this.addToKNearest(qData, t2);
			qData.unfinished = t1;
		}
	}
	
	@Override
	public List<UotsTrajectory> getDistance(UotsTrajectory queryTrajectory, UotsTrajectory dataTrajectory) {
		
		final long start = System.currentTimeMillis();
		
		final QueryTrajectoryData qData = this.trajectoryMap.get(queryTrajectory);
		
		dataTrajectory.setIterator(queryTrajectory.getGraphPoints().iterator());
		
		if(qData.kthTrajectory == null && false) {
			if(qData.unfinished == null) {
				// ganz berechnen
				for(final Point ec : queryTrajectory.getGraphPoints()) {
					dataTrajectory.setDistance(dataTrajectory.getDistance() 
							+ this.getMinDistance(ec, dataTrajectory));
				}
				this.addToKNearest(qData, dataTrajectory);
			} else {
				this.race(qData, queryTrajectory, dataTrajectory);
			}
		} else {
			// berechnen bis kthDistance
			//final double kthDistance = qData.kthTrajectory.getDistance();
			double distance = 0;
			while(dataTrajectory.getIterator().hasNext()) {
				distance += this.getMinDistance(dataTrajectory.getIterator().next(), dataTrajectory);
//				dataTrajectory.setDistance(dataTrajectory.getDistance() 
//						+ this.getMinDistance(dataTrajectory.getIterator().next(), dataTrajectory));
				
//				if(dataTrajectory.getDistance() > kthDistance) {
//					//break;
//				}
			}
			distance /= 1000.0;
			dataTrajectory.setDistance(2.0 / (1.0 + Math.pow(Math.E, -distance)) - 1);
			LOGGER.info(dataTrajectory.getDistance() + "|" + distance + " id: " + dataTrajectory.getRawTrajectory().getVehicleId());
			if(dataTrajectory.getIterator().hasNext()) {
				this.race(qData, dataTrajectory, qData.unfinished);
			} else {
				this.addToKNearest(qData, dataTrajectory);
			}
		}
		
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
		this.setKthTrajectory(qData);
	}
	
	private void setKthTrajectory(QueryTrajectoryData qData) {
		if (qData.kNearest.size() < qData.k) {
			qData.kthTrajectory = null;
		}
		final Iterator<UotsTrajectory> it = qData.kNearest.iterator();
		qData.kthTrajectory = it.next();
		for (int i = 1; it.hasNext() && i < qData.k; i++) {
			qData.kthTrajectory = it.next();
		}
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
		
		private UotsTrajectory kthTrajectory;
		
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
