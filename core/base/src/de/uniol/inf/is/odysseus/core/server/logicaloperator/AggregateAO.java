/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItemParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;

@LogicalOperator(name = "AGGREGATE", minInputPorts = 1, maxInputPorts = 1)
public class AggregateAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 2539966167342852544L;

	final private Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations;
	final private List<SDFAttribute> groupingAttributes;
	final private List<SDFAttribute> outputAttributList;
	private int dumpAtValueCount = -1;

	public AggregateAO() {
		super();
		aggregations = new HashMap<SDFSchema, Map<AggregateFunction, SDFAttribute>>();
		groupingAttributes = new ArrayList<SDFAttribute>();
		outputAttributList = new ArrayList<SDFAttribute>();
	}

	public AggregateAO(AggregateAO op) {
		super(op);
		aggregations = new HashMap<SDFSchema, Map<AggregateFunction, SDFAttribute>>(
				op.aggregations);
		groupingAttributes = new ArrayList<SDFAttribute>(op.groupingAttributes);
		outputAttributList = new ArrayList<SDFAttribute>(op.outputAttributList);
		dumpAtValueCount = op.dumpAtValueCount;
	}

	public void addAggregation(SDFAttribute attribute,
			AggregateFunction function, SDFAttribute outAttribute) {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(attribute);
		SDFSchema schema = new SDFSchema("", attributes);
		addAggregation(schema, function, outAttribute);
	}

	public void addAggregation(SDFSchema attributes,
			AggregateFunction function, SDFAttribute outAttribute) {
		if (outputAttributList.contains(outAttribute)) {
			throw new IllegalArgumentException(
					"multiple definitions of element " + outAttribute);
		}

		outputAttributList.add(outAttribute);
		setOutputSchema(null);
		Map<AggregateFunction, SDFAttribute> af = aggregations.get(attributes);
		if (af == null) {
			af = new HashMap<AggregateFunction, SDFAttribute>();
			aggregations.put(attributes, af);
		}
		af.put(function, outAttribute);
	}

	public void addAggregations(
			Map<SDFAttribute, Map<AggregateFunction, SDFAttribute>> aggregations) {
		for (Entry<SDFAttribute, Map<AggregateFunction, SDFAttribute>> attrSet : aggregations
				.entrySet()) {
			Map<AggregateFunction, SDFAttribute> aggs = attrSet.getValue();
			for (Entry<AggregateFunction, SDFAttribute> e : aggs.entrySet()) {
				addAggregation(attrSet.getKey(), e.getKey(), e.getValue());
			}
		}
	}

	@GetParameter(name = "AGGREGATIONS")
	public Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> getAggregations() {
		return this.aggregations;
	}

	public boolean hasAggregations() {
		return !this.aggregations.isEmpty();
	}

	public void addGroupingAttribute(SDFAttribute attribute) {
		if (groupingAttributes.contains(attribute)) {
			return;
		}
		groupingAttributes.add(attribute);
		outputAttributList.add(attribute);
		setOutputSchema(null);
	}

	public void addGroupingAttributes(SDFSchema attributes) {
		for (SDFAttribute a : attributes) {
			addGroupingAttribute(a);
		}
	}

	@Parameter(name = "GROUP_BY", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true)
	public void setGroupingAttributes(List<SDFAttribute> attributes) {
		for (SDFAttribute a : attributes) {
			addGroupingAttribute(a);
		}
	}

	@Parameter(name = "AGGREGATIONS", type = AggregateItemParameter.class, isList = true)
	public void setAggregationItems(List<AggregateItem> aggregations) {
		for (AggregateItem item : aggregations) {
			addAggregation(item.inAttribute, item.aggregateFunction,
					item.outAttribute);
		}
	}

	@GetParameter(name = "GROUP_BY")
	public List<SDFAttribute> getGroupingAttributes() {
		return Collections.unmodifiableList(groupingAttributes);
	}

	@Override
	public AggregateAO clone() {
		return new AggregateAO(this);
	}

	@Override
	protected SDFSchema getOutputSchemaIntern() {
		SDFSchema outputSchema;
		if (getInputSchema() != null) {
			outputSchema = new SDFSchema(getInputSchema().getURI(),
					outputAttributList);
		} else {
			outputSchema = new SDFSchema("<tmp>", outputAttributList);
		}
		setOutputSchema(outputSchema);
		return outputSchema;
	}

	@Parameter(type = IntegerParameter.class, optional = true)
	public void setDumpAtValueCount(int dumpAtValueCount) {
		this.dumpAtValueCount = dumpAtValueCount;
	}

	public int getDumpAtValueCount() {
		return dumpAtValueCount;
	}

}
