package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator.SourceSyncAO;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator.SourceSyncPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

// TODO update javaDoc
/**
 * Rule to transform an {@link SourceSyncAO} into a physical operator.
 * 
 * @author Michael Brand
 *
 */
public class TSourceSyncAORule extends AbstractTransformationRule<SourceSyncAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(SourceSyncAO logical, TransformationConfiguration config)
			throws RuleException {
		defaultExecute(logical, new SourceSyncPO<>(logical), config, true, true);
	}

	@Override
	public boolean isExecutable(SourceSyncAO logical,
			TransformationConfiguration transformConfig) {
		return logical.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "SourceSyncAO -> SourceSyncPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super SourceSyncAO> getConditionClass() {
		return SourceSyncAO.class;
	}

}