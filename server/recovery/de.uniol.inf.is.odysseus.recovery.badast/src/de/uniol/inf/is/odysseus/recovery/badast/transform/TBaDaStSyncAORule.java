package de.uniol.inf.is.odysseus.recovery.badast.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.recovery.badast.logicaloperator.BaDaStSyncAO;
import de.uniol.inf.is.odysseus.recovery.badast.physicaloperator.BaDaStBackupPO;
import de.uniol.inf.is.odysseus.recovery.badast.physicaloperator.BaDaStRecoveryPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Rule to transform an {@link BaDaStSyncAO} into an {@link BaDaStBackupPO} or
 * {@link BaDaStRecoveryPO}.
 * 
 * @author Michael Brand
 *
 */
public class TBaDaStSyncAORule extends AbstractTransformationRule<BaDaStSyncAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(BaDaStSyncAO logical, TransformationConfiguration config) throws RuleException {
		if (logical.isInRecoveryMode()) {
			defaultExecute(logical, new BaDaStRecoveryPO<>(logical.getSource()), config, true, true);
		} else {
			defaultExecute(logical, new BaDaStBackupPO<>(logical.getSource()), config, true, true);
		}
	}

	@Override
	public boolean isExecutable(BaDaStSyncAO logical, TransformationConfiguration transformConfig) {
		return logical.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "BaDaStSyncAO -> BaDaStBackupPO | BaDaStRecoveryPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super BaDaStSyncAO> getConditionClass() {
		return BaDaStSyncAO.class;
	}

}