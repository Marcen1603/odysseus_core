package de.uniol.inf.is.odysseus.spatial.aggregationfunctions;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.spatial.index.SpatialIndex2;
import de.uniol.inf.is.odysseus.spatial.index.VPTreeIndex;

public class SpatialKNearestNeighbors<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractIncrementalAggregationFunction<M, T> implements IAggregationFunctionFactory {

	private static final long serialVersionUID = 6762176719624927978L;
	private SpatialIndex2<T> index;
	private SDFSchema subSchema;
	private final static String OUTPUT_NAME = "kNN";

	private final int k;
	private final int pointAttributeIndex;

	public SpatialKNearestNeighbors() {
		this.k = 0;
		this.pointAttributeIndex = 0;
	}

	public SpatialKNearestNeighbors(int pointAttributeIndex, int k, SDFSchema subSchema, int[] attributes) {
		super(attributes, true, new String[] { OUTPUT_NAME });
		this.k = k;
		this.pointAttributeIndex = pointAttributeIndex;
		this.index = new VPTreeIndex<>(pointAttributeIndex);
		this.subSchema = subSchema.clone();
	}

	public SpatialKNearestNeighbors(SpatialKNearestNeighbors<M, T> copy) {
		super(copy);
		this.k = copy.k;
		this.pointAttributeIndex = copy.pointAttributeIndex;
		this.index = new VPTreeIndex<>(copy.pointAttributeIndex);
		this.subSchema = copy.subSchema.clone();
	}

	@Override
	public void addNew(T newElement) {
		this.index.add(newElement);
	}

	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {
		for (T outdated : outdatedElements) {
			this.index.remove(outdated);
		}
	}

	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {
		return this.index.getKNearestNeighbors(trigger, this.k).toArray();
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		// Nested output
		final SDFDatatype datatype = SDFDatatype.createTypeWithSubSchema(SDFDatatype.TUPLE, SDFDatatype.TUPLE,
				subSchema);
		return Collections.singleton(new SDFAttribute(null, "kNN", datatype, null, null, null));
	}

	private final static String K = "K";
	private final static String POINT_ATTRIBUTE = "point_attribute_name";

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		final int k = AggregationFunctionParseOptionsHelper.getFunctionParameterAsInt(parameters, K, -1);
		final int[] pointAttributeIndexes = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
				attributeResolver, POINT_ATTRIBUTE);
		return k > 0 && pointAttributeIndexes.length > 0;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		final int k = AggregationFunctionParseOptionsHelper.getFunctionParameterAsInt(parameters, K, -1);
		final int[] pointAttributeIndexes = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
				attributeResolver, POINT_ATTRIBUTE);
		final int[] attributes = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters,
				attributeResolver, 0, false);

		SpatialKNearestNeighbors<M, T> spatialKNearestNeighbors = null;
		if (attributes != null && pointAttributeIndexes.length > 0) {
			spatialKNearestNeighbors = new SpatialKNearestNeighbors<>(pointAttributeIndexes[0], k,
					attributeResolver.getSchema().get(0), attributes);
		} else {
			throw new RuntimeException("Is the point attributes correct?");
		}
		return spatialKNearestNeighbors;
	}

	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new SpatialKNearestNeighbors<M, T>(this);
	}

}
