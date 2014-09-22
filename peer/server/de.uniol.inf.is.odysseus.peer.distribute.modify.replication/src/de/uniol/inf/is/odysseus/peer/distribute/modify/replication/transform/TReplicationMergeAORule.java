package de.uniol.inf.is.odysseus.peer.distribute.modify.replication.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.modify.replication.logicaloperator.ReplicationMergeAO;
import de.uniol.inf.is.odysseus.peer.distribute.modify.replication.physicaloperator.ReplicationMergePO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * The rule of transformation for the {@link ReplicationMergeAO}. Any {@link ReplicationMergeAO} will be 
 * transformed into a new {@link ReplicationMergePO}.
 * @author Michael Brand
 */
public class TReplicationMergeAORule extends AbstractTransformationRule<ReplicationMergeAO> {

	 @Override
	 public int getPriority() {
	 
		 return 0;
		 
	 }
	 
	  
	 @SuppressWarnings("rawtypes")
	@Override
	 public void execute(ReplicationMergeAO mergeAO, TransformationConfiguration config) throws RuleException { 
		 
		 defaultExecute(mergeAO, new ReplicationMergePO(), config, true, true);
		 
	 }
	 
	 @Override
	public boolean isExecutable(ReplicationMergeAO mergeAO, TransformationConfiguration transformConfig) {
		 
		return mergeAO.isAllPhysicalInputSet() &&
				transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName());
		
	}

	@Override
	public String getName() {
		
		return "ReplicationMergeAO -> ReplicationMergePO";
		
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		
		return TransformRuleFlowGroup.TRANSFORMATION;
		
	}
	
	@Override
	public Class<? super ReplicationMergeAO> getConditionClass() {	
		
		return ReplicationMergeAO.class;
		
	}
	
}