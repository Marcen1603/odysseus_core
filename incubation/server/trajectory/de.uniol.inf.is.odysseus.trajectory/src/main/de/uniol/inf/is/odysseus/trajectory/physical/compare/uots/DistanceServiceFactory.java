package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.graph.NetGraph;
import de.uniol.inf.is.odysseus.trajectory.util.AbstractFactory;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class DistanceServiceFactory extends AbstractFactory<IDistanceService, NetGraph> {

	
	private final static DistanceServiceFactory INSTANCE = new DistanceServiceFactory();

	public static DistanceServiceFactory getInstance() {
		return INSTANCE;
	}
	
	@Override
	protected NetGraph convertKey(NetGraph key) {
		return key;
	}

	@Override
	protected IDistanceService createProduct(NetGraph convertedKey) {
		return new DijkstraDistanceService(convertedKey);
	}

}
