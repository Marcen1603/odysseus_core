package de.uniol.inf.is.odysseus.relational.transform;

import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalSelectPO;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;

public class TRelationalSelectAORule extends AbstractRelationalTransformationRule<SelectAO> {

	@Override
	public int getPriority() {
		// Needs a higher prio than the standard selection rule
		return 1;
	}

	@Override
	public void execute(SelectAO operator, TransformationConfiguration config) throws RuleException {
		RelationalPredicate pred = (RelationalPredicate) operator.getPredicate();
		RelationalExpression<IMetaAttribute> predicate = new RelationalExpression<IMetaAttribute>(pred.getExpression());
        predicate.initVars(operator.getInputSchema());
		RelationalSelectPO po = new RelationalSelectPO(predicate);
		defaultExecute(operator, po, config, true, true);
	}
	
	@Override
	public boolean isExecutable(SelectAO operator, TransformationConfiguration config) {
		boolean v = super.isExecutable(operator, config);
		return v && operator.getPredicate() instanceof RelationalPredicate;
	}

	@Override
	public Class<? super SelectAO> getConditionClass() {
		return SelectAO.class;
	}
}
