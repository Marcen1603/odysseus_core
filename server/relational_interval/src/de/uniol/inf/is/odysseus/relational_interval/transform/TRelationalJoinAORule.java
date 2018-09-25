package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.IRelationalExpression;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.transform.join.TJoinAORule;

public class TRelationalJoinAORule extends TJoinAORule {

	@Override
	public int getPriority() {
		// Higher Prio than standard join rule
		return 1;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void setJoinPredicate(@SuppressWarnings("rawtypes") JoinTIPO joinPO, JoinAO logOp) {
		IPredicate<?> pred = logOp.getPredicate();
		if (pred instanceof IRelationalExpression) {
			RelationalExpression<?> joinPred = new RelationalExpression<>(
					((RelationalExpression<?>) pred));
			joinPred.initVars(logOp.getInputSchema(0), logOp.getInputSchema(1));
			joinPO.setJoinPredicate(joinPred);
		} else {
			super.setJoinPredicate(joinPO, logOp);
		}
	}

	@Override
	public boolean isExecutable(JoinAO operator, TransformationConfiguration transformConfig) {
		return operator.getInputSchema(0).getType() == Tuple.class && super.isExecutable(operator, transformConfig);
	}

}
