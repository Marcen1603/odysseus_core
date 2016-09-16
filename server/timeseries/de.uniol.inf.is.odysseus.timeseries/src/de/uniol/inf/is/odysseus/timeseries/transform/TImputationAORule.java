package de.uniol.inf.is.odysseus.timeseries.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.timeseries.imputation.IImputation;
import de.uniol.inf.is.odysseus.timeseries.imputation.ImputationFactory;
import de.uniol.inf.is.odysseus.timeseries.logicaloperator.ImputationAO;
import de.uniol.inf.is.odysseus.timeseries.physicaloperator.ImputationPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christoph Schröer
 *
 */
public class TImputationAORule extends AbstractTransformationRule<ImputationAO> {

	private ImputationFactory<ITimeInterval> imputationFactory;

	public TImputationAORule() {
		super();
		this.imputationFactory = new ImputationFactory<>();
	}

	@Override
	public void execute(final ImputationAO operator, final TransformationConfiguration config) throws RuleException {

		IImputation<ITimeInterval> imputation = this.imputationFactory.createImputation(
				operator.getImputationStrategy(), operator.getImputationWindowSize(), operator.getOptionsMap());

		defaultExecute(operator, new ImputationPO(imputation), config, true, true);
	}

	@Override
	public boolean isExecutable(final ImputationAO operator, final TransformationConfiguration config) {
		// QN: Welche Bedeutung hat dies?
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {

		// QN: Welche Bedeutung hat dies?
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
}
