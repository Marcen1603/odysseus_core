package de.uniol.inf.is.odysseus.p2p_new.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.DummyAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.DummyReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.DummySenderPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"rawtypes"})
public class TDummyAORule extends AbstractTransformationRule<DummyAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(DummyAO dummyAO, TransformationConfiguration config) throws RuleException {
		// quelle
		if(dummyAO.getSubscribedToSource()==null || dummyAO.getSubscribedToSource().isEmpty()) {
			defaultExecute(dummyAO, new DummyReceiverPO(dummyAO), config, true, true);			
		}
		// senke
		else {
			defaultExecute(dummyAO, new DummySenderPO(dummyAO), config, true, true);	
		}
	}

	@Override
	public boolean isExecutable(DummyAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "DummyAO -> DummyPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	 public Class<? super DummyAO> getConditionClass() { 
		return DummyAO.class;
	 }
}
