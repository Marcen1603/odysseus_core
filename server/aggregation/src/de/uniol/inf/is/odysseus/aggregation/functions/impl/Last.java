package de.uniol.inf.is.odysseus.aggregation.functions.impl;

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

/**
 * Returns the last known element on evaluation. Especially useful with a
 * tumbling window.
 * 
 * @author Tobias Brandt
 */
public class Last<M extends ITimeInterval, T extends Tuple<M>> extends AbstractIncrementalAggregationFunction<M, T>
		implements IAggregationFunctionFactory {

	private static final long serialVersionUID = 7074271095932640014L;

	private final SDFSchema subSchema;
	private T lastElement;
	@SuppressWarnings("unchecked")
	private Tuple<M>[] emptyResult = new Tuple[] {};

	public Last() {
		super();
		this.subSchema = null;
	}

	public Last(final int[] attributes, final String outputAttributeName, final SDFSchema subSchema) {
		super(attributes, new String[] { outputAttributeName });
		this.subSchema = subSchema;
	}

	public Last(final Last<M, T> other) {
		super(other);
		this.subSchema = other.getSubSchema().clone();
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		String outputName = AggregationFunctionParseOptionsHelper.getFunctionParameterAsString(parameters,
				AggregationFunctionParseOptionsHelper.OUTPUT_ATTRIBUTES);
		if (outputName == null) {
			outputName = "last";
		}

		return new Last<>(null, outputName, attributeResolver.getSchema().get(0));
	}

	@Override
	public void addNew(T newElement) {
		this.lastElement = newElement;
	}

	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {
		if (outdatedElements.contains(this.lastElement)) {
			this.lastElement = null;
		}
	}

	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {
		if (this.lastElement != null) {
			@SuppressWarnings("unchecked")
			Tuple<M>[] tupleReturn = new Tuple[1];
			tupleReturn[0] = this.lastElement;
			return tupleReturn;
		}
		return this.emptyResult;
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		return Collections.singleton(new SDFAttribute(null, outputAttributeNames[0],
				SDFDatatype.createTypeWithSubSchema(SDFDatatype.TUPLE, this.subSchema), null, null, null));
	}

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		return true;
	}

	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new Last<M, T>(this);
	}

	public SDFSchema getSubSchema() {
		return subSchema;
	}

}
