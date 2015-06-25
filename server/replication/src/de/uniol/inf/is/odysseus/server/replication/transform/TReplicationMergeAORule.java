package de.uniol.inf.is.odysseus.server.replication.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.server.replication.logicaloperator.ReplicationMergeAO;
import de.uniol.inf.is.odysseus.server.replication.physicaloperator.ReplicationMergePO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.transform.AbstractIntervalTransformationRule;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

/**
 * The rule of transformation for the {@link ReplicationMergeAO}. Any {@link ReplicationMergeAO} will be 
 * transformed into a new {@link ReplicationMergePO}.
 * @author Michael Brand
 */
public class TReplicationMergeAORule extends AbstractIntervalTransformationRule<ReplicationMergeAO> {

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