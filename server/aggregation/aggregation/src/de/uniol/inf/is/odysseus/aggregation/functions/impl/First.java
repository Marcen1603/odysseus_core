package de.uniol.inf.is.odysseus.aggregation.functions.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractNonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class First<M extends ITimeInterval, T extends Tuple<M>> extends AbstractNonIncrementalAggregationFunction<M, T>
		implements IAggregationFunctionFactory {

	private static final long serialVersionUID = -2033932993111049995L;

	private final SDFSchema subSchema;

	public First() {
		super();
		this.subSchema = null;
	}

	public First(final int[] attributes, final String outputAttributeName, final SDFSchema subSchema) {
		super(attributes, new String[] { outputAttributeName });
		this.subSchema = subSchema;
	}

	public First(final First<M, T> other) {
		super(other);
		this.subSchema = null;
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		return Collections.singleton(new SDFAttribute(null, "first",
				SDFDatatype.createTypeWithSubSchema(SDFDatatype.TUPLE, this.subSchema), null, null, null));
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		return new First<>(null, "", attributeResolver.getSchema().get(0));
	}

	@Override
	public Object[] evaluate(Collection<T> elements, T trigger, PointInTime pointInTime) {
		if (!elements.isEmpty()) {
			@SuppressWarnings("unchecked")
			Tuple<M>[] tupleReturn = new Tuple[1];
			/// Use the first element from the window (they are ordered, see
			/// {@needsOrderedElements})
			tupleReturn[0] = elements.iterator().next();
			return tupleReturn;
		}
		return new Tuple[] {};
	}

	@Override
	public boolean needsOrderedElements() {
		return true;
	}

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		return true;
	}

}