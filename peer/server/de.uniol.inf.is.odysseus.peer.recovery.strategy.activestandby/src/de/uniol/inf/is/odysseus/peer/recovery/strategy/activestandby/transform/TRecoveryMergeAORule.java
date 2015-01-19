package de.uniol.inf.is.odysseus.peer.recovery.strategy.activestandby.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.peer.recovery.strategy.activestandby.logicaloperator.RecoveryMergeAO;
import de.uniol.inf.is.odysseus.peer.recovery.strategy.activestandby.physicaloperator.RecoveryMergePO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * The rule of transformation for the {@link RecoveryMergeAO}. Any {@link RecoveryMergeAO} will be 
 * transformed into a new {@link RecoveryMergePO}.
 * @author Michael Brand
 */
public class TRecoveryMergeAORule extends AbstractTransformationRule<RecoveryMergeAO> {

	 @Override
	 public int getPriority() {
	 
		 return 0;
		 
	 }
	 
	  
	@SuppressWarnings("rawtypes")
	@Override
	 public void execute(RecoveryMergeAO mergeAO, TransformationConfiguration config) throws RuleException { 
		 
		 defaultExecute(mergeAO, new RecoveryMergePO(mergeAO), config, true, true);
		 
	 }
	 
	@Override
	public boolean isExecutable(RecoveryMergeAO mergeAO, TransformationConfiguration transformConfig) {
		 
		return mergeAO.isAllPhysicalInputSet() &&
				transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName());
		
	}

	@Override
	public String getName() {
		
		return "RecoveryMergeAO -> RecoveryMergePO";
		
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		
		return TransformRuleFlowGroup.TRANSFORMATION;
		
	}
	
	@Override
	public Class<? super RecoveryMergeAO> getConditionClass() {	
		
		return RecoveryMergeAO.class;
		
	}
	
}