package de.uniol.inf.is.odysseus.query.transformation.operator.rule.registry;

import java.util.Comparator;

import de.uniol.inf.is.odysseus.query.transformation.operator.rule.IOperatorRule;

public class IOperatorRuleComparator implements Comparator<IOperatorRule>{

			@Override
			public int compare(IOperatorRule o1, IOperatorRule o2) {
				 return o2.getPriority() - o1.getPriority();
		
			}
	   
}
