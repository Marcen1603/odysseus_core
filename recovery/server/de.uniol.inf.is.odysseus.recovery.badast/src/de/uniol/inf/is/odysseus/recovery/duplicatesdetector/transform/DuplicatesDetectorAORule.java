package de.uniol.inf.is.odysseus.recovery.duplicatesdetector.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.recovery.duplicatesdetector.logicaloperator.DupliatesDetectorAO;
import de.uniol.inf.is.odysseus.recovery.duplicatesdetector.physicaloperator.DupliatesDetectorPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.trust.ITrust;

/**
 * Rule to transform an {@link DupliatesDetectorAO} into a {@link DupliatesDetectorPO}.
 * 
 * @author Michael Brand
 *
 */
public class DuplicatesDetectorAORule extends AbstractTransformationRule<DupliatesDetectorAO> {

	@Override
	public void execute(DupliatesDetectorAO operator, TransformationConfiguration config) throws RuleException {
		this.defaultExecute(operator, new DupliatesDetectorPO<>(operator.getTrustValue()), config, true, true);
	}

	@Override
	public boolean isExecutable(DupliatesDetectorAO operator, TransformationConfiguration config) {
		// Logical all input set AND meta type must contain trust!
		return operator.isAllPhysicalInputSet() && operator.getInputSchema().hasMetatype(ITrust.class);
	}

	@Override
	public String getName() {
		return "DuplicatesDetectorAO -> DuplicatesDetectorPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super DupliatesDetectorAO> getConditionClass() {
		return DupliatesDetectorAO.class;
	}

}