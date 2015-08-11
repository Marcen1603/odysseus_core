package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator.BaDaStAccessAO;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator.AbstractBaDaStAccessPO;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator.BaDaStBackupPO;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator.BaDaStRecoveryPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Rule to transform an {@link BaDaStAccessAO} into a physical operator:
 * {@link BaDaStBackupPO} or {@link BaDaStRecoveryPO}.
 * 
 * @author Michael Brand
 *
 */
public class TBaDaStAccessAORule extends
		AbstractTransformationRule<BaDaStAccessAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(BaDaStAccessAO logical,
			TransformationConfiguration config) throws RuleException {
		AbstractBaDaStAccessPO<?> po;
		if (logical.isInRecoveryMode()) {
			po = new BaDaStRecoveryPO<>(logical);
		} else {
			po = new BaDaStBackupPO<>(logical);
		}
		defaultExecute(logical, po, config, true, true);
	}

	@Override
	public boolean isExecutable(BaDaStAccessAO logical,
			TransformationConfiguration transformConfig) {
		return logical.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "SourceSyncAO -> BaDaStBackupPO | BaDaStRecoveryPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super BaDaStAccessAO> getConditionClass() {
		return BaDaStAccessAO.class;
	}

}