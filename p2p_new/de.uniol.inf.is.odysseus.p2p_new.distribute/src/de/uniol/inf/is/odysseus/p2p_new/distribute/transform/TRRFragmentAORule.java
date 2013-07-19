package de.uniol.inf.is.odysseus.p2p_new.distribute.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.distribute.logicaloperator.RRFragmentAO;
import de.uniol.inf.is.odysseus.p2p_new.distribute.physicaloperator.RRFragmentPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * The rule of transformation for the {@link RRFragmentAO}. Any {@link RRFragmentAO} will be 
 * transformed into a new {@link RRFragmentPO}.
 * @author Michael Brand
 */
public class TRRFragmentAORule extends AbstractTransformationRule<RRFragmentAO> {
	
	@Override
	 public int getPriority() {
	 
		 return 0;
		 
	 }
	 
	  
	 @SuppressWarnings("rawtypes")
	@Override
	 public void execute(RRFragmentAO fragmentAO, TransformationConfiguration config) { 
		 
		 defaultExecute(fragmentAO, new RRFragmentPO(fragmentAO), config, true, true);
		 
	 }
	 
	 @Override
	public boolean isExecutable(RRFragmentAO fragmentAO, TransformationConfiguration transformConfig) {
		 
		return fragmentAO.isAllPhysicalInputSet();
		
	}

	@Override
	public String getName() {
		
		return "RRFragmentAO -> RRFragmentPO";
		
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		
		return TransformRuleFlowGroup.TRANSFORMATION;
		
	}
	
	@Override
	public Class<? super RRFragmentAO> getConditionClass() {	
		
		return RRFragmentAO.class;
		
	}

}