/**
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.aggregation.functions.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

/**
 * @author Cornelius Ludmann
 *
 */
public class DistinctCount<M extends ITimeInterval, T extends Tuple<M>> extends DistinctNest<M, T> {
	private static final long serialVersionUID = -1728291233547271392L;


	/**
	 * 
	 */
	public DistinctCount() {
		super();
	}

	/**
	 * 
	 */
	public DistinctCount(final int[] attributes, final String outputAttributeName, final SDFSchema subSchema, final int[] uniqueAttributeIndices) {
		super(attributes, outputAttributeName, subSchema, false, false, uniqueAttributeIndices);
	}

	/**
	 * 
	 */
	public DistinctCount(final String outputAttributeName, final SDFSchema subSchema, final int[] uniqueAttributeIndices) {
		super(outputAttributeName, subSchema, false, false, uniqueAttributeIndices);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.aggregation.functions.impl.DistinctNest#evaluate
	 * (java.util.Collection, de.uniol.inf.is.odysseus.core.collection.Tuple,
	 * de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object[] evaluate(final Collection<T> elements, final T trigger, final PointInTime pointInTime) {
		return new Object[] { ((Collection<T>) super.evaluate(elements, trigger, pointInTime)[0]).size() };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.aggregation.functions.impl.AbstractNest#evalute(
	 * de.uniol.inf.is.odysseus.core.collection.Tuple,
	 * de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object[] evalute(final T trigger, final PointInTime pointInTime) {
		return new Object[] { ((Collection<T>) super.evalute(trigger, pointInTime)[0]).size() };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.factory.
	 * IAggregationFunctionFactory#checkParameters(java.util.Map,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver)
	 */
	@Override
	public boolean checkParameters(final Map<String, Object> parameters, final IAttributeResolver attributeResolver) {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.factory.
	 * IAggregationFunctionFactory#createInstance(java.util.Map,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver)
	 */
	@Override
	public IAggregationFunction createInstance(final Map<String, Object> parameters,
			final IAttributeResolver attributeResolver) {
		final int[] inputAttrs = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters,
				attributeResolver);
		final int[] uniqueAttributeIndices = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
				attributeResolver, "UNIQUE_ATTR");
		String outputName = AggregationFunctionParseOptionsHelper.getFunctionParameterAsString(parameters,
				AggregationFunctionParseOptionsHelper.OUTPUT_ATTRIBUTES);
		if (outputName == null) {
			outputName = "distinct_count";
		}

		if (inputAttrs == null) {
			return new DistinctCount<>(outputName, attributeResolver.getSchema().get(0), uniqueAttributeIndices);
		} else {
			final List<SDFAttribute> attr = new ArrayList<>();
			for (final int idx : inputAttrs) {
				attr.add(attributeResolver.getSchema().get(0).getAttribute(idx).clone());
			}
			final SDFSchema subSchema = SDFSchemaFactory.createNewTupleSchema("", attr);
			return new DistinctCount<>(inputAttrs, outputName, subSchema, uniqueAttributeIndices);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.impl.AbstractNest#
	 * getOutputAttributes()
	 */
	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		return Collections
				.singleton(new SDFAttribute(null, outputAttributeNames[0], SDFDatatype.LONG, null, null, null));
	}
}
