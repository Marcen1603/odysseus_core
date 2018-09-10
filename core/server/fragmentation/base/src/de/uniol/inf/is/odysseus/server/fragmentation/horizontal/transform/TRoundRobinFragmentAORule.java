package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.RoundRobinFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.physicaloperator.RoundRobinFragmentPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * The rule of transformation for the {@link RoundRobinFragmentAO}.
 * @author Michael Brand
 */
public class TRoundRobinFragmentAORule extends AbstractTransformationRule<RoundRobinFragmentAO> {
	
	@Override
	 public int getPriority() {
	 
		 return 0;
		 
	 }
	 
	  
	@SuppressWarnings("rawtypes")
	@Override
	public void execute(RoundRobinFragmentAO fragmentAO, TransformationConfiguration config) throws RuleException { 
		
		defaultExecute(fragmentAO, new RoundRobinFragmentPO(fragmentAO), config, true, true);
		 
	}
	 
	@Override
	public boolean isExecutable(RoundRobinFragmentAO fragmentAO, TransformationConfiguration transformConfig) {
		 
		return fragmentAO.isAllPhysicalInputSet();
		
	}

	@Override
	public String getName() {
		
		return "RoundRobinFragmentAO -> RoundRobinFragmentPO";
		
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		
		return TransformRuleFlowGroup.TRANSFORMATION;
		
	}
	
	@Override
	public Class<? super RoundRobinFragmentAO> getConditionClass() {	
		
		return RoundRobinFragmentAO.class;
		
	}

}