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
package de.uniol.inf.is.odysseus.aggregation.logicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.logicaloperator.builder.AggregationItemParameter;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.IStatefulAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * @author Cornelius Ludmann
 *
 */
@LogicalOperator(name = "SIMPLE_AGGREGATION", minInputPorts = 1, maxInputPorts = 1, doc = "Aggretations on inputAttributeIndices e.g Min, Max, Count, Avg, Sum and grouping.", url = "http://odysseus.offis.uni-oldenburg.de:8090/display/ODYSSEUS/Aggregate+%28and+Group%29+operator", category = {
		LogicalOperatorCategory.BASE })
public class AggregationAO extends UnaryLogicalOp implements IStatefulAO {

	private static final long serialVersionUID = -94620121792280046L;

	private List<SDFAttribute> groupingAttributes;

	private List<IAggregationFunction> functions;

	public AggregationAO() {
		super();
	}

	/**
	 * Copy constructor.
	 * 
	 * @param op
	 *            The other operator to copy.
	 */
	public AggregationAO(final AggregationAO op) {
		super(op);
		groupingAttributes = op.groupingAttributes;
		functions = op.functions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.
	 * AbstractLogicalOperator#clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new AggregationAO(this);
	}

	@Parameter(name = "GROUP_BY", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true)
	public void setGroupingAttributes(final List<SDFAttribute> attributes) {
		groupingAttributes = attributes;
	}

	@GetParameter(name = "GROUP_BY")
	public List<SDFAttribute> getGroupingAttributes() {
		return Collections.unmodifiableList(groupingAttributes);
	}

	@Parameter(name = "AGGREGATIONS", type = AggregationItemParameter.class, isList = true)
	public void setAggregations(final List<IAggregationFunction> aggregations) {
		functions = aggregations;
	}

	@GetParameter(name = "AGGREGATIONS")
	public List<IAggregationFunction> getAggregations() {
		return Collections.unmodifiableList(functions);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.
	 * AbstractLogicalOperator#getOutputSchemaIntern(int)
	 */
	@Override
	protected SDFSchema getOutputSchemaIntern(final int pos) {
		final List<SDFAttribute> outAttribs = new ArrayList<>();
		// First grouping inputAttributeIndices
		if (groupingAttributes != null) {
			outAttribs.addAll(groupingAttributes);
		}

		int attrPos = outAttribs.size();

		for (final IAggregationFunction f : functions) {
			final Collection<SDFAttribute> attributes = f.getOutputAttributes();
			outAttribs.addAll(attributes);
			final int[] outputIndices = new int[attributes.size()];
			for (int i = 0; i < outputIndices.length; ++i) {
				outputIndices[i] = attrPos++;
			}
			f.setOutputAttributeIndices(outputIndices);
		}

		if (getInputSchema() != null) {
			return SDFSchemaFactory.createNewWithAttributes(outAttribs, getInputSchema());
		} else {
			return SDFSchemaFactory.createNewSchema("<tmp>", Tuple.class, outAttribs);
		}
	}
}
