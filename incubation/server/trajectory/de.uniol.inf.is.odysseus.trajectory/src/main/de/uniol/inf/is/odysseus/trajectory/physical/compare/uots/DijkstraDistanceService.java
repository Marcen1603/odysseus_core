package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import org.apache.commons.collections15.Transformer;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraDistance;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class DijkstraDistanceService implements IDistanceService {

	private final static Logger LOGGER = LoggerFactory.getLogger(DijkstraDistanceService.class);
	
	
	private final DijkstraDistance<Point, LineSegment> dijkstraDistance;
	
	///für jede route endpunkt + distance merken
	/// ------------------------------        x
	///     ?    -----------------------------------------
	///                ----------------------------------------
	
	SortedSet<UotsTrajectory> kNearest;
	
	private final Map<UotsTrajectory, Foo> trajectoryMap = new HashMap<>();
	
	private double max;
	
	public DijkstraDistanceService(final UndirectedSparseGraph<Point, LineSegment> graph) {
		this.dijkstraDistance = new DijkstraDistance<>(graph, new Transformer<LineSegment, Number>() {
			@Override
			public Number transform(LineSegment ls) {
				return ls.getLength();
			}
		}, true);
	}
	
	
	@Override
	public List<UotsTrajectory> getDistance(UotsTrajectory queryTrajectory,
			UotsTrajectory dataTrajectory, double upperBound, int k) {
				
		double distance = 0;
		for(final Point ec : queryTrajectory.getGraphPoints()) {
			
			final Iterator<Point> dataTraIteratorIt = dataTrajectory.getGraphPoints().iterator();
			double minDistance = this.dijkstraDistance.getDistance(ec, dataTraIteratorIt.next()).doubleValue();
			while(dataTraIteratorIt.hasNext()) {
				double ff = this.dijkstraDistance.getDistance(ec, dataTraIteratorIt.next()).doubleValue();
				if(ff < minDistance) {
					minDistance = ff;
				}
			}
			distance += minDistance;
			
			for(final UotsTrajectory old : this.trajectoryMap.keySet()) {
				final Iterator<Point> oldIt = this.trajectoryMap.get(old).it;
				while(oldIt.hasNext()) {
					oldIt.next();
				}
			}
		}
		return null;
	}


	@Override
	public void removeTrajectory(UotsTrajectory queryTrajectory) {
		this.trajectoryMap.remove(queryTrajectory);
	}
	
	private final static class Foo {
		private final Iterator<Point> it;
		private double distance;
		
		public Foo(Iterator<Point> it, double distance) {
			this.it = it;
			this.distance = distance;
		}
	}
}
