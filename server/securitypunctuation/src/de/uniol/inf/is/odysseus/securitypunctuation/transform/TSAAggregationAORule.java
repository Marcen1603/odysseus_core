package de.uniol.inf.is.odysseus.securitypunctuation.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.INonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator.SAAggregationAO;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SAAggregationPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSAAggregationAORule extends AbstractTransformationRule<SAAggregationAO>{

	@SuppressWarnings({ "unchecked"})
	public void execute(final SAAggregationAO operator, final TransformationConfiguration config) throws RuleException {
		final List<INonIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>> nonIncrementalFunctions = new ArrayList<>();
		final List<IIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>> incrementalFunctions = new ArrayList<>();
		String tupleRangeAttribute=operator.getTupleRangeAttribute();
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

		final SAAggregationPO<ITimeInterval, Tuple<ITimeInterval>> po = new SAAggregationPO<ITimeInterval,Tuple<ITimeInterval>>(nonIncrementalFunctions,
				incrementalFunctions, evaluateAtOutdatingElements, evaluateBeforeRemovingOutdatingElements, evaluateAtNewElement, evaluateAtDone,
				outputOnlyChanges, outputSchema, groupingAttributesIndices,tupleRangeAttribute);
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(SAAggregationAO operator, TransformationConfiguration config) {
	return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public Class<? super SAAggregationAO> getConditionClass() {
		return SAAggregationAO.class;
	}
	
	@Override
	public String getName() {
		return "SAAggregationAO -> SAAggregationPO";
	}

}
