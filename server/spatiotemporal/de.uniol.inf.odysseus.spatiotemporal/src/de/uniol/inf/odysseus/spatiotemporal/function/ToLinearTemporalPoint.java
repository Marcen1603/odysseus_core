package de.uniol.inf.odysseus.spatiotemporal.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.geotools.referencing.GeodeticCalculator;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractNonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;
import de.uniol.inf.odysseus.spatiotemporal.types.point.LinearMovingPointFunction;
import de.uniol.inf.odysseus.spatiotemporal.types.point.TemporalPoint;

/**
 * An aggregation function to create a temporal function for a point that is
 * moving linearly in space.
 * 
 * @author Tobias Brandt
 *
 * @param <M>
 * @param <T>
 */
public class ToLinearTemporalPoint<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractNonIncrementalAggregationFunction<M, T> implements IAggregationFunctionFactory {

	private static final long serialVersionUID = -564559788689771841L;

	// For OSGi
	public ToLinearTemporalPoint() {
		super();
	}

	public ToLinearTemporalPoint(final int[] attributes, final String[] outputNames) {
		super(attributes, outputNames);
		if (outputNames.length != attributes.length) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
	}

	public ToLinearTemporalPoint(final int inputAttributesLength, final String[] outputNames) {
		super(null, outputNames);
		if (outputNames.length != inputAttributesLength) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
	}

	public ToLinearTemporalPoint(ToLinearTemporalPoint<M, T> other) {
		super(other);
	}

	@Override
	public Object[] evaluate(Collection<T> elements, T trigger, PointInTime pointInTime) {
		T oldestElement = popOldestElement(elements);
		
		if (oldestElement == null) {
			return this.handleEmptyHistory(trigger);
		}
		return this.handleFilledHistory(trigger, oldestElement, pointInTime, elements);
	}
	
	private Object[] handleEmptyHistory(T trigger) {
		Geometry currentPoint = getPointFromElement(trigger);
		TemporalFunction<GeometryWrapper> temporalPointFunction = new LinearMovingPointFunction(currentPoint,
				trigger.getMetadata().getStart(), 0, 0);
		TemporalPoint[] temporalPoint = new TemporalPoint[1];
		temporalPoint[0] = new TemporalPoint(temporalPointFunction);
		return temporalPoint;
	}
	
	private Object[] handleFilledHistory(T trigger, T oldestElement, PointInTime pointInTime, Collection<T> history) {
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

	private GeodeticCalculator getGeodeticCalculator(Geometry from, Geometry to) {
		GeodeticCalculator geodeticCalculator = new GeodeticCalculator();

		double startLongitude = from.getCentroid().getY();
		double startLatitude = from.getCentroid().getX();
		geodeticCalculator.setStartingGeographicPoint(startLongitude, startLatitude);

		double destinationLongitude = to.getCentroid().getY();
		double destinationLatitude = to.getCentroid().getX();
		geodeticCalculator.setDestinationGeographicPoint(destinationLongitude, destinationLatitude);

		return geodeticCalculator;
	}

	private Geometry getPointFromElement(T element) {
		Geometry point = null;

		Object[] attributes = this.getAttributes(element);
		// Should only be one attribute given for this function
		Object pointObject = attributes[0];
		if (pointObject instanceof GeometryWrapper) {
			point = ((GeometryWrapper) pointObject).getGeometry();
		} else {
			throw new ClassCastException("Cannot use any other attribute type than a Geometry.");
		}
		return point;
	}

	private T popOldestElement(Collection<T> elements) {
		if (elements.isEmpty()) {
			return null;
		}
		return elements.iterator().next();
	}

	@Override
	public boolean needsOrderedElements() {
		// We need to know the first element to calculate the direction and speed
		return true;
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		final List<SDFAttribute> result = new ArrayList<>(outputAttributeNames.length);

		for (final String attr : outputAttributeNames) {
			result.add(new SDFAttribute(null, attr, SDFSpatialDatatype.SPATIAL_POINT, null,
					TemporalDatatype.getTemporalConstraint(), null));
		}

		return result;
	}

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		final boolean checkInputOutputLength = AggregationFunctionParseOptionsHelper
				.checkInputAttributesLengthEqualsOutputAttributesLength(parameters, attributeResolver);
		return checkInputOutputLength;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		final int[] attributes = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters,
				attributeResolver);
		final String[] outputNames = AggregationFunctionParseOptionsHelper.getOutputAttributeNames(parameters,
				attributeResolver);

		if (attributes == null) {
			return new ToLinearTemporalPoint<>(attributeResolver.getSchema().get(0).size(), outputNames);
		}

		return new ToLinearTemporalPoint<>(attributes, outputNames);
	}

	@Override
	public AbstractNonIncrementalAggregationFunction<M, T> clone() {
		return new ToLinearTemporalPoint<>(this);
	}

}
