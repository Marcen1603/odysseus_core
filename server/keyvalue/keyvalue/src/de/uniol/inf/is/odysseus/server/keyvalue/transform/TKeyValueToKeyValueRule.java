package de.uniol.inf.is.odysseus.server.keyvalue.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.keyvalue.logicaloperator.ToKeyValueAO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TKeyValueToKeyValueRule extends AbstractTransformationRule<ToKeyValueAO> {


	@Override
	public void execute(ToKeyValueAO ao, TransformationConfiguration config) throws RuleException {
		Collection<ILogicalOperator> toUpdate = LogicalPlan.removeOperator(ao, true);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}
		retract(ao);
	}

	@Override
	public boolean isExecutable(ToKeyValueAO operator, TransformationConfiguration config) {
		return operator.getInputSchema().getType() == KeyValueObject.class;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.INIT;
	}

	@Override
	public Class<? super ToKeyValueAO> getConditionClass() {
		return ToKeyValueAO.class;
	}

}
