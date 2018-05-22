package de.uniol.inf.odysseus.spatiotemporal.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;
import de.uniol.inf.odysseus.spatiotemporal.types.point.TemporalGeometry;
import de.uniol.inf.odysseus.spatiotemporal.types.region.LinearMovingRegionFunction;
import de.uniol.inf.odysseus.spatiotemporal.types.region.TemporalRegion;

public class ToLinearMovingRegion<M extends ITimeInterval, T extends Tuple<M>> extends ToLinearTemporalPoint<M, T> {

	private static final long serialVersionUID = 6318595318867497345L;

	// For OSGi
	public ToLinearMovingRegion() {
		super();
	}

	public ToLinearMovingRegion(final int inputAttributesLength, final String[] outputNames) {
		super(null, outputNames);
		if (outputNames.length != inputAttributesLength) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
	}

	public ToLinearMovingRegion(final int[] attributes, final String[] outputNames) {
		super(attributes, outputNames);
		if (outputNames.length != attributes.length) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
	}

	public ToLinearMovingRegion(ToLinearMovingRegion<M, T> other) {
		super(other);
	}

	@Override
	public Object[] evaluate(Collection<T> elements, T trigger, PointInTime pointInTime) {
		T oldestElement = popOldestElement(elements);
		T newestElement = getNewestElement(elements);

		Polygon oldestPolygon = (Polygon) getGeometryFromElement(oldestElement);
		Polygon newestPolygon = (Polygon) getGeometryFromElement(newestElement);

		Coordinate[] oldestCoordinates = oldestPolygon.getCoordinates();
		Coordinate[] newestCoordinates = newestPolygon.getCoordinates();

		List<TemporalFunction<GeometryWrapper>> movingPoints = new ArrayList<>();

		for (int i = 0; i < oldestCoordinates.length; i++) {
			Coordinate oldCorner = oldestCoordinates[i];
			Coordinate newCorner = newestCoordinates[i];
			Point oldPoint = GeometryFactory.createPointFromInternalCoord(oldCorner, oldestPolygon);
			Point newPoint = GeometryFactory.createPointFromInternalCoord(newCorner, newestPolygon);
			Object[] temporalPoint = createTemporalPoint(newPoint, oldPoint, newestElement.getMetadata().getStart(),
					oldestElement.getMetadata().getStart());
			TemporalFunction<GeometryWrapper> function = temporalPoint[0] instanceof TemporalGeometry
					? ((TemporalGeometry) temporalPoint[0]).getFunction()
					: null;
			movingPoints.add(function);
		}

		LinearMovingRegionFunction movingRegion = new LinearMovingRegionFunction(movingPoints);
		TemporalRegion region = new TemporalRegion(movingRegion);
		Object[] result = new Object[1];
		result[0] = region;
		return result;
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		final List<SDFAttribute> result = new ArrayList<>(outputAttributeNames.length);

		for (final String attr : outputAttributeNames) {
			result.add(new SDFAttribute(null, attr, SDFSpatialDatatype.SPATIAL_POLYGON, null,
					TemporalDatatype.getTemporalConstraint(), null));
		}

		return result;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		final int[] attributes = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters,
				attributeResolver);
		final String[] outputNames = AggregationFunctionParseOptionsHelper.getOutputAttributeNames(parameters,
				attributeResolver);

		if (attributes == null) {
			return new ToLinearMovingRegion<>(attributeResolver.getSchema().get(0).size(), outputNames);
		}

		return new ToLinearMovingRegion<>(attributes, outputNames);
	}

}
