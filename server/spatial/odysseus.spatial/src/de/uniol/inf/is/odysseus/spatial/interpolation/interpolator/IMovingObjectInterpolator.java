package de.uniol.inf.is.odysseus.spatial.interpolation.interpolator;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;

public interface IMovingObjectInterpolator {
	
	public void addLocation(LocationMeasurement locationMeasurement);
	
	public LocationMeasurement calcLocation(String movingObjectId, PointInTime time);
	
	public Map<String, LocationMeasurement> calcAllLocations(PointInTime time);

}
