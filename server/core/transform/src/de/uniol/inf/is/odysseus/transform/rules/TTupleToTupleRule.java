package de.uniol.inf.is.odysseus.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ToTupleAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TTupleToTupleRule extends AbstractTransformationRule<ToTupleAO> {

	@Override
	public void execute(ToTupleAO ao, TransformationConfiguration config) throws RuleException {
		Collection<ILogicalOperator> toUpdate = LogicalPlan.removeOperator(ao, true);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}
		retract(ao);		
	}

	@Override
	public boolean isExecutable(ToTupleAO operator, TransformationConfiguration config) {
		return operator.getInputSchema().getType() == Tuple.class;

	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.INIT;
	}

	@Override
	public Class<? super ToTupleAO> getConditionClass() {
		return ToTupleAO.class;
	}
	
}
