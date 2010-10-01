package de.uniol.inf.is.odysseus.interval.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.intervalapproach.AntiJoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.predicate.OverlapsPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.ExistenceAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISweepArea;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TExistenceAORule extends AbstractTransformationRule<ExistenceAO> {

	@Override 
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(ExistenceAO existenceAO, TransformationConfiguration transformConfig) {
		ISweepArea leftSA = new DefaultTISweepArea();
		ISweepArea rightSA = new DefaultTISweepArea();
		IPredicate predicate = existenceAO.getPredicate();
		if (existenceAO.getType() == ExistenceAO.Type.NOT_EXISTS) {
			predicate = new de.uniol.inf.is.odysseus.predicate.NotPredicate(predicate);
		}
		leftSA.setQueryPredicate(new AndPredicate(OverlapsPredicate.getInstance(), predicate));
		rightSA.setQueryPredicate(new AndPredicate(OverlapsPredicate.getInstance(), predicate));
		AntiJoinTIPO po = new AntiJoinTIPO(existenceAO, leftSA, rightSA);
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(existenceAO, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(existenceAO);
		
	}

	@Override
	public boolean isExecutable(ExistenceAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "ExistenceAO -> AntiJoinTIPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
