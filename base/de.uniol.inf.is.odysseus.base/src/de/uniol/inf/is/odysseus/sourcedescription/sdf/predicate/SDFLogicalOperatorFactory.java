package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFPredicates;

public class SDFLogicalOperatorFactory {
	private static HashMap<String, SDFLogicalOperator> operatorCache = new HashMap<String, SDFLogicalOperator>();

	protected SDFLogicalOperatorFactory() {
	}

	public static SDFLogicalOperator getOperator(String operatorURI) {
		SDFLogicalOperator operator = operatorCache.get(operatorURI);
		if (operator == null) {
			if (operatorURI.equals(SDFPredicates.And)) {
				operator = new SDFAndOperator(operatorURI);
				operatorCache.put(operatorURI, operator);
			} else {
				if (operatorURI.equals(SDFPredicates.Or)) {
					operator = new SDFOrOperator(operatorURI);
					operatorCache.put(operatorURI, operator);
				} else {
					System.err.println("Fehlerhafter Operator-Type: "
							+ operatorURI);
				}
			}
		}
		return operator;
	}

}