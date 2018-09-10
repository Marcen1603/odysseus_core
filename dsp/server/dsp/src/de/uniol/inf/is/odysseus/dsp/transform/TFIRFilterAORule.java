package de.uniol.inf.is.odysseus.dsp.transform;

import java.util.List;

import com.google.common.collect.ImmutableMap.Builder;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.aggregation.logicaloperator.AggregationAO;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.dsp.aggregation.FIRFilterAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.dsp.logicaloperator.FIRFilterAO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TFIRFilterAORule extends AbstractTransformationRule<FIRFilterAO>
		implements IAggregationSubstitutionRule<FIRFilterAO> {

	@Override
	public IAggregationFunctionFactory getAggregationFunctionFactory() {
		return new FIRFilterAggregationFunctionFactory();
	}

	@Override
	public List<String> getInputAttributes(FIRFilterAO operator) {
		return operator.getInputAttributes();
	}

	@Override
	public List<String> getOutputAttributes(FIRFilterAO operator) {
		return operator.getOutputAttributes();
	}

	@Override
	public void setAdditionalParameters(FIRFilterAO operator, Builder<String, Object> parameterMapBuilder) {
		parameterMapBuilder.put(FIRFilterAggregationFunctionFactory.COEFFICIENTS, operator.getCoefficients());
	}

	@Override
	public AbstractWindowAO getWindowAO(FIRFilterAO operator) {
		final ElementWindowAO window = new ElementWindowAO();
		window.setBaseTimeUnit(operator.getBaseTimeUnit());
		window.setWindowSizeE((long) operator.getCoefficients().size());
		return window;
	}

	@Override
	public void replaceAO(FIRFilterAO originalAO, AbstractWindowAO windowAO, AggregationAO aggregationAO) {
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
	public boolean isExecutable(FIRFilterAO operator, TransformationConfiguration config) {
		return true;
	}

	@Override
	public void execute(FIRFilterAO operator, TransformationConfiguration config) throws RuleException {
		new AggregationSubstitutionRuleExecutor<FIRFilterAO>(this).execute(operator);
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SUBSTITUTION;
	}
}
