package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator.BaDaStAccessAO;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator.BaDaStAccessPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

// TODO update javaDoc
/**
 * Rule to transform an {@link BaDaStAccessAO} into a physical operator.
 * 
 * @author Michael Brand
 *
 */
public class TBaDaStAccessAORule extends AbstractTransformationRule<BaDaStAccessAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(BaDaStAccessAO logical, TransformationConfiguration config)
			throws RuleException {
		BaDaStAccessPO<?> po;
		if(logical.isInRecoveryMode()) {
			// TODO new PO
			po = new BaDaStAccessPO<>(logical);
		} else {
			po = new BaDaStAccessPO<>(logical);
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
		// TODO change name
		return "SourceSyncAO -> SourceSyncPO";
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