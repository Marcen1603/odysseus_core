package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class RestructParameterInfoUtil {
	
	private RestructParameterInfoUtil() {
	}
	
	public static void updatePredicateParameterInfo(ILogicalOperator sel) {
		String predicateString = sel.getParameterInfos().get("PREDICATE");
		int pos = predicateString.indexOf("(");
		String newPredicateString = predicateString.substring(0, pos + 1) + "'" + sel.getPredicate().toString() + "')";
		sel.addParameterInfo("PREDICATE", newPredicateString);
	}

}
