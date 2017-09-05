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

public class First<M extends ITimeInterval, T extends Tuple<M>> extends AbstractIncrementalAggregationFunction<M, T>
		implements IAggregationFunctionFactory {

	private static final long serialVersionUID = -2033932993111049995L;

	private T firstTuple;
	private final SDFSchema subSchema;
	private boolean alreadyReturned = false;;
	
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
	public void addNew(T newElement) {
		if (this.firstTuple == null) {
			this.firstTuple = newElement;
		}
	}

	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {
		if (outdatedElements.contains(this.firstTuple)) {
			this.firstTuple = null;
			this.alreadyReturned = false;
		}
	}

	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {
		if (!this.alreadyReturned && this.firstTuple != null) {
			this.alreadyReturned = true;
			return this.firstTuple.getAttributes();
		}
		return new Tuple[] { };
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		return subSchema.getAttributes();
	}

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		return true;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		return new First<>(null, "", attributeResolver.getSchema().get(0));
	}

	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new First<>(this);
	}

}
