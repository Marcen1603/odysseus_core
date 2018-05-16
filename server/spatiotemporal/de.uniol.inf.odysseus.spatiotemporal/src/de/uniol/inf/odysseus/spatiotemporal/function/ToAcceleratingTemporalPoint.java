package de.uniol.inf.odysseus.spatiotemporal.function;

import java.util.Collection;
import java.util.Iterator;

import org.geotools.referencing.GeodeticCalculator;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;
import de.uniol.inf.odysseus.spatiotemporal.types.point.AcceleratingMovingPointFunction;
import de.uniol.inf.odysseus.spatiotemporal.types.point.TemporalPoint;

public class ToAcceleratingTemporalPoint<M extends ITimeInterval, T extends Tuple<M>>
		extends ToLinearTemporalPoint<M, T> {

	private static final long serialVersionUID = 3700722537949075863L;

	// For OSGi
	public ToAcceleratingTemporalPoint() {
		super();
	}

	@Override
	protected Object[] handleFilledHistory(T trigger, T oldestElement, PointInTime currentPointInTime,
			Collection<T> history) {
		Geometry basePoint = getPointFromElement(oldestElement);
		PointInTime basePointInTime = oldestElement.getMetadata().getStart();

		T middleElement = getMiddleElement(history);
		PointInTime middlePointInTime = middleElement.getMetadata().getStart();
		Geometry middlePoint = getPointFromElement(middleElement);

		Geometry currentPoint = getPointFromElement(trigger);

		GeodeticCalculator fullCalculator = getGeodeticCalculator(basePoint, currentPoint);
		GeodeticCalculator firstHalfCalculator = getGeodeticCalculator(basePoint, middlePoint);
		GeodeticCalculator secondHalfCalculator = getGeodeticCalculator(middlePoint, currentPoint);

		// Speed first half
		double v1 = calculateSpeed(firstHalfCalculator, basePointInTime, middlePointInTime);

		// Speed second half
		double v2 = calculateSpeed(secondHalfCalculator, middlePointInTime, currentPointInTime);

		// Acceleration
		double acceleration = (v2 - v1) / (currentPointInTime.minus(basePointInTime).getMainPoint());

		// Azimuth over full distance
		double azimuth = fullCalculator.getAzimuth();

		TemporalFunction<GeometryWrapper> temporalPointFunction = new AcceleratingMovingPointFunction(basePoint,
				basePointInTime, v1, acceleration, azimuth);
		TemporalPoint[] temporalPoint = new TemporalPoint[1];
		temporalPoint[0] = new TemporalPoint(temporalPointFunction);
		return temporalPoint;
	}

	private double calculateSpeed(GeodeticCalculator calculator, PointInTime firstTime, PointInTime secondTime) {
		long timeInstancesTravelled = secondTime.minus(firstTime).getMainPoint();
		double metersTravelled = calculator.getOrthodromicDistance();
		double speedMetersPerTimeInstance = metersTravelled / timeInstancesTravelled;
		if (Double.isNaN(speedMetersPerTimeInstance) || Double.isInfinite(speedMetersPerTimeInstance)) {
			speedMetersPerTimeInstance = 0;
		}
		return speedMetersPerTimeInstance;
	}

	private T getMiddleElement(Collection<T> history) {
		T middleElement = null;

		int middleElementIndex = (int) history.size() / 2;
		Iterator<T> iterator = history.iterator();
		for (int i = 0; i < middleElementIndex; i++) {
			middleElement = iterator.next();
		}

		return middleElement;
	}

}
