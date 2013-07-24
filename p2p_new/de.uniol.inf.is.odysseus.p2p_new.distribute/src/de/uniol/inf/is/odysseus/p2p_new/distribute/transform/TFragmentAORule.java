package de.uniol.inf.is.odysseus.p2p_new.distribute.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.distribute.logicaloperator.FragmentAO;
import de.uniol.inf.is.odysseus.p2p_new.distribute.physicaloperator.AbstractFragmentPO;
import de.uniol.inf.is.odysseus.p2p_new.distribute.physicaloperator.RRFragmentPO;
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
	public void execute(FragmentAO fragmentAO, TransformationConfiguration config) { 
		
		/*default = round robin*/
		defaultExecute(fragmentAO, new RRFragmentPO(fragmentAO), config, true, true);
		 
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