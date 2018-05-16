package de.uniol.inf.odysseus.spatiotemporal.function;

import java.util.Collection;

import org.geotools.referencing.GeodeticCalculator;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;
import de.uniol.inf.odysseus.spatiotemporal.types.point.LinearMovingPointFunction;
import de.uniol.inf.odysseus.spatiotemporal.types.point.TemporalPoint;

public class ToAcceleratingMovingPoint<M extends ITimeInterval, T extends Tuple<M>> extends ToLinearTemporalPoint<M, T> {

	private static final long serialVersionUID = 3700722537949075863L;
	
	// For OSGi
	public ToAcceleratingMovingPoint() {
		super();
	}
	
	@Override
	protected Object[] handleFilledHistory(T trigger, T oldestElement, PointInTime pointInTime, Collection<T> history) {
		Geometry currentPoint = getPointFromElement(trigger);
		Geometry basePoint = getPointFromElement(oldestElement);
		GeodeticCalculator geodeticCalculator = getGeodeticCalculator(basePoint, currentPoint);

		PointInTime basePointInTime = oldestElement.getMetadata().getStart();
		long timeInstancesTravelled = pointInTime.minus(basePointInTime).getMainPoint();
		double metersTravelled = geodeticCalculator.getOrthodromicDistance();
		double speedMetersPerTimeInstance = metersTravelled / timeInstancesTravelled;
		if (Double.isNaN(speedMetersPerTimeInstance) || Double.isInfinite(speedMetersPerTimeInstance)) {
			speedMetersPerTimeInstance = 0;
		}
		double azimuth = geodeticCalculator.getAzimuth();

		TemporalFunction<GeometryWrapper> temporalPointFunction = new LinearMovingPointFunction(basePoint,
				basePointInTime, speedMetersPerTimeInstance, azimuth);
		TemporalPoint[] temporalPoint = new TemporalPoint[1];
		temporalPoint[0] = new TemporalPoint(temporalPointFunction);
		return temporalPoint;
	}

}
