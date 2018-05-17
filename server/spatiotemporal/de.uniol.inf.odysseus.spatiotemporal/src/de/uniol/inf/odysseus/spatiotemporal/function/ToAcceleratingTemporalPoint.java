package de.uniol.inf.odysseus.spatiotemporal.function;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.geotools.referencing.GeodeticCalculator;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractNonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;
import de.uniol.inf.odysseus.spatiotemporal.types.point.AcceleratingMovingPointFunction;
import de.uniol.inf.odysseus.spatiotemporal.types.point.TemporalPoint;

/**
 * Creates a temporal point that is accelerating. The acceleration is calculated
 * by taking the speed of the first half and the second half and calculating the
 * acceleration between these two values. The speed of the first half is used as
 * the initial speed.
 * 
 * @author Tobias Brandt
 *
 */
public class ToAcceleratingTemporalPoint<M extends ITimeInterval, T extends Tuple<M>>
		extends ToLinearTemporalPoint<M, T> {

	private static final long serialVersionUID = 3700722537949075863L;

	// For OSGi
	public ToAcceleratingTemporalPoint() {
		super();
	}

	public ToAcceleratingTemporalPoint(final int[] attributes, final String[] outputNames) {
		super(attributes, outputNames);
		if (outputNames.length != attributes.length) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
	}

	public ToAcceleratingTemporalPoint(final int inputAttributesLength, final String[] outputNames) {
		super(null, outputNames);
		if (outputNames.length != inputAttributesLength) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
	}

	public ToAcceleratingTemporalPoint(ToAcceleratingTemporalPoint<M, T> other) {
		super(other);
	}

	@Override
	protected Object[] handleFilledHistory(T newestElement, T oldestElement, Collection<T> history) {
		Geometry basePoint = getGeometryFromElement(oldestElement);
		PointInTime basePointInTime = oldestElement.getMetadata().getStart();

		T middleElement = getMiddleElement(history);
		PointInTime middlePointInTime = middleElement.getMetadata().getStart();
		Geometry middlePoint = getGeometryFromElement(middleElement);

		PointInTime currentPointInTime = newestElement.getMetadata().getStart();
		Geometry currentPoint = getGeometryFromElement(newestElement);

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
		int i = 0;
		do {
			middleElement = iterator.next();
			i++;
		} while (i < middleElementIndex);

		return middleElement;
	}

	@Override
	public AbstractNonIncrementalAggregationFunction<M, T> clone() {
		return new ToAcceleratingTemporalPoint<>(this);
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		final int[] attributes = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters,
				attributeResolver);
		final String[] outputNames = AggregationFunctionParseOptionsHelper.getOutputAttributeNames(parameters,
				attributeResolver);

		if (attributes == null) {
			return new ToAcceleratingTemporalPoint<>(attributeResolver.getSchema().get(0).size(), outputNames);
		}

		return new ToAcceleratingTemporalPoint<>(attributes, outputNames);
	}

}
