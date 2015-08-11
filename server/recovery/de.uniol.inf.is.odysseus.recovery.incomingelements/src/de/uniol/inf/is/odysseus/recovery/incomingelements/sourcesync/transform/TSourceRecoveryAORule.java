package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator.SourceRecoveryAO;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator.AbstractSourceRecoveryPO;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator.SourceBackupPO;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator.SourceRecoveryPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Rule to transform an {@link SourceRecoveryAO} into an
 * {@link AbstractSourceRecoveryPO}.
 * 
 * @author Michael Brand
 *
 */
public class TSourceRecoveryAORule extends
		AbstractTransformationRule<SourceRecoveryAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(SourceRecoveryAO logical,
			TransformationConfiguration config) throws RuleException {
		AbstractSourceRecoveryPO<?> po;
		if (logical.isInRecoveryMode()) {
			po = new SourceRecoveryPO<>(logical);
		} else {
			po = new SourceBackupPO<>(logical);
		}
		defaultExecute(logical, po, config, true, true);
	}

	@Override
	public boolean isExecutable(SourceRecoveryAO logical,
			TransformationConfiguration transformConfig) {
		return logical.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "SourceRecoveryAO -> SourceBackupPO | SourceRecoveryPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super SourceRecoveryAO> getConditionClass() {
		return SourceRecoveryAO.class;
	}

}