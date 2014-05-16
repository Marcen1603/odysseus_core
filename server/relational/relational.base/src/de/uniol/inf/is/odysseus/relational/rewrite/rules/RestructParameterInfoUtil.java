package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public final class RestructParameterInfoUtil {
	
	private RestructParameterInfoUtil() {
	}
	
	// TODO: Make indipendent from ILogicalOperator (use ParameterInfo-Map directly)
	public static void updatePredicateParameterInfo(ILogicalOperator sel) {
		String predicateString = sel.getParameterInfos().get("PREDICATE");
		if( predicateString != null && predicateString.length() > 0 ) {
			String newPredicateString;
			// Cases where predicate type is in front
			int pos = predicateString.indexOf("(");
			if (pos > 0){
				newPredicateString = predicateString.substring(0, pos + 1) + "'" + sel.getPredicate().toString() + "')";
			}else{
				newPredicateString = "'" + sel.getPredicate().toString() + "'";
			}
			sel.addParameterInfo("PREDICATE", newPredicateString);
		} else {
			sel.addParameterInfo("PREDICATE", "'" + sel.getPredicate().toString() + "'");
		}
	}

}
