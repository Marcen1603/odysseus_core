package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.logicaloperator.RangeFragmentAO;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.physicaloperator.RangeFragmentPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * The rule of transformation for the {@link RangeFragmentAO}. 
 * @author Michael Brand
 */
public class TRangeFragmentAORule extends
		AbstractTransformationRule<RangeFragmentAO> {

	@Override
	 public int getPriority() {
	 
		 return 0;
		 
	 }
	
	@SuppressWarnings("rawtypes")
	@Override
	public void execute(RangeFragmentAO fragmentAO, TransformationConfiguration config) { 
		
		defaultExecute(fragmentAO, new RangeFragmentPO(fragmentAO), config, true, true);
		 
	}

	@Override
	public boolean isExecutable(RangeFragmentAO fragmentAO, TransformationConfiguration transformConfig) {
		 
		return fragmentAO.isAllPhysicalInputSet();
		
	}

	@Override
	public String getName() {
		
		return "RangeFragmentAO -> RangeFragmentPO";
		
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		
		return TransformRuleFlowGroup.TRANSFORMATION;
		
	}
	
	@Override
	public Class<? super RangeFragmentAO> getConditionClass() {	
		
		return RangeFragmentAO.class;
		
	}

}