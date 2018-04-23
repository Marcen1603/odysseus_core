package de.uniol.inf.is.odysseus.spatial.aggregationfunctions;

import java.util.Collection;
import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

public class SpatialKNearestNeighbors<M extends ITimeInterval, T extends Tuple<M>> extends AbstractIncrementalAggregationFunction<M, T>
implements IAggregationFunctionFactory {

	private static final long serialVersionUID = 6762176719624927978L;

	@Override
	public void addNew(T newElement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
