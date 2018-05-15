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

import java.util.List;
import java.util.stream.Collectors;

import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.INonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.logicaloperator.AggregationAO;
import de.uniol.inf.is.odysseus.aggregation.physicaloperator.AggregationPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
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
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
	 * java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(final AggregationAO operator, final TransformationConfiguration config) throws RuleException {

		@SuppressWarnings("rawtypes")
		IMetadataMergeFunction mf = getMetadataMergeFunction(operator);

		final List<INonIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>> nonIncrementalFunctions = getNonIncrementalFunction(
				operator.getAggregations(), operator.getInputSchema());
		final List<IIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>> incrementalFunctions = getIncrementalFunction(
				operator.getAggregations());

		operator.getInputSchema();

		/*
		 * If there is at least one non-incremental function, the SweepArea is used and
		 * hence, the merge of metadata for other types of metadata works. If there is
		 * no non-incremental function, the operator currently cannot handle other types
		 * of metadata. BUT: When having multiple metadatas, simply use the SweepArea
		 * all the time.
		 */
		boolean hasMoreThanOneMetadata = operator.getInputSchema().getMetaAttributeNames().size() > 1;
		boolean hasNonIncrementalFunction = nonIncrementalFunctions.size() > 0;
		boolean hasOnlyOneMetadataButOtherThanTimeInterval = !hasMoreThanOneMetadata
				&& operator.getInputSchema().getMetaAttributeNames().get(0) != ITimeInterval.class.getName();
		boolean alwaysUseSweepArea = false;

		if (hasMoreThanOneMetadata && !hasNonIncrementalFunction || hasOnlyOneMetadataButOtherThanTimeInterval) {
			alwaysUseSweepArea = true;
		}

		final boolean evaluateAtOutdatingElements = operator.isEvaluateAtOutdatingElements();
		final boolean evaluateBeforeRemovingOutdatingElements = operator.isEvaluateBeforeRemovingOutdatingElements();
		final boolean evaluateAtNewElement = operator.isEvaluateAtNewElement();
		final boolean evaluateAtDone = operator.isEvaluateAtDone();
		final boolean outputOnlyChanges = operator.isOutputOnlyChanges();
		final SDFSchema outputSchema = getOutputSchema(operator);
		final SDFSchema inputSchema = operator.getInputSchema();
		final List<SDFAttribute> groupingAttributes = operator.getGroupingAttributes();
		final int[] groupingAttributesIndices = new int[groupingAttributes.size()];
		for (int i = 0; i < groupingAttributes.size(); ++i) {
			groupingAttributesIndices[i] = inputSchema.indexOf(groupingAttributes.get(i));
		}
		final int[] groupingAttributeIndicesOutputSchema = operator.getGroupingAttributeIndicesOnOutputSchema();
		final boolean supressFullMetaDataHandling = operator.isSupressFullMetaDataHandling();

		final AggregationPO<ITimeInterval, Tuple<ITimeInterval>> po = new AggregationPO<>(nonIncrementalFunctions,
				incrementalFunctions, evaluateAtOutdatingElements, evaluateBeforeRemovingOutdatingElements,
				evaluateAtNewElement, evaluateAtDone, outputOnlyChanges, outputSchema, groupingAttributesIndices,
				groupingAttributeIndicesOutputSchema, supressFullMetaDataHandling, mf, alwaysUseSweepArea);

		defaultExecute(operator, po, config, true, true);
	}

	protected SDFSchema getOutputSchema(AggregationAO operator) {
		return operator.getOutputSchema();
	}

	@SuppressWarnings("rawtypes")
	protected IMetadataMergeFunction getMetadataMergeFunction(AggregationAO operator) {
		List<String> metadataSet = operator.getInputSchema().getMetaAttributeNames();
		// Attention: Time meta data is set in aggregation
		metadataSet.remove(ITimeInterval.class.getName());
		IMetadataMergeFunction mf = MetadataRegistry.getMergeFunction(metadataSet);
		return mf;
	}

	@SuppressWarnings("unchecked")
	protected List<INonIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>> getNonIncrementalFunction(
			List<IAggregationFunction> allFunctions, SDFSchema inputSchema) {
		return allFunctions.stream().filter(f -> !f.isIncremental())
				.map(f -> ((INonIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>) f))
				.collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	protected List<IIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>> getIncrementalFunction(
			List<IAggregationFunction> allFunctions) {
		return allFunctions.stream().filter(f -> f.isIncremental())
				.map(f -> ((IIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>) f))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang.
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
