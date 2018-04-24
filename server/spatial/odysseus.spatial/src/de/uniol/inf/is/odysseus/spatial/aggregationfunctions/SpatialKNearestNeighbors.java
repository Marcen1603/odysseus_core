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

	private final int k;
	private final int latitudeAttributeIndex;
	private final int longitudeAttributeIndex;

	public SpatialKNearestNeighbors() {
		this.k = 0;
		this.latitudeAttributeIndex = 0;
		this.longitudeAttributeIndex = 0;
	}
	
	public SpatialKNearestNeighbors(int latitudeAttributeIndex, int longitudeAttributeIndex, int k,
			SDFSchema subSchema) {
		this.k = k;
		this.latitudeAttributeIndex = latitudeAttributeIndex;
		this.longitudeAttributeIndex = longitudeAttributeIndex;
		this.index = new VPTreeIndex<>(latitudeAttributeIndex, longitudeAttributeIndex);
		this.subSchema = subSchema.clone();
	}

	public SpatialKNearestNeighbors(SpatialKNearestNeighbors<M, T> copy) {
		this.k = copy.k;
		this.latitudeAttributeIndex = copy.latitudeAttributeIndex;
		this.longitudeAttributeIndex = copy.longitudeAttributeIndex;
		this.index = new VPTreeIndex<>(copy.latitudeAttributeIndex, copy.longitudeAttributeIndex);
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
		return Collections.singleton(new SDFAttribute(null, outputAttributeNames[0], datatype, null, null, null));
	}

	private final static String K = "K";
	private final static String LATITUDE_ATTRIBUTE = "latitude_attribute_name";
	private final static String LONGITUDE_ATTRIBUTE = "longitude_attribute_name";

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		final int k = AggregationFunctionParseOptionsHelper.getFunctionParameterAsInt(parameters, K, -1);
		final int[] latitudeAttributeIndexes = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
				attributeResolver, LATITUDE_ATTRIBUTE);
		final int[] longitudeAttributeIndexes = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
				attributeResolver, LONGITUDE_ATTRIBUTE);
		return k > 0 && latitudeAttributeIndexes.length > 0 && longitudeAttributeIndexes.length > 0;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		final int k = AggregationFunctionParseOptionsHelper.getFunctionParameterAsInt(parameters, K, -1);
		final int[] latitudeAttributeIndexes = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
				attributeResolver, LATITUDE_ATTRIBUTE);
		final int[] longitudeAttributeIndexes = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
				attributeResolver, LONGITUDE_ATTRIBUTE);

		SpatialKNearestNeighbors<M, T> spatialKNearestNeighbors = null;
		if (latitudeAttributeIndexes.length > 0 && longitudeAttributeIndexes.length > 0) {
			spatialKNearestNeighbors = new SpatialKNearestNeighbors<>(latitudeAttributeIndexes[0],
					longitudeAttributeIndexes[0], k, attributeResolver.getSchema().get(0));
		} else {
			throw new RuntimeException("Are the latitude and longitude attributes correct?");
		}
		return spatialKNearestNeighbors;
	}

	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new SpatialKNearestNeighbors<M, T>(this);
	}

}
