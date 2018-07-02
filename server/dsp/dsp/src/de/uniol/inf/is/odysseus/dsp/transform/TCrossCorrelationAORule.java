package de.uniol.inf.is.odysseus.dsp.transform;

import java.util.List;

import com.google.common.collect.ImmutableMap.Builder;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.aggregation.logicaloperator.AggregationAO;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.dsp.aggregation.CrossCorrelationAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.dsp.logicaloperator.CrossCorrelationAO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TCrossCorrelationAORule extends AbstractTransformationRule<CrossCorrelationAO>
		implements IAggregationSubstitutionRule<CrossCorrelationAO> {

	@Override
	public IAggregationFunctionFactory getAggregationFunctionFactory() {
		return new CrossCorrelationAggregationFunctionFactory();
	}

	@Override
	public List<String> getInputAttributes(CrossCorrelationAO operator) {
		return operator.getInputAttributes();
	}

	@Override
	public List<String> getOutputAttributes(CrossCorrelationAO operator) {
		return operator.getOutputAttributes();
	}

	@Override
	public void setAdditionalParameters(CrossCorrelationAO operator, Builder<String, Object> parameterMapBuilder) {
	}

	@Override
	public AbstractWindowAO getWindowAO(CrossCorrelationAO operator) {
		final ElementWindowAO window = new ElementWindowAO();
		window.setBaseTimeUnit(operator.getBaseTimeUnit());
		window.setWindowSizeE((long) operator.getWindowSize());
		return window;
	}

	@Override
	public void replaceAO(CrossCorrelationAO originalAO, AbstractWindowAO windowAO, AggregationAO aggregationAO) {
		LogicalPlan.insertOperatorBefore(windowAO, originalAO);
		insert(windowAO);
		LogicalPlan.insertOperatorBefore(aggregationAO, windowAO);
		insert(aggregationAO);
		LogicalPlan.removeOperator(originalAO, false);
		retract(originalAO);
	}

	@Override
	public void setAdditionalAggregationSettings(AggregationAO aggregationAO) {
	}

	@Override
	public boolean isExecutable(CrossCorrelationAO operator, TransformationConfiguration config) {
		return true;
	}

	@Override
	public void execute(CrossCorrelationAO operator, TransformationConfiguration config) throws RuleException {
		new AggregationSubstitutionRuleExecutor<CrossCorrelationAO>(this).execute(operator);
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SUBSTITUTION;
	}
}
