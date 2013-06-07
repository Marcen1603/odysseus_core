package de.uniol.inf.is.odysseus.p2p_new.lb.distributionmerger;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.lb.distributionmerger.logicaloperator.DistributionMergeAO;
import de.uniol.inf.is.odysseus.p2p_new.lb.distributionmerger.physicaloperator.DistributionMergePO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * The rule of transformation for the {@link DistributionMergeAO}. Any {@link DistributionMergeAO} will be 
 * transformed into a new {@link DistributionMergePO}.
 * @author Michael Brand
 */
public class TDistributionMergeAORule extends AbstractTransformationRule<DistributionMergeAO> {

	 @Override
	 public int getPriority() {
	 
		 return 0;
		 
	 }
	 
	  
	 @SuppressWarnings("rawtypes")
	@Override
	 public void execute(DistributionMergeAO mergeAO, TransformationConfiguration config) { 
		 
		 defaultExecute(mergeAO, new DistributionMergePO(), config, true, true);
		 
	 }
	 
	 @Override
	public boolean isExecutable(DistributionMergeAO mergeAO, TransformationConfiguration transformConfig) {
		 
		return mergeAO.isAllPhysicalInputSet() &&
				transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName());
		
	}

	@Override
	public String getName() {
		
		return "DistributionMergeAO -> DistributionMergePO";
		
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		
		return TransformRuleFlowGroup.TRANSFORMATION;
		
	}
	
	@Override
	public Class<? super DistributionMergeAO> getConditionClass() {	
		
		return DistributionMergeAO.class;
		
	}
	
}