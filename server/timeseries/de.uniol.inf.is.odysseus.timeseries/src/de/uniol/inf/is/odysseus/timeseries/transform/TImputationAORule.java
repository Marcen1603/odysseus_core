package de.uniol.inf.is.odysseus.timeseries.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.timeseries.imputation.IImputation;
import de.uniol.inf.is.odysseus.timeseries.imputation.ImputationRegistry;
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

	public TImputationAORule() {
		super();
	}

	@Override
	public void execute(final ImputationAO operator, final TransformationConfiguration config) throws RuleException {

		IImputation<Tuple<ITimeInterval>, ITimeInterval> imputation = ImputationRegistry.getInstance()
				.createInstance(operator.getImputationStrategy(), operator.getOptionsMap());

		ImputationPO<ITimeInterval> imputationPO = new ImputationPO<ITimeInterval>(imputation);

		defaultExecute(operator, imputationPO, config, true, true);
	}

	@Override
	public boolean isExecutable(final ImputationAO operator, final TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
}
