package de.uniol.inf.is.odysseus.timeseries.transform;


import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.timeseries.autoregression.estimator.AbstractAutoregressionModelEstimator;
import de.uniol.inf.is.odysseus.timeseries.autoregression.estimator.AutoregressionEstimatorFactory;
import de.uniol.inf.is.odysseus.timeseries.logicaloperator.ModelVarianceAO;
import de.uniol.inf.is.odysseus.timeseries.physicaloperator.ModelVariancePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christoph Schröer
 *
 */
@Deprecated
@SuppressWarnings("all")
public class TModelVarianceAORule extends AbstractTransformationRule<ModelVarianceAO> {

	public TModelVarianceAORule() {
		super();
	}

	@Override
	public void execute(final ModelVarianceAO operator, final TransformationConfiguration config) throws RuleException {

		AutoregressionEstimatorFactory factory = new AutoregressionEstimatorFactory(
				operator.getLearningMode());

		AbstractAutoregressionModelEstimator estimator = factory.createEstimator(operator.getModelName(),
				operator.getModelOptionsMap());

		defaultExecute(operator, new ModelVariancePO(estimator), config, true, false);
	}

	@Override
	public boolean isExecutable(final ModelVarianceAO operator, final TransformationConfiguration config) {
		// QN: Welche Bedeutung hat dies?
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {

		// QN: Welche Bedeutung hat dies?
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
}
