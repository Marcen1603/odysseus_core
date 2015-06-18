package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.ShuffleFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.physicaloperator.ShuffleFragmentPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TShuffleFragmentAORule extends AbstractTransformationRule<ShuffleFragmentAO>  {

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(ShuffleFragmentAO fragmentAO,
			TransformationConfiguration config) throws RuleException {
		defaultExecute(fragmentAO, new ShuffleFragmentPO(fragmentAO), config, true, true);
	}

	@Override
	public boolean isExecutable(ShuffleFragmentAO fragmentAO,
			TransformationConfiguration config) {
		return fragmentAO.isAllPhysicalInputSet();
	}
	
	@Override
	public String getName() {
		return "ShuffleFragmentAO -> ShuffleFragmentPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super ShuffleFragmentAO> getConditionClass() {	
		return ShuffleFragmentAO.class;
	}

}
