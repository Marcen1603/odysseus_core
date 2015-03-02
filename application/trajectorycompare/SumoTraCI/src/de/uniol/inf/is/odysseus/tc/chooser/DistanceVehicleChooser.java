package de.uniol.inf.is.odysseus.tc.chooser;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.tc.vehicle.VehicleInfo;

public class DistanceVehicleChooser extends AbstractVehicleChooser {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(DistanceVehicleChooser.class);
	
	private final static double MIN_DISTANCE = 30.0;
	
	
	private final Map<String, Point> lastPoints = new HashMap<>();
	
	
	@Override
	protected boolean choose(VehicleInfo vi) {
		final Point lastPoint = this.lastPoints.get(vi.getId());
		if(lastPoint.distance(vi.getPosition()) > MIN_DISTANCE) {
			this.lastPoints.put(vi.getId(), vi.getPosition());
			return true;
		}
		return false;
	}


	@Override
	protected void onFirstAdded(VehicleInfo vi) {
		this.lastPoints.put(vi.getId(), vi.getPosition());
	}


	@Override
	protected void onRemoved(VehicleInfo vi) {
		this.lastPoints.remove(vi);
	}


}
