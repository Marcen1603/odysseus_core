package de.uniol.inf.odysseus.spatiotemporal.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

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
import de.uniol.inf.odysseus.spatiotemporal.types.point.SplineMovingPointFunction;
import de.uniol.inf.odysseus.spatiotemporal.types.point.TemporalGeometry;

/**
 * Creates a temporal point from a stream of non-temporal points by creating a
 * spline function. Only works for 3 and more points of the moving object in the
 * window. If less, a linear function is used instead.
 * 
 * Idea to use two spline functions from here:
 * https://math.stackexchange.com/a/578971/569340
 * 
 * @author Tobias Brandt
 *
 * @param <M>
 *            The metadata
 * @param <T>
 *            The stream element (tuple)
 */
public class ToSplineTemporalPoint<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractNonIncrementalAggregationFunction<M, T> implements IAggregationFunctionFactory {

	private static final long serialVersionUID = -4696528652851211224L;

	private ToLinearTemporalPoint<M, T> fallBackFunction;

	// For OSGi
	public ToSplineTemporalPoint() {

	}

	public ToSplineTemporalPoint(final int[] attributes, final String[] outputNames) {
		super(attributes, outputNames);
		if (outputNames.length != attributes.length) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
		fallBackFunction = new ToLinearTemporalPoint<>(attributes, outputNames);
	}

	public ToSplineTemporalPoint(final int inputAttributesLength, final String[] outputNames) {
		super(null, outputNames);
		if (outputNames.length != inputAttributesLength) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
		fallBackFunction = new ToLinearTemporalPoint<>(null, outputNames);
	}

	public ToSplineTemporalPoint(ToSplineTemporalPoint<M, T> other) {
		super(other);
		this.fallBackFunction = other.fallBackFunction;
	}

	@Override
	public Object[] evaluate(Collection<T> elements, T trigger, PointInTime pointInTime) {

		// Based on this answer: https://math.stackexchange.com/a/578971/569340

		// The time
		double[] t = this.getAllTimestamps(elements);

		// Splines work with 3 or more points. If less, use simple linear function.
		if (elements.size() < 1) {
			return fallBackFunction.handleNoElement(trigger);
		} else if (elements.size() < 2) {
			T newestElement = fallBackFunction.getNewestElement(elements);
			return fallBackFunction.handleEmptyHistory(newestElement);
		} else if (elements.size() < 3) {
			T newestElement = fallBackFunction.getNewestElement(elements);
			T oldestElement = fallBackFunction.popOldestElement(elements);
			return fallBackFunction.handleFilledHistory(newestElement, oldestElement, elements);
		}

		// The locations
		double[] x = new double[elements.size()];
		double[] y = new double[elements.size()];

		// Fill x and y
		this.getAllXYValues(elements, x, y);

		// The function to inter- and extrapolate x
		PolynomialSplineFunction functionX = new SplineInterpolator().interpolate(t, x);

		// The function to inter- and extrapolate y
		PolynomialSplineFunction functionY = new SplineInterpolator().interpolate(t, y);

		// Wrap the function in a temporal point
		TemporalFunction<GeometryWrapper> temporalPointFunction = new SplineMovingPointFunction(functionX, functionY);
		TemporalGeometry[] temporalPoint = new TemporalGeometry[1];
		temporalPoint[0] = new TemporalGeometry(temporalPointFunction);
		return temporalPoint;
	}

	private double[] getAllTimestamps(Collection<T> elements) {
		double[] t = new double[elements.size()];

		int i = 0;
		Iterator<T> iterator = elements.iterator();
		while (iterator.hasNext()) {
			double value = (double) iterator.next().getMetadata().getStart().getMainPoint();
			t[i] = value;
			i++;
		}

		return t;
	}

	private void getAllXYValues(Collection<T> elements, double[] x, double[] y) {
		int i = 0;
		Iterator<T> iterator = elements.iterator();
		while (iterator.hasNext()) {
			Object[] attributes = this.getAttributes(iterator.next());
			// Should only be one
			GeometryWrapper wrapper = (GeometryWrapper) attributes[0];
			x[i] = wrapper.getGeometry().getCoordinate().x;
			y[i] = wrapper.getGeometry().getCoordinate().y;
			i++;
		}
	}

	@Override
	public boolean needsOrderedElements() {
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
			return new ToSplineTemporalPoint<>(attributeResolver.getSchema().get(0).size(), outputNames);
		}

		return new ToSplineTemporalPoint<>(attributes, outputNames);
	}

	@Override
	public AbstractNonIncrementalAggregationFunction<M, T> clone() {
		return new ToSplineTemporalPoint<>(this);
	}

}
