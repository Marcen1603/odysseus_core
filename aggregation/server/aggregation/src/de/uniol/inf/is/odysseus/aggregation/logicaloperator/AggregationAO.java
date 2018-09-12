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
import de.uniol.inf.is.odysseus.core.logicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.logicaloperator.IParallelizableOperator;
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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * @author Cornelius Ludmann
 *
 */
@LogicalOperator(name = "AGGREGATION", minInputPorts = 1, maxInputPorts = 1, doc = "Aggretations on inputAttributeIndices e.g Min, Max, Count, Avg, Sum and grouping.", url = "http://odysseus.informatik.uni-oldenburg.de:8090/display/ODYSSEUS/Aggregate+%28and+Group%29+operator", category = {
		LogicalOperatorCategory.BASE })
public class AggregationAO extends UnaryLogicalOp implements IStatefulAO, IParallelizableOperator {

	private static final long serialVersionUID = -94620121792280046L;

	private List<SDFAttribute> groupingAttributes;

	private List<IAggregationFunction> functions;

	/**
	 * This flag is set if this operator should output new elements when elements
	 * get outdated.
	 */
	private boolean evaluateAtOutdatingElements = true;

	private boolean evaluateBeforeRemovingOutdatingElements = false;

	/**
	 * This flag is set if this operator should output new elements when elements
	 * get valid.
	 */
	private boolean evaluateAtNewElement = true;

	/**
	 * This flag is set if this operator should output the last output element at
	 * done. This can be used when you want only the final aggr. value in an
	 * evaluation. E. g., the final AVG of the latency.
	 */
	private boolean evaluateAtDone = false;
	
	/** 
	 * If this flag is set to true, the operator generate additionally output 
	 * for each incomming punctuation
	 */
	private boolean createOutputOnPunctuation = false;

	private boolean outputOnlyChanges = false;
	
	/**
	 * This flag is set if this operator should consider other meta data than time intervals (works for non-incremental aggregation functions only)
	 */
	private boolean supressFullMetaDataHandling = false;

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
		evaluateAtDone = op.evaluateAtDone;
		evaluateAtNewElement = op.evaluateAtNewElement;
		evaluateAtOutdatingElements = op.evaluateAtOutdatingElements;
		evaluateBeforeRemovingOutdatingElements = op.evaluateBeforeRemovingOutdatingElements;
		outputOnlyChanges = op.outputOnlyChanges;
		supressFullMetaDataHandling = op.supressFullMetaDataHandling;
		createOutputOnPunctuation = op.createOutputOnPunctuation;
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
		if (groupingAttributes == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(groupingAttributes);
	}

	public int[] getGroupingAttributeIndicesOnOutputSchema() {
		if (groupingAttributes == null) {
			return new int[] {};
		}
		int[] groupingAttributeIndices = new int[groupingAttributes.size()];
		for (int i = 0; i < groupingAttributes.size(); i++) {
			// The grouping attributes in the output schema are always at the beginning of
			// the tuple
			groupingAttributeIndices[i] = i;
		}
		return groupingAttributeIndices;
	}

	@Parameter(name = "AGGREGATIONS", type = AggregationItemParameter.class, isList = true)
	public void setAggregations(final List<IAggregationFunction> aggregations) {
		functions = aggregations;
	}

	@GetParameter(name = "AGGREGATIONS")
	public List<IAggregationFunction> getAggregations() {
		return Collections.unmodifiableList(functions);
	}

	/**
	 * @return the evaluateAtOutdatingElements
	 */
	@GetParameter(name = "EVAL_AT_OUTDATING")
	public boolean isEvaluateAtOutdatingElements() {
		return evaluateAtOutdatingElements;
	}

	/**
	 * @param evaluateAtOutdatingElements
	 *            the evaluateAtOutdatingElements to set
	 */
	@Parameter(name = "EVAL_AT_OUTDATING", type = BooleanParameter.class, optional = true)
	public void setEvaluateAtOutdatingElements(final boolean evaluateAtOutdatingElements) {
		this.evaluateAtOutdatingElements = evaluateAtOutdatingElements;
	}

	@GetParameter(name = "EVAL_BEFORE_REMOVE_OUTDATING")
	public boolean isEvaluateBeforeRemovingOutdatingElements() {
		return evaluateBeforeRemovingOutdatingElements;
	}

	@Parameter(name = "EVAL_BEFORE_REMOVE_OUTDATING", type = BooleanParameter.class, optional = true)
	public void setEvaluateBeforeRemovingOutdatingElements(final boolean evaluateBeforeRemovingOutdatingElements) {
		this.evaluateBeforeRemovingOutdatingElements = evaluateBeforeRemovingOutdatingElements;
	}

	/**
	 * @return the evaluateAtNewElement
	 */
	@GetParameter(name = "EVAL_AT_NEW_ELEMENT")
	public boolean isEvaluateAtNewElement() {
		return evaluateAtNewElement;
	}

	/**
	 * @param evaluateAtNewElement
	 *            the evaluateAtNewElement to set
	 */
	@Parameter(name = "EVAL_AT_NEW_ELEMENT", type = BooleanParameter.class, optional = true)
	public void setEvaluateAtNewElement(final boolean evaluateAtNewElement) {
		this.evaluateAtNewElement = evaluateAtNewElement;
	}

	/**
	 * @return the evaluateAtDone
	 */
	@GetParameter(name = "EVAL_AT_DONE")
	public boolean isEvaluateAtDone() {
		return evaluateAtDone;
	}

	/**
	 * @param evaluateAtDone
	 *            the evaluateAtDone to set
	 */
	@Parameter(name = "EVAL_AT_DONE", type = BooleanParameter.class, optional = true)
	public void setEvaluateAtDone(final boolean evaluateAtDone) {
		this.evaluateAtDone = evaluateAtDone;
	}
	
	@Parameter(type = BooleanParameter.class, optional = true)
	public void setCreateOutputOnPunctuation(boolean createOutputOnPunctuation) {
		this.createOutputOnPunctuation = createOutputOnPunctuation;
	}
	
	public boolean isCreateOutputOnPunctuation() {
		return createOutputOnPunctuation;
	}

	@GetParameter(name = "OUTPUT_ONLY_CHANGES")
	public boolean isOutputOnlyChanges() {
		return outputOnlyChanges;
	}

	@Parameter(name = "OUTPUT_ONLY_CHANGES", type = BooleanParameter.class, optional = true)
	public void setOutputOnlyChanges(final boolean outputOnlyChanges) {
		this.outputOnlyChanges = outputOnlyChanges;
	}
	
	@GetParameter(name = "SUPPRESS_FULL_META_DATA_HANDLING")
	public boolean isSupressFullMetaDataHandling(){
		return supressFullMetaDataHandling;
	}
	
	@Parameter(name = "SUPPRESS_FULL_META_DATA_HANDLING", type = BooleanParameter.class, optional = true)
	public void setSupressFullMetaDataHandling(final boolean processMetaData){
		this.supressFullMetaDataHandling = processMetaData;
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

	@Override
	public OperatorStateType getStateType() {
		if (this.groupingAttributes.isEmpty()) {
			return IOperatorState.OperatorStateType.ARBITRARY_STATE;
		} else {
			return IOperatorState.OperatorStateType.PARTITIONED_STATE;
		}
	}

	@Override
	public boolean isParallelizable() {
		// FIXME check predicate
		return true;
	}
}
