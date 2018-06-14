package de.uniol.inf.is.odysseus.recovery.recoverytime.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.recovery.recoverytime.IRecoveryTime;
import de.uniol.inf.is.odysseus.recovery.recoverytime.logicaloperator.RecoveryTimeCalculatorAO;
import de.uniol.inf.is.odysseus.recovery.recoverytime.physicaloperator.RecoveryTimeCalculatorPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.trust.ITrust;

/**
 * Transformation rule for {@link RecoveryTimeCalculatorAO} -> {@link RecoveryTimeCalculatorPO}.
 * @author Michael Brand
 *
 */
public class TRecoveryTimeCalculatorRule extends AbstractTransformationRule<RecoveryTimeCalculatorAO> {

	@Override
	public void execute(RecoveryTimeCalculatorAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new RecoveryTimeCalculatorPO<>(), config, true, true);
	}

	@Override
	public boolean isExecutable(RecoveryTimeCalculatorAO operator, TransformationConfiguration config) {
		if(!operator.isAllPhysicalInputSet()) {
			return false;
		}
		
		final SDFSchema schema = operator.getInputSchema();
		return schema.hasMetatype(ITimeInterval.class) && schema.hasMetatype(ITrust.class) && schema.hasMetatype(IRecoveryTime.class);
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}