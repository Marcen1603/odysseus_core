package de.uniol.inf.is.odysseus.spatial.aggregationfunctions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.aggregation.physicaloperator.AggregationPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.spatial.index.SpatialIndex2;
import de.uniol.inf.is.odysseus.spatial.index.VPTreeIndex;

public class SpatialKNearestNeighbors<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractIncrementalAggregationFunction<M, T> implements IAggregationFunctionFactory {

	private static final long serialVersionUID = 6762176719624927978L;
	private SpatialIndex2<T> index;
	private SDFSchema subSchema;
	private final static String OUTPUT_NAME = "kNN";
	protected Serializable defaultGroupingKey = "__DEFAULT_GROUPING_KEY";

	private final int k;
	private final int pointAttributeIndex;
	private final Object centerId;
	private final int[] idAttributeIndexes;

	protected final Map<Object, T> mapByUniqueAttributes = new HashMap<>();

	public SpatialKNearestNeighbors() {
		this.k = 0;
		this.pointAttributeIndex = 0;
		this.centerId = "";
		this.idAttributeIndexes = new int[0];
	}

	public SpatialKNearestNeighbors(int pointAttributeIndex, int k, Object centerId, int[] idAttributeIndexes,
			SDFSchema subSchema, int[] attributes) {
		super(attributes, true, new String[] { OUTPUT_NAME });
		this.k = k;
		this.pointAttributeIndex = pointAttributeIndex;
		this.centerId = centerId;
		this.idAttributeIndexes = idAttributeIndexes;
		this.index = new VPTreeIndex<>(pointAttributeIndex);
		this.subSchema = subSchema.clone();
	}

	public SpatialKNearestNeighbors(SpatialKNearestNeighbors<M, T> copy) {
		super(copy);
		this.k = copy.k;
		this.pointAttributeIndex = copy.pointAttributeIndex;
		this.centerId = copy.centerId;
		this.idAttributeIndexes = copy.idAttributeIndexes;
		this.index = new VPTreeIndex<>(copy.pointAttributeIndex);
		this.subSchema = copy.subSchema.clone();
	}

	@Override
	public void addNew(T newElement) {
		Object uniqueAttrKey = AggregationPO.getGroupKey(newElement, idAttributeIndexes, defaultGroupingKey);

		// Remove last element from index if available
		T e = mapByUniqueAttributes.get(uniqueAttrKey);
		if (e != null) {
			this.index.remove(e);
		}

		/*
		 * Don't put the center in the index -> We don't want to have the center in the
		 * results.
		 */
		if (!uniqueAttrKey.equals(this.centerId)) {
			this.index.add(newElement);
		}
		this.mapByUniqueAttributes.put(uniqueAttrKey, newElement);
	}

	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {
		for (T outdated : outdatedElements) {
			this.index.remove(outdated);
		}
	}

	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {
		if (this.mapByUniqueAttributes.get(this.centerId) == null) {
			return null;
		}

		Object e;
		List<T> kNearestNeighbors = this.index.getKNearestNeighbors(this.mapByUniqueAttributes.get(this.centerId),
				this.k);
		List<Object> outputList = new ArrayList<>();
		if (kNearestNeighbors != null) {
			for (T tuple : kNearestNeighbors) {
				if (subSchema.size() == 1) {
					e = getFirstAttribute(tuple);
				} else {
					e = getAttributesAsTuple(tuple);
				}
				outputList.add(e);
			}
		}

		return outputList.toArray();
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
	private final static String ID = "center_id";
	private final static String ID_ATTRIBUTE = "id_attribute_names";

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		int k = AggregationFunctionParseOptionsHelper.getFunctionParameterAsInt(parameters, K, -1);
		int[] pointAttributeIndexes = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
				attributeResolver, POINT_ATTRIBUTE);
		return k > 0 && pointAttributeIndexes.length > 0;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		final int k = AggregationFunctionParseOptionsHelper.getFunctionParameterAsInt(parameters, K, -1);
		final int[] pointAttributeIndexes = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
				attributeResolver, POINT_ATTRIBUTE);
		final int[] centerIdAttributeIndexes = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
				attributeResolver, ID_ATTRIBUTE);
		final int[] attributes = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters,
				attributeResolver, 0, false);
		Object centerId = AggregationFunctionParseOptionsHelper.getFunctionParameter(parameters, ID);

		SpatialKNearestNeighbors<M, T> spatialKNearestNeighbors = null;
		if (attributes != null && pointAttributeIndexes.length > 0 && centerIdAttributeIndexes.length > 0) {
			SDFSchema subSchema = createSubSchemaForOutputAttributes(attributes, attributeResolver);
			spatialKNearestNeighbors = new SpatialKNearestNeighbors<>(pointAttributeIndexes[0], k, centerId,
					centerIdAttributeIndexes, subSchema, attributes);
		} else {
			throw new RuntimeException("Is the point attribute correct?");
		}
		return spatialKNearestNeighbors;
	}

	/**
	 * Creates a schema which is used as the subschema for the output tuples
	 */
	private SDFSchema createSubSchemaForOutputAttributes(int[] attributes, IAttributeResolver attributeResolver) {
		final List<SDFAttribute> attr = new ArrayList<>();
		for (final int idx : attributes) {
			attr.add(attributeResolver.getSchema().get(0).getAttribute(idx).clone());
		}
		final SDFSchema subSchema = SDFSchemaFactory.createNewTupleSchema("", attr);
		return subSchema;
	}

	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new SpatialKNearestNeighbors<M, T>(this);
	}

}
