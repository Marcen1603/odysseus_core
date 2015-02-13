package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.util.AbstractFactory;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class DistanceServiceFactory extends AbstractFactory<IDistanceService, UndirectedSparseGraph<Point, LineSegment>> {

	
	private final static DistanceServiceFactory INSTANCE = new DistanceServiceFactory();

	public static DistanceServiceFactory getInstance() {
		return INSTANCE;
	}
	
	@Override
	protected UndirectedSparseGraph<Point, LineSegment> convertKey(
			UndirectedSparseGraph<Point, LineSegment> key) {
		return key;
	}

	@Override
	protected IDistanceService createProduct(
			UndirectedSparseGraph<Point, LineSegment> convertedKey) {
		return new DijkstraDistanceService(convertedKey);
	}

}
