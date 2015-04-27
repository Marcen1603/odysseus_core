package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.PlanModificationActionAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.PlanModificationActionPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TPlanModificationActionAORule extends AbstractTransformationRule<PlanModificationActionAO> {

	@Override
	public void execute(PlanModificationActionAO operator, TransformationConfiguration config) throws RuleException {
		IServerExecutor executor = config.getOption(IServerExecutor.class.getName());
		if (executor == null){
			throw new TransformationException("Cannot create PlanModificationActionPO. Executor not set in Transformation Configuration!");
		}
		
		PlanModificationActionPO po = new PlanModificationActionPO(operator, executor); 
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(PlanModificationActionAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super PlanModificationActionAO> getConditionClass() {
		return PlanModificationActionAO.class;
	}
}
