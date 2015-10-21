package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import java.util.Comparator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class ICOperatorRuleComparator implements Comparator<ICOperatorRule<ILogicalOperator>>{

			@Override
			public int compare(ICOperatorRule<ILogicalOperator> o1, ICOperatorRule<ILogicalOperator> o2) {
				 return o2.getPriority() - o1.getPriority();
		
			}
	   
}
