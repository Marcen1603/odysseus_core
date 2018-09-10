package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.physicaloperator.HashFragmentPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * The rule of transformation for the {@link HashFragmentAO}. 
 * @author Michael Brand
 */
public class THashFragmentAORule extends AbstractTransformationRule<HashFragmentAO> {
	
	@Override
	 public int getPriority() {
	 
		 return 0;
		 
	 }
	 
	  
	@SuppressWarnings("rawtypes")
	@Override
	public void execute(HashFragmentAO fragmentAO, TransformationConfiguration config) throws RuleException { 
		
		defaultExecute(fragmentAO, new HashFragmentPO(fragmentAO), config, true, true);
		 
	}
	 
	@Override
	public boolean isExecutable(HashFragmentAO fragmentAO, TransformationConfiguration transformConfig) {
		 
		return fragmentAO.isAllPhysicalInputSet();
		
	}

	@Override
	public String getName() {
		
		return "HashFragmentAO -> HashFragmentPO";
		
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		
		return TransformRuleFlowGroup.TRANSFORMATION;
		
	}
	
	@Override
	public Class<? super HashFragmentAO> getConditionClass() {	
		
		return HashFragmentAO.class;
		
	}

}