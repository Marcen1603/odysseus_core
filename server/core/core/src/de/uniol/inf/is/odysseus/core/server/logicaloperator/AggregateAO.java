/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItemParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;

@LogicalOperator(name = "AGGREGATE", minInputPorts = 1, maxInputPorts = 1, doc = "Aggretations on attributes e.g Min, Max, Count, Avg, Sum and grouping.", url = "http://odysseus.offis.uni-oldenburg.de:8090/display/ODYSSEUS/Aggregate+%28and+Group%29+operator", category = { LogicalOperatorCategory.BASE })
public class AggregateAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 2539966167342852544L;

	final private Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations;
	final private Map<SDFAttribute, AggregateFunction> outAttributeToAggregation;
	final private List<SDFAttribute> groupingAttributes;
	final private List<Pair<SDFAttribute, Boolean>> outputAttributList;

	private List<AggregateItem> aggregationItems;
	private int dumpAtValueCount = -1;
	private boolean outputPA = false;
	private boolean drainAtDone = true;
	private boolean drainAtClose = false;
	private boolean fastGrouping = false;
	
	public static final String AGGREGATIONS = "AGGREGATIONS";

	public AggregateAO() {
		super();
		aggregations = new HashMap<SDFSchema, Map<AggregateFunction, SDFAttribute>>();
		outAttributeToAggregation = new HashMap<>();
		groupingAttributes = new ArrayList<SDFAttribute>();
		outputAttributList = new ArrayList<>();
	}

	public AggregateAO(AggregateAO op) {
		super(op);
		aggregations = new HashMap<SDFSchema, Map<AggregateFunction, SDFAttribute>>(
				op.aggregations);
		outAttributeToAggregation = new HashMap<>(op.outAttributeToAggregation);
		groupingAttributes = new ArrayList<SDFAttribute>(op.groupingAttributes);
		outputAttributList = new ArrayList<>(op.outputAttributList);
		dumpAtValueCount = op.dumpAtValueCount;
		this.outputPA = op.outputPA;
		this.drainAtDone = op.drainAtDone;
		this.fastGrouping  = op.fastGrouping;
		this.drainAtClose = op.drainAtClose;
		this.aggregationItems = op.aggregationItems != null ? Lists.newArrayList(op.aggregationItems) : null;
	}

	public void addAggregation(SDFAttribute attribute,
			AggregateFunction function, SDFAttribute outAttribute) {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(attribute);
		addAggregation(attributes, function, outAttribute);
	}

    public void addAggregation(List<SDFAttribute> attributes, AggregateFunction function, SDFAttribute outAttribute) {
        @SuppressWarnings("unchecked")
		SDFSchema schema = SDFSchemaFactory.createNewSchema("", (Class<? extends IStreamObject<?>>) Tuple.class, attributes);
        addAggregation(schema, function, outAttribute);
    }
	   
	public void addAggregation(SDFSchema attributes,
			AggregateFunction function, SDFAttribute outAttribute) {
		for (Pair<SDFAttribute, Boolean> v : outputAttributList) {
			if (v.getE1().equals(outAttribute)) {
				throw new IllegalArgumentException(
						"multiple definitions of element " + outAttribute);
			}
		}

		outputAttributList.add(new Pair<>(outAttribute, true));
		setOutputSchema(null);
		Map<AggregateFunction, SDFAttribute> af = aggregations.get(attributes);
		if (af == null) {
			af = new HashMap<AggregateFunction, SDFAttribute>();
			aggregations.put(attributes, af);
		}
		af.put(function, outAttribute);
		outAttributeToAggregation.put(outAttribute, function);
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
	
	/**
	 * Removes all existing aggregations (needed for renaming of attributes)
	 * Author: Chris Tönjes-Deye
	 */
	public void clearAggregations(){
		aggregationItems.clear();
		outputAttributList.clear();
		aggregations.clear();
		outAttributeToAggregation.clear();
	}

	@GetParameter(name = AGGREGATIONS)
	public Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> getAggregations() {
		return Collections.unmodifiableMap(this.aggregations);
	}

	public boolean hasAggregations() {
		return !this.aggregations.isEmpty();
	}

	public void addGroupingAttribute(SDFAttribute attribute) {
		if (groupingAttributes.contains(attribute)) {
			return;
		}
		groupingAttributes.add(attribute);
		outputAttributList.add(new Pair<>(attribute, false));
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

	@Parameter(name = AGGREGATIONS, type = AggregateItemParameter.class, isList = true)
	public void setAggregationItems(List<AggregateItem> aggregations) {
		for (AggregateItem item : aggregations) {
			addAggregation(item.inAttributes, item.aggregateFunction,
					item.outAttribute);
		}
		aggregationItems = aggregations;
	}
	
	public List<AggregateItem> getAggregationItems() {
		return aggregationItems;
	}

	@GetParameter(name = "GROUP_BY")
	public List<SDFAttribute> getGroupingAttributes() {
		return Collections.unmodifiableList(groupingAttributes);
	}

	@Override
	public AggregateAO clone() {
		return new AggregateAO(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema outputSchema;
		List<SDFAttribute> outAttribs = new ArrayList<>();

		for (Pair<SDFAttribute, Boolean> a : outputAttributList) {
			if (outputPA && a.getE2()) {
				SDFDatatype type = SDFDatatype.PARTIAL_AGGREGATE;
				AggregateFunction f = this.outAttributeToAggregation.get(a
						.getE1());

				// Determine the real PartialAggregate type
				IAggregateFunctionBuilder builder = AggregateFunctionBuilderRegistry
						.getBuilder(getInputSchema().getType(), f.getName());
				int[] posArray = new int[1];
				posArray[0] = 0;
				IAggregateFunction<?, ?> realAggFunc = builder
						.createAggFunction(f, getInputSchema(), posArray,
								false, type.getURI());
				type = realAggFunc.getPartialAggregateType();

				outAttribs.add(new SDFAttribute(a.getE1().getSourceName(), a
						.getE1().getAttributeName(), type, null, null, null));
			} else {
				outAttribs.add(a.getE1());
			}
		}

		if (getInputSchema() != null) {
			outputSchema = SDFSchemaFactory.createNewWithAttributes(outAttribs, getInputSchema());
		} else {
			outputSchema = SDFSchemaFactory.createNewSchema("<tmp>", (Class<? extends IStreamObject<?>>) Tuple.class, outAttribs);
		}
		return outputSchema;
	}

	@Parameter(type = IntegerParameter.class, optional = true)
	public void setDumpAtValueCount(int dumpAtValueCount) {
		this.dumpAtValueCount = dumpAtValueCount;
	}

	public int getDumpAtValueCount() {
		return dumpAtValueCount;
	}

	@Parameter(type = BooleanParameter.class, optional = true)
	public void setOutputPA(boolean outputPA) {
		this.outputPA = outputPA;
	}

	public boolean isOutputPA() {
		return outputPA;
	}

	@Parameter(name = "drain", type = BooleanParameter.class, optional = true, doc = "If set to true (default), elements are not yet written will be written at done.", deprecated=true)
	public void setDrainAtDoneOld(boolean drainAtDone) {
		this.drainAtDone = drainAtDone;
	}
	
	public boolean isDrainAtDoneOld() {
		return this.drainAtDone;
	}
	
	@Parameter(name = "drainAtDone", type = BooleanParameter.class, optional = true, doc = "If set to true (default), elements are not yet written will be written at done. ")
	public void setDrainAtDone(boolean drainAtDone) {
		this.drainAtDone = drainAtDone;
	}
	

	@Parameter(name = "drainAtClose", type = BooleanParameter.class, optional = true, doc = "If set to true (default is false), elements are not yet written will be written at close. ")
	public void setDrainAtClose(boolean drainAtClose) {
		this.drainAtClose = drainAtClose;
	}

	public boolean isDrainAtDone() {
		return drainAtDone;
	}
	
	public boolean isDrainAtClose() {
		return drainAtClose;
	}
	
	public boolean isFastGrouping() {
		return fastGrouping;
	}

	@Parameter(name = "fastGrouping", type = BooleanParameter.class, optional = true, doc = "Use hash code instead of tuple compare to create group. Potentially unsafe!")
	public void setFastGrouping(boolean fastGrouping) {
		this.fastGrouping = fastGrouping;
	}
}
