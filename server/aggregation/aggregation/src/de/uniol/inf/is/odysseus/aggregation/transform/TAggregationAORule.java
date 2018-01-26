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
package de.uniol.inf.is.odysseus.aggregation.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.INonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.logicaloperator.AggregationAO;
import de.uniol.inf.is.odysseus.aggregation.physicaloperator.AggregationPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Cornelius Ludmann
 *
 */
public class TAggregationAORule extends AbstractTransformationRule<AggregationAO> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
	 * java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(final AggregationAO operator, final TransformationConfiguration config) throws RuleException {
		
		// temp check to avoid aggreation in scenarios where more that timeinterval is used --> aggregation does not handle
		// metadata correctly in this case
		
		if (operator.getInputSchema().getMetaAttributeNames().size() > 1 || operator.getInputSchema().getMetaAttributeNames().get(0)!=ITimeInterval.class.getName()) {
			throw new TransformationException("Aggregation currently only works with #METADATA TimeInterval! Use Aggregate instead");
		}
		
		final List<INonIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>> nonIncrementalFunctions = new ArrayList<>();
		final List<IIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>> incrementalFunctions = new ArrayList<>();

		final List<IAggregationFunction> functions = operator.getAggregations();
		for (final IAggregationFunction f : functions) {
			if (!f.isIncremental()) {
				nonIncrementalFunctions
						.add((INonIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>) f);
			} else {
				incrementalFunctions.add((IIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>) f);
			}
		}

		final boolean evaluateAtOutdatingElements = operator.isEvaluateAtOutdatingElements();
		final boolean evaluateBeforeRemovingOutdatingElements = operator.isEvaluateBeforeRemovingOutdatingElements();
		final boolean evaluateAtNewElement = operator.isEvaluateAtNewElement();
		final boolean evaluateAtDone = operator.isEvaluateAtDone();
		final boolean outputOnlyChanges = operator.isOutputOnlyChanges();
		final SDFSchema outputSchema = operator.getOutputSchema();
		final SDFSchema inputSchema = operator.getInputSchema();
		final List<SDFAttribute> groupingAttributes = operator.getGroupingAttributes();
		final int[] groupingAttributesIndices = new int[groupingAttributes.size()];
		for (int i = 0; i < groupingAttributes.size(); ++i) {
			groupingAttributesIndices[i] = inputSchema.indexOf(groupingAttributes.get(i));
		}
		final int[] groupingAttributeIndicesOutputSchema = operator.getGroupingAttributeIndicesOnOutputSchema();

		final AggregationPO<ITimeInterval, Tuple<ITimeInterval>> po = new AggregationPO<>(nonIncrementalFunctions,
				incrementalFunctions, evaluateAtOutdatingElements, evaluateBeforeRemovingOutdatingElements, evaluateAtNewElement, evaluateAtDone,
				outputOnlyChanges, outputSchema, groupingAttributesIndices, groupingAttributeIndicesOutputSchema);
		defaultExecute(operator, po, config, true, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang.
	 * Object, java.lang.Object)
	 */
	@Override
	public boolean isExecutable(final AggregationAO operator, final TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
	 */
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
