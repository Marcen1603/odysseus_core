package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;

public final class RestructParameterInfoUtil {

	private RestructParameterInfoUtil() {
	}

	// TODO: Make independent from ILogicalOperator (use ParameterInfo-Map
	// directly)
	public static void updatePredicateParameterInfo(ILogicalOperator op) {
		if (op instanceof IHasPredicate) {
			IHasPredicate sel = (IHasPredicate) op;
			String predicateString = op.getParameterInfos().get("PREDICATE");
			if (predicateString != null && predicateString.length() > 0) {
				String newPredicateString;
				// Cases where predicate type is in front
				int pos = predicateString.indexOf("(");
				if (pos > 0) {
					newPredicateString = predicateString.substring(0, pos + 1)
							+ "'" + sel.getPredicate().toString() + "')";
				} else {
					newPredicateString = "'" + sel.getPredicate().toString()
							+ "'";
				}
				op.addParameterInfo("PREDICATE", newPredicateString);
			} else {
				op.addParameterInfo("PREDICATE", "'"
						+ sel.getPredicate().toString() + "'");
			}
		}
	}
}
