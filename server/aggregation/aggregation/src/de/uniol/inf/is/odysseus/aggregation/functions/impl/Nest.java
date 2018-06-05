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
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

/**
 * @author Cornelius Ludmann
 *
 */
public class Nest<M extends ITimeInterval, T extends Tuple<M>> extends AbstractNest<M, T> {

	private static final long serialVersionUID = 2408592302467699238L;

	public Nest() {
		super();
	}

	public Nest(final int[] attributes, final String outputAttributeName, final SDFSchema subSchema,
			final boolean preserveOrderingOfElements, final boolean sortElements, final int[] uniqueAttributeIndices) {
		super(new ArrayList<>(), attributes, outputAttributeName, subSchema, preserveOrderingOfElements, sortElements,
				uniqueAttributeIndices);
	}

	public Nest(final String outputAttributeName, final SDFSchema subSchema, final boolean preserveOrderingOfElements,
			final boolean sortElements, final int[] uniqueAttributeIndices) {
		super(outputAttributeName, subSchema, preserveOrderingOfElements, sortElements, uniqueAttributeIndices);
	}

	public Nest(final Nest<M, T> other) {
		super(other, new ArrayList<>());
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
		String outputName = AggregationFunctionParseOptionsHelper.getFunctionParameterAsString(parameters,
				AggregationFunctionParseOptionsHelper.OUTPUT_ATTRIBUTES);
		final boolean preserveOrdering = AggregationFunctionParseOptionsHelper.getFunctionParameterAsBoolean(parameters,
				"PRESERVE_ORDERING", false);
		final boolean sort = AggregationFunctionParseOptionsHelper.getFunctionParameterAsBoolean(parameters, "SORT",
				false);

		/*
		 * We need to test if the parameter exists. If not, we would get an error if we
		 * don't test first. And the user does not deserve unnecessary errors.
		 */
		String parameterExists = AggregationFunctionParseOptionsHelper.getFunctionParameterAsString(parameters,
				"UNIQUE_ATTR");
		final int[] uniqueAttributeIndices = parameterExists == null ? null
				: AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters, attributeResolver,
						"UNIQUE_ATTR");

		if (outputName == null) {
			outputName = "nest";
		}
		if (inputAttrs == null) {
			return new Nest<>(outputName, attributeResolver.getSchema().get(0), preserveOrdering, sort,
					uniqueAttributeIndices);
		} else {
			final List<SDFAttribute> attr = new ArrayList<>();
			for (final int idx : inputAttrs) {
				attr.add(attributeResolver.getSchema().get(0).getAttribute(idx).clone());
			}
			final SDFSchema subSchema = SDFSchemaFactory.createNewTupleSchema("", attr);
			return new Nest<>(inputAttrs, outputName, subSchema, preserveOrdering, sort, uniqueAttributeIndices);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.
	 * AbstractIncrementalAggregationFunction#clone()
	 */
	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new Nest<>(this);
	}
}
