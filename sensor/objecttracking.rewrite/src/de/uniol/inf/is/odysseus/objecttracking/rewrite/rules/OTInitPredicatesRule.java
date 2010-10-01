package de.uniol.inf.is.odysseus.objecttracking.rewrite.rules;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.IHasRangePredicates;
import de.uniol.inf.is.odysseus.objecttracking.util.ObjectTrackingPredicateInitializer;
import de.uniol.inf.is.odysseus.rewrite.engine.RewriteConfiguration;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;


public class OTInitPredicatesRule extends AbstractRewriteRule<IHasRangePredicates>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 15;
	}

	@Override
	public void execute(IHasRangePredicates operator,
			RewriteConfiguration config) {
		System.out.println("Init predicates of operator : " + operator);
		ObjectTrackingPredicateInitializer.visitPredicates(operator.getRangePredicates(), (ILogicalOperator)operator);
		operator.setInitialized(true);
		//retract(operator);
		System.out.println("Init predicates of operator finished: " + operator);
	}

	@Override
	public boolean isExecutable(IHasRangePredicates operator,
			RewriteConfiguration config) {
		if(operator.getRangePredicates() != null && !operator.isInitialized()){
			return true;
		}
		return false;
		
		// DRL-Code
//		$op : IHasRangePredicates(rangePredicates != null && initialized == false)
	}

	@Override
	public String getName() {
		return "Init RangePredicates";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.CLEANUP;
	}

}
