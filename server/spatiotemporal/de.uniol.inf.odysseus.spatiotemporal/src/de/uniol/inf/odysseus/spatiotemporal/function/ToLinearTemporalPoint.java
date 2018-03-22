package de.uniol.inf.odysseus.spatiotemporal.function;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractNonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;
import de.uniol.inf.odysseus.spatiotemporal.types.point.LinearMovingPointFunction;
import de.uniol.inf.odysseus.spatiotemporal.types.point.TemporalPoint;

public class ToLinearTemporalPoint<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractNonIncrementalAggregationFunction<M, T> implements IAggregationFunctionFactory {

	private static final long serialVersionUID = -564559788689771841L;

	protected final TemporalPoint[] temporalPoint = new TemporalPoint[1];

	@Override
	public Object[] evaluate(Collection<T> elements, T trigger, PointInTime pointInTime) {
		T oldestElement = popOldestElement(elements);
		Geometry basePoint = getOldestPoint(oldestElement);
		PointInTime basePointInTime = getOldestStartTime(oldestElement);
		long timeInstancesTravelled = pointInTime.minus(basePointInTime).getMainPoint();
		
		
		double speedMetersPerTimeInstance = 0;
		double azimuth = 0;
		// TODO Auto-generated method stub
		TemporalFunction<GeometryWrapper> temporalPointFunction = new LinearMovingPointFunction(basePoint,
				basePointInTime, speedMetersPerTimeInstance, azimuth);
		this.temporalPoint[0] = new TemporalPoint(null);
		return null;
	}
		
	private PointInTime getOldestStartTime(T oldestElement) {
		return oldestElement.getMetadata().getStart();
	}

	private Geometry getOldestPoint(T oldestElement) {
		Geometry oldestPoint = null;
		
		Object[] attributesFromOldest = this.getAttributes(oldestElement);
		// Should only be one attribute given for this function
		Object pointFromOldest = attributesFromOldest[0];
		if (pointFromOldest instanceof GeometryWrapper) {
			oldestPoint = ((GeometryWrapper) pointFromOldest).getGeometry();
		} else {
			throw new ClassCastException("Cannot use any other attribute type than a Geometry.");
		}
		return oldestPoint;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		// TODO Auto-generated method stub
		return null;
	}

}
