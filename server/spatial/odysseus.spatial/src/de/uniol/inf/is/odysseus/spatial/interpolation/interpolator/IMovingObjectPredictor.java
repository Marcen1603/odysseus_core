package de.uniol.inf.is.odysseus.spatial.interpolation.interpolator;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;

public interface IMovingObjectPredictor {
	
	public void addLocation(LocationMeasurement locationMeasurement, IStreamObject<? extends IMetaAttribute> streamElement);
	
	public LocationMeasurement calcLocation(String movingObjectId, PointInTime time);
	
	public Map<String, LocationMeasurement> calcAllLocations(PointInTime time);

}
