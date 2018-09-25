package de.uniol.inf.is.odysseus.server.replication.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.transform.AbstractIntervalTransformationRule;
import de.uniol.inf.is.odysseus.server.replication.logicaloperator.ReplicationMergeAO;
import de.uniol.inf.is.odysseus.server.replication.physicaloperator.ReplicationMergePO;
import de.uniol.inf.is.odysseus.server.replication.physicaloperator.ReplicationMergeWithTrustPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.trust.ITrust;

/**
 * The rule of transformation for the {@link ReplicationMergeAO}. A
 * {@link ReplicationMergeAO} will be transformed into a new
 * {@link ReplicationMergeWithTrustPO} if {@link ITrust} is used as meta
 * attribute. Otherwise it will be transformed into a new
 * {@link ReplicationMergePO}. Both POs need {@link ITimeInterval}.
 * 
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
		if (hasMetaAttribute(mergeAO, "Trust")) {
			defaultExecute(mergeAO, new ReplicationMergeWithTrustPO(), config, true, true);
		} else {
			defaultExecute(mergeAO, new ReplicationMergePO(), config, true, true);
		}
	}

	@Override
	public boolean isExecutable(ReplicationMergeAO operator, TransformationConfiguration config) {
		return super.isExecutable(operator, config) && hasMetaAttribute(operator, "TimeInterval");
	}

	@Override
	public String getName() {
		return "ReplicationMergeAO -> ReplicationMergePO | ReplicationMergeWithTrustPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super ReplicationMergeAO> getConditionClass() {
		return ReplicationMergeAO.class;
	}

	private static boolean hasMetaAttribute(ReplicationMergeAO operator, String metaDataBaseSourceName) {
		return operator.getInputSchema(0).getMetaschema().stream()
				.anyMatch(schema -> schema.getBaseSourceNames().contains(metaDataBaseSourceName));
	}

}