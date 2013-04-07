package de.uniol.inf.is.odysseus.hmm.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.hmm.logicaloperator.HmmAO;
import de.uniol.inf.is.odysseus.hmm.logicaloperator.VectorquantizationAO;
import de.uniol.inf.is.odysseus.hmm.physicaloperator.HmmPO;
import de.uniol.inf.is.odysseus.hmm.physicaloperator.VectorquantizationOrientationPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class THmmAORule extends
		AbstractTransformationRule<HmmAO> {

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void execute(HmmAO operator, TransformationConfiguration config) {
		defaultExecute(operator, new HmmPO(), config, true, true);

	}

	@Override
	public boolean isExecutable(HmmAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "HmmAO -> HmmPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super HmmAO> getConditionClass() {
		return HmmAO.class;
	}
}
