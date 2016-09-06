package de.uniol.inf.is.odysseus.codegenerator.operator.rule;

import java.util.Comparator;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

/**
 * Comparator class to compare two operator rules based 
 * of the priority.
 *  
 * @author MarcPreuschaft
 *
 */
public class ICOperatorRuleComparator implements Comparator<ICOperatorRule<ILogicalOperator>>{

	@Override
	public int compare(ICOperatorRule<ILogicalOperator> o1, ICOperatorRule<ILogicalOperator> o2) {
		return o2.getPriority() - o1.getPriority();
	}
	   
}
