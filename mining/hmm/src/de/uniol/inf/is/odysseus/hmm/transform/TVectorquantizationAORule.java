package de.uniol.inf.is.odysseus.hmm.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.hmm.logicaloperator.VectorquantizationAO;
import de.uniol.inf.is.odysseus.hmm.physicaloperator.VectorquantizationOrientationPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({ "rawtypes" })
public class TVectorquantizationAORule extends
		AbstractTransformationRule<VectorquantizationAO> {

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void execute(VectorquantizationAO operator, TransformationConfiguration config) {
		defaultExecute(operator, new VectorquantizationOrientationPO(), config, true, true);

	}

	@Override
	public boolean isExecutable(VectorquantizationAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "VectorquantizationAO -> VectorquantizationOrientationPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super VectorquantizationAO> getConditionClass() {
		return VectorquantizationAO.class;
	}
}
