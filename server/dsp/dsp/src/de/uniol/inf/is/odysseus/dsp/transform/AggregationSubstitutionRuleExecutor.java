package de.uniol.inf.is.odysseus.dsp.transform;

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.aggregation.logicaloperator.AggregationAO;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;

public class AggregationSubstitutionRuleExecutor<T extends UnaryLogicalOp> {

	private final IAggregationSubstitutionRule<T> aggregationSubstitutionRule;

	public AggregationSubstitutionRuleExecutor(IAggregationSubstitutionRule<T> aggregationSubstitutionRule) {
		this.aggregationSubstitutionRule = aggregationSubstitutionRule;
	}

	public void execute(T operator, TransformationConfiguration config) throws RuleException {
		final AbstractWindowAO windowAO = aggregationSubstitutionRule.getWindowAO(operator);
		final AggregationAO aggregationAO = buildAggregationAO(operator);
		aggregationSubstitutionRule.replaceAO(operator, windowAO, aggregationAO, config);
	}

	private AggregationAO buildAggregationAO(T operator) {
		final IAggregationFunctionFactory factory = aggregationSubstitutionRule.getAggregationFunctionFactory();
		final DirectAttributeResolver attributeResolver = new DirectAttributeResolver(operator.getInputSchema());
		final IAggregationFunction aggregationFunction = factory
				.createInstance(buildAggregationParameters(operator, factory), attributeResolver);
		final AggregationAO aggregationAO = new AggregationAO();
		aggregationSubstitutionRule.setAdditionalAggregationSettings(aggregationAO);
		aggregationAO.setAggregations(Collections.singletonList(aggregationFunction));
		return aggregationAO;
	}

	private Map<String, Object> buildAggregationParameters(T operator,
			IAggregationFunctionFactory aggregationFunctionFactory) {
		final Builder<String, Object> parameterBuilder = ImmutableMap.<String, Object>builder()
				.put(AggregationFunctionParseOptionsHelper.FUNCTION_NAME, aggregationFunctionFactory.getFunctionName());
		if (aggregationSubstitutionRule.getInputAttributes(operator) != null) {
			parameterBuilder.put(AggregationFunctionParseOptionsHelper.INPUT_ATTRIBUTES,
					aggregationSubstitutionRule.getInputAttributes(operator));
		}
		if (aggregationSubstitutionRule.getOutputAttributes(operator) != null) {
			parameterBuilder.put(AggregationFunctionParseOptionsHelper.OUTPUT_ATTRIBUTES,
					aggregationSubstitutionRule.getOutputAttributes(operator));
		}
		aggregationSubstitutionRule.setAdditionalParameters(operator, parameterBuilder);
		return parameterBuilder.build();
	}

}
