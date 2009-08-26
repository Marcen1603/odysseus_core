package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

import java.util.HashMap;


import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFPredicates;

public class SDFCompareOperatorFactory {
	private static HashMap<String, SDFCompareOperator> operatorCache = new HashMap<String, SDFCompareOperator>();

	protected SDFCompareOperatorFactory() {
	}

	public static SDFCompareOperator getCompareOperator(String operatorURI) {

		SDFCompareOperator operator = operatorCache.get(operatorURI);
		if (operator == null) {
			if ((operatorURI).equals(SDFPredicates.Equal)) {
				operator = new SDFEqualOperator();
			} else if (operatorURI.equals(SDFPredicates.Unequal)) {
				operator = new SDFUnequalOperator();
			} else if (operatorURI.equals(SDFPredicates.LowerThan)) {
				operator = new SDFLowerThanOperator();
			} else if (operatorURI.equals(SDFPredicates.LowerOrEqualThan)) {
				operator = new SDFLowerOrEqualThanOperator();
			} else if (operatorURI.equals(SDFPredicates.GreaterOrEqualThan)) {
				operator = new SDFGreaterOrEqualThanOperator();
			} else if (operatorURI.equals(SDFPredicates.GreaterThan)) {
				operator = new SDFGreaterThanOperator();
			} else {
				System.err.println("Operator nicht implementiert "
						+ operatorURI);
				throw new RuntimeException("Not implemented");
			}
			operatorCache.put(operatorURI, operator);
		}
		return operator;
	}

	public static SDFCompareOperator invertOperator(String operatorURI) {
		if ((operatorURI).equals(SDFPredicates.Equal)) {
			return getCompareOperator(SDFPredicates.Unequal);
		} else if (operatorURI.equals(SDFPredicates.Unequal)) {
			return getCompareOperator(SDFPredicates.Equal);
		} else if (operatorURI.equals(SDFPredicates.LowerThan)) {
			return getCompareOperator(SDFPredicates.GreaterOrEqualThan);
		} else if (operatorURI.equals(SDFPredicates.LowerOrEqualThan)) {
			return getCompareOperator(SDFPredicates.GreaterThan);
		} else if (operatorURI.equals(SDFPredicates.GreaterOrEqualThan)) {
			return getCompareOperator(SDFPredicates.LowerThan);
		} else if (operatorURI.equals(SDFPredicates.GreaterThan)) {
			return getCompareOperator(SDFPredicates.LowerOrEqualThan);
		} else {
			System.err.println("Operator nicht implementiert " + operatorURI);
			throw new RuntimeException("Not implemented");
		}
	}
}