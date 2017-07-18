package de.uniol.inf.is.odysseus.spatial.interpolation.interpolator;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.GeoHashMODataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.IMovingObjectDataStructure;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;

public class MovingObjectLinearHistoryPredictor implements IMovingObjectPredictor {
	
	private IMovingObjectDataStructure indexStructure;

	public MovingObjectLinearHistoryPredictor(int geometryAttributePosition, int idAttributePosition) {
		// TODO Fill magic values with correct content / change index structure
		this.indexStructure = new GeoHashMODataStructure("bla", geometryAttributePosition, idAttributePosition, 1000);
	}
	
	@Override
	public void addLocation(LocationMeasurement locationMeasurement, IStreamObject<? extends IMetaAttribute> streamElement) {
		this.indexStructure.add(locationMeasurement, streamElement);
	}

	@Override
	public LocationMeasurement calcLocation(String movingObjectId, PointInTime time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, LocationMeasurement> calcAllLocations(PointInTime time) {
		// TODO Auto-generated method stub
		return null;
	}

}
