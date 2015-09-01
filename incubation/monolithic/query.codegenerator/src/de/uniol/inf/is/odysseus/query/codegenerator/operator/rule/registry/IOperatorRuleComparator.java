package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.registry;

import java.util.Comparator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.IOperatorRule;

public class IOperatorRuleComparator implements Comparator<IOperatorRule<ILogicalOperator>>{

			@Override
			public int compare(IOperatorRule<ILogicalOperator> o1, IOperatorRule<ILogicalOperator> o2) {
				 return o2.getPriority() - o1.getPriority();
		
			}
	   
}
