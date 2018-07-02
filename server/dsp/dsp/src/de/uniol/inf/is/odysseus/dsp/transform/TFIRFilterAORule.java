package de.uniol.inf.is.odysseus.dsp.transform;

import java.util.Collections;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.aggregation.logicaloperator.AggregationAO;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.dsp.aggregation.FIRFilterAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.dsp.logicaloperator.FIRFilterAO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TFIRFilterAORule extends AbstractTransformationRule<FIRFilterAO> {

	@Override
	public void execute(FIRFilterAO operator, TransformationConfiguration config) throws RuleException {
		
		final Builder<String, Object> parameterBuilder = ImmutableMap.<String, Object>builder().put("function", "fir").put("coefficients", operator.getCoefficients());

		if (operator.getInputAttributes() != null) {
			parameterBuilder.put(AggregationFunctionParseOptionsHelper.INPUT_ATTRIBUTES, operator.getInputAttributes());
		}
		if (operator.getOutputAttributes() != null) {
			parameterBuilder.put(AggregationFunctionParseOptionsHelper.OUTPUT_ATTRIBUTES, operator.getOutputAttributes());
		}
		
		final DirectAttributeResolver attributeResolver = new DirectAttributeResolver(operator.getInputSchema());
		final FIRFilterAggregationFunctionFactory aggregationFunctionFactory = new FIRFilterAggregationFunctionFactory();
		final IAggregationFunction aggregationFunction = aggregationFunctionFactory.createInstance(parameterBuilder.build(), attributeResolver);

		final AggregationAO aggregationAO = new AggregationAO();
		aggregationAO.setAggregations(Collections.singletonList(aggregationFunction));

		final ElementWindowAO elementWindowAO = new ElementWindowAO();
		replace(operator, elementWindowAO, config);
		retract(operator);
		insert(elementWindowAO);
		elementWindowAO.setWindowSizeE(6l);

		LogicalPlan.insertOperatorBefore(aggregationAO, elementWindowAO);
		insert(aggregationAO);

	}

	@Override
	public boolean isExecutable(FIRFilterAO operator, TransformationConfiguration config) {
		return true;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SUBSTITUTION;
	}

}
