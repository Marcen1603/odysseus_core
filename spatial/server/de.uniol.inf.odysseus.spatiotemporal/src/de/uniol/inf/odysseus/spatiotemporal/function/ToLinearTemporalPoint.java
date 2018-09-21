package de.uniol.inf.odysseus.spatiotemporal.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.referencing.GeodeticCalculator;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

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
import de.uniol.inf.is.odysseus.temporaltypes.types.real.SplineDoubleFunction;
import de.uniol.inf.odysseus.spatiotemporal.types.point.LinearMovingPointFunction;
import de.uniol.inf.odysseus.spatiotemporal.types.point.TemporalGeometry;

/**
 * An aggregation function to create a temporal function for a point that is
 * moving linearly in space.
 * 
 * @author Tobias Brandt
 *
 * @param <M>
 *            The metadata type
 * @param <T>
 *            The tuple type
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
		T newestElement = getNewestElement(elements);

		if (oldestElement == null && newestElement != null) {
			// We have only one element in history
			return handleEmptyHistory(newestElement);
		} else if (newestElement == null) {
			/*
			 * We don't have any element (happens if evaluated at outdating for a certain
			 * group)
			 */
			return handleNoElement(trigger);
		}
		return handleFilledHistory(newestElement, oldestElement, elements);
	}

	public Object[] handleNoElement(T trigger) {
		Geometry triggerPoint = getGeometryFromElement(trigger);
		Point zeroPoint = GeometryFactory.createPointFromInternalCoord(new Coordinate(0, 0), triggerPoint);
		TemporalFunction<GeometryWrapper> temporalPointFunction = new LinearMovingPointFunction(zeroPoint,
				trigger.getMetadata().getStart(), 0, 0);
		TemporalGeometry[] temporalPoint = new TemporalGeometry[1];
		temporalPoint[0] = new TemporalGeometry(temporalPointFunction);
		return temporalPoint;
	}

	public Object[] handleEmptyHistory(T newestElement) {
		Geometry currentPoint = getGeometryFromElement(newestElement);
		TemporalFunction<GeometryWrapper> temporalPointFunction = new LinearMovingPointFunction(currentPoint,
				newestElement.getMetadata().getStart(), 0, 0);
		TemporalGeometry[] temporalPoint = new TemporalGeometry[1];
		temporalPoint[0] = new TemporalGeometry(temporalPointFunction);
		return temporalPoint;
	}

	public Object[] handleFilledHistory(T newestElement, T oldestElement, Collection<T> history) {
		Geometry currentPoint = getGeometryFromElement(newestElement);
		Geometry oldestPoint = getGeometryFromElement(oldestElement);
		PointInTime oldestPointInTime = oldestElement.getMetadata().getStart();
		PointInTime currentPointInTime = newestElement.getMetadata().getStart();
		TemporalFunction<Double> trustFunction = createTrustFunction(history);
		return createTemporalPoint(currentPoint, oldestPoint, currentPointInTime, oldestPointInTime, trustFunction);
	}

	public Object[] createTemporalPoint(Geometry currentPoint, Geometry oldestPoint, PointInTime currentPointInTime,
			PointInTime oldestPointInTime, TemporalFunction<Double> trustFunction) {
		GeodeticCalculator geodeticCalculator = getGeodeticCalculator(oldestPoint, currentPoint);
		long timeInstancesTravelled = currentPointInTime.minus(oldestPointInTime).getMainPoint();
		double metersTravelled = geodeticCalculator.getOrthodromicDistance();
		double speedMetersPerTimeInstance = metersTravelled / timeInstancesTravelled;
		if (Double.isNaN(speedMetersPerTimeInstance) || Double.isInfinite(speedMetersPerTimeInstance)) {
			speedMetersPerTimeInstance = 0;
		}
		double azimuth = geodeticCalculator.getAzimuth();

		TemporalFunction<GeometryWrapper> temporalPointFunction = new LinearMovingPointFunction(currentPoint,
				currentPointInTime, speedMetersPerTimeInstance, azimuth);
		TemporalGeometry[] temporalPoint = new TemporalGeometry[1];
		temporalPoint[0] = new TemporalGeometry(temporalPointFunction, trustFunction);
		return temporalPoint;
	}

	/**
	 * Estimate the trust value of the temporal values
	 * 
	 * @return
	 */
	protected TemporalFunction<Double> createTrustFunction(Collection<T> history) {
		return createSplineTemporalTrustFunction(history);
	}
	
	private TemporalFunction<Double> createSplineTemporalTrustFunction(Collection<T> history) {
		List<Double> tempDim = new ArrayList<>();
		List<Double> trustDim = new ArrayList<>();

		T prevValue = null;
		for (T element : history) {

			// Only the first loop
			if (prevValue == null) {
				// Before our real history, the trust is low
				tempDim.add((double) (0));
				trustDim.add(0.0);

				// Now its getting slightly better
				tempDim.add((double) element.getMetadata().getStart().minus(100).getMainPoint());
				trustDim.add(0.5);
			} else if (prevValue != null
					&& element.getMetadata().getStart().minus(prevValue.getMetadata().getStart()).getMainPoint() > 2) {
				/*
				 * There has been some time between the last known value and this one, lower the
				 * trust in between
				 */

				long diff = element.getMetadata().getStart().minus(prevValue.getMetadata().getStart()).getMainPoint();
				long middle = prevValue.getMetadata().getStart().plus(diff / 2).getMainPoint();

				tempDim.add((double) middle);
				trustDim.add(0.2);
			}

			// When we have information in the history, the trust is high
			tempDim.add((double) element.getMetadata().getStart().getMainPoint());
			trustDim.add(1.0);

			prevValue = element;
		}

		// At the end, reduce the trust again
		tempDim.add((double) prevValue.getMetadata().getStart().plus(10).getMainPoint());
		trustDim.add(0.8);

		// Now its getting slightly worse
		tempDim.add((double) prevValue.getMetadata().getStart().plus(20).getMainPoint());
		trustDim.add(0.7);
		tempDim.add((double) prevValue.getMetadata().getStart().plus(30).getMainPoint());
		trustDim.add(0.6);
		tempDim.add((double) prevValue.getMetadata().getStart().plus(40).getMainPoint());
		trustDim.add(0.5);
		tempDim.add((double) prevValue.getMetadata().getStart().plus(50).getMainPoint());
		trustDim.add(0.4);
		tempDim.add((double) prevValue.getMetadata().getStart().plus(60).getMainPoint());
		trustDim.add(0.3);
		tempDim.add((double) prevValue.getMetadata().getStart().plus(70).getMainPoint());
		trustDim.add(0.2);
		tempDim.add((double) prevValue.getMetadata().getStart().plus(80).getMainPoint());
		trustDim.add(0.1);

		/*
		 * At the end, the trust is 0 and should not shrink too much. Hence, add a few 0
		 * points.
		 */
		tempDim.add((double) prevValue.getMetadata().getStart().plus(90).getMainPoint());
		trustDim.add(0.0);
		tempDim.add((double) prevValue.getMetadata().getStart().plus(100).getMainPoint());
		trustDim.add(0.0);
		tempDim.add((double) prevValue.getMetadata().getStart().plus(110).getMainPoint());
		trustDim.add(0.0);
		tempDim.add((double) prevValue.getMetadata().getStart().plus(120).getMainPoint());
		trustDim.add(0.0);
		tempDim.add((double) prevValue.getMetadata().getStart().plus(10000).getMainPoint());
		trustDim.add(0.0);

		double[] tempDimension = new double[tempDim.size()];
		double[] trustDimension = new double[trustDim.size()];

		for (int i = 0; i < tempDim.size(); i++) {
			tempDimension[i] = tempDim.get(i);
			trustDimension[i] = trustDim.get(i);
		}

		TemporalFunction<Double> trustFunction = new SplineDoubleFunction(tempDimension, trustDimension);
		return trustFunction;
	}
	
	

	protected GeodeticCalculator getGeodeticCalculator(Coordinate from, Coordinate to) {
		GeodeticCalculator geodeticCalculator = new GeodeticCalculator();
		double startLongitude = from.y;
		double startLatitude = from.x;
		geodeticCalculator.setStartingGeographicPoint(startLongitude, startLatitude);

		double destinationLongitude = to.y;
		double destinationLatitude = to.x;
		geodeticCalculator.setDestinationGeographicPoint(destinationLongitude, destinationLatitude);

		return geodeticCalculator;
	}

	protected GeodeticCalculator getGeodeticCalculator(Geometry from, Geometry to) {
		return getGeodeticCalculator(from.getCoordinate(), to.getCoordinate());
	}

	protected Geometry getGeometryFromElement(T element) {
		Geometry geom = null;

		Object[] attributes = this.getAttributes(element);
		// Should only be one attribute given for this function
		Object pointObject = attributes[0];
		if (pointObject instanceof GeometryWrapper) {
			geom = ((GeometryWrapper) pointObject).getGeometry();
		} else {
			throw new ClassCastException("Cannot use any other attribute type than a Geometry.");
		}
		return geom;
	}

	public T popOldestElement(Collection<T> elements) {
		if (elements.isEmpty()) {
			return null;
		}
		return elements.iterator().next();
	}

	public T getNewestElement(Collection<T> elements) {
		Iterator<T> iterator = elements.iterator();
		T newestElement = null;
		while (iterator.hasNext()) {
			newestElement = iterator.next();
		}
		return newestElement;
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
