package de.uniol.inf.is.odysseus.tc.error;

import java.util.List;
import java.util.Random;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.tc.vehicle.VehicleInfo;

public class RandomErrorGenerator implements IErrorGenerator {

	private final static Random RANDOM = new Random();
	
	private final static GeometryFactory GF = new GeometryFactory();
	
	@Override
	public List<VehicleInfo> accumulate(List<VehicleInfo> vList) {
		for(VehicleInfo vi : vList) {
			final double x = vi.getPosition().getX() + RANDOM.nextGaussian() * 3;
			final double y = vi.getPosition().getY() + RANDOM.nextGaussian() * 3;
			vi.setPosition(GF.createPoint(new Coordinate(x, y)));
		}
		return vList;
	}

}
