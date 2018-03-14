package de.uniol.inf.is.odysseus.temporaltypes.transform;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.temporaltypes.physicalopertor.TemporalSelectPO;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TTemporalSelectAORule extends AbstractTransformationRule<SelectAO> {

	@Override
	public void execute(SelectAO operator, TransformationConfiguration config) throws RuleException {
		
		// Create TemporalExpression
		// I only have a predicate here, why? :(
		
	}

	@Override
	public boolean isExecutable(SelectAO operator, TransformationConfiguration config) {
		return expressionContainsTemporalAttribute(operator.getPredicate());
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public int getPriority() {
		return 1;
	}

	private boolean expressionContainsTemporalAttribute(IPredicate<?> predicate) {
		for (SDFAttribute attribute : predicate.getAttributes()) {
			if (attribute instanceof TemporalType) {
				return true;
			}
		}
		return false;
	}

}
