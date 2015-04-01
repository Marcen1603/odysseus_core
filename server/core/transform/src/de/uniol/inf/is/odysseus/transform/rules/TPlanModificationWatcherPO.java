package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.PlanModificationWatcherAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.PlanModificationWatcherPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TPlanModificationWatcherPO extends
		AbstractTransformationRule<PlanModificationWatcherAO> {

	@Override
	public void execute(PlanModificationWatcherAO operator,
			TransformationConfiguration config) throws RuleException {
		IServerExecutor executor = config.getOption(IServerExecutor.class.getName());
		if (executor == null){
			throw new TransformationException("Cannot create PlanModificationWatcherPO. Executor not set in Transformation Configuration!");
		}
		PlanModificationWatcherPO po = new PlanModificationWatcherPO(executor);
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(PlanModificationWatcherAO operator,
			TransformationConfiguration config) {
		return true;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super PlanModificationWatcherAO> getConditionClass() {
		return PlanModificationWatcherAO.class;
	}

}
