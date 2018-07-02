package de.uniol.inf.is.odysseus.dsp.transform;

import java.util.List;

import com.google.common.collect.ImmutableMap.Builder;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.aggregation.logicaloperator.AggregationAO;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.dsp.aggregation.FFTAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.dsp.logicaloperator.FFTAO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TFFTAORule extends AbstractTransformationRule<FFTAO> implements IAggregationSubstitutionRule<FFTAO> {

	@Override
	public IAggregationFunctionFactory getAggregationFunctionFactory() {
		return new FFTAggregationFunctionFactory();
	}

	@Override
	public List<String> getInputAttributes(FFTAO operator) {
		return operator.getInputAttributes();
	}

	@Override
	public List<String> getOutputAttributes(FFTAO operator) {
		return operator.getOutputAttributes();
	}

	@Override
	public void setAdditionalParameters(FFTAO operator, Builder<String, Object> parameterMapBuilder) {
	}

	@Override
	public AbstractWindowAO getWindowAO(FFTAO operator) {
		final ElementWindowAO window = new ElementWindowAO();
		window.setBaseTimeUnit(operator.getBaseTimeUnit());
		window.setWindowSizeE((long) operator.getWindowSize());
		window.setWindowSlideE((long) operator.getWindowSize());
		return window;
	}
	
	@Override
	public void replaceAO(FFTAO originalAO, AbstractWindowAO windowAO, AggregationAO aggregationAO,
			TransformationConfiguration config) {
		replace(originalAO, windowAO, config);
		retract(originalAO);
		insert(windowAO);
		LogicalPlan.insertOperatorBefore(aggregationAO, windowAO);
		insert(aggregationAO);
	}
	
	@Override
	public void setAdditionalAggregationSettings(AggregationAO aggregationAO) {
		aggregationAO.setEvaluateAtNewElement(false);
		aggregationAO.setEvaluateBeforeRemovingOutdatingElements(true);
	}

	@Override
	public boolean isExecutable(FFTAO operator, TransformationConfiguration config) {
		return true;
	}

	@Override
	public void execute(FFTAO operator, TransformationConfiguration config) throws RuleException {
		new AggregationSubstitutionRuleExecutor<FFTAO>(this).execute(operator, config);
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SUBSTITUTION;
	}
}
