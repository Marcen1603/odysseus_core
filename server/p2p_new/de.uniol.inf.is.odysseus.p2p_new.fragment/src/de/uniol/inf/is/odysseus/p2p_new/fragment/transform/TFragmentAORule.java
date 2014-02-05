package de.uniol.inf.is.odysseus.p2p_new.fragment.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.fragment.HashFragmentation;
import de.uniol.inf.is.odysseus.p2p_new.fragment.logicaloperator.FragmentAO;
import de.uniol.inf.is.odysseus.p2p_new.fragment.physicaloperator.AbstractFragmentPO;
import de.uniol.inf.is.odysseus.p2p_new.fragment.physicaloperator.HashFragmentPO;
import de.uniol.inf.is.odysseus.p2p_new.fragment.physicaloperator.RRFragmentPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * The rule of transformation for the {@link FragmentAO}. 
 * The {@link TransformationConfiguration} will decide into which physical operator, extending {@link AbstractFragmentPO}, 
 * the {@link RRFragmentAO} will be transformed.
 * @author Michael Brand
 */
public class TFragmentAORule extends AbstractTransformationRule<FragmentAO> {
	
	@Override
	 public int getPriority() {
	 
		 return 0;
		 
	 }
	 
	  
	@SuppressWarnings("rawtypes")
	@Override
	public void execute(FragmentAO fragmentAO, TransformationConfiguration config) throws RuleException { 
		
		if(fragmentAO.getType().equals(HashFragmentation.NAME))
			defaultExecute(fragmentAO, new HashFragmentPO(fragmentAO), config, true, true);
		else {
		
			// default = round robin
			defaultExecute(fragmentAO, new RRFragmentPO(fragmentAO), config, true, true);
			
		}
		 
	}
	 
	@Override
	public boolean isExecutable(FragmentAO fragmentAO, TransformationConfiguration transformConfig) {
		 
		return fragmentAO.isAllPhysicalInputSet();
		
	}

	@Override
	public String getName() {
		
		return "FragmentAO -> AbstractFragmentPO";
		
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		
		return TransformRuleFlowGroup.TRANSFORMATION;
		
	}
	
	@Override
	public Class<? super FragmentAO> getConditionClass() {	
		
		return FragmentAO.class;
		
	}

}