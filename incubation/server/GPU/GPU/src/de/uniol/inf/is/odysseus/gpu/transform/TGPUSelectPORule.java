package de.uniol.inf.is.odysseus.gpu.transform;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.gpu.physicaloperator.GpuSelectPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TGPUSelectPORule extends AbstractTransformationRule<SelectAO> {

	@Override
	public int getPriority() {
		return 100; // TODO: muss geändert
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void execute(SelectAO operator, TransformationConfiguration config) throws RuleException {
		// select ao durch gpu po ersetzen
		GpuSelectPO po = new GpuSelectPO(operator);
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(SelectAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super SelectAO> getConditionClass() {
		// TODO Auto-generated method stub
		return SelectAO.class;
	}
}
