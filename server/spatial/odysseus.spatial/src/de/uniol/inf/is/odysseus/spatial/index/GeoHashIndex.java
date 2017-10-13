package de.uniol.inf.is.odysseus.spatial.index;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.datatype.ResultElement;
import de.uniol.inf.is.odysseus.spatial.datatype.TrajectoryElement;

public class GeoHashIndex implements SpatialIndex {

	@Override
	public void add(LocationMeasurement locationMeasurement, IStreamObject<? extends IMetaAttribute> streamElement) {
		// TODO Auto-generated method stub
	}

	@Override
	public Map<String, List<ResultElement>> queryCircle(double centerLatitude, double centerLongitude, double radius,
			TimeInterval interval) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ResultElement> queryCircleOnLatestElements(double centerLatitude, double centerLongitude,
			double radius, TimeInterval interval) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<TrajectoryElement>> approximateCircle(double centerLatitude, double centerLongitude,
			double radius, TimeInterval interval) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, TrajectoryElement> approximateCircleOnLatestElements(double centerLatitude, double longitude,
			double radius, TimeInterval interval) {
		// TODO Auto-generated method stub
		return null;
	}

}
