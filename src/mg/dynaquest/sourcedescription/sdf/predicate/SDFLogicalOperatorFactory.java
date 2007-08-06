package mg.dynaquest.sourcedescription.sdf.predicate;

import java.util.HashMap;

import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates;

public class SDFLogicalOperatorFactory {
	private static HashMap<String, SDFLogicalOperator>  operatorCache = new HashMap<String, SDFLogicalOperator>();

	protected SDFLogicalOperatorFactory() {
	}

	public static SDFLogicalOperator getOperator(String operatorURI) {

		SDFLogicalOperator operator = operatorCache.get(operatorURI);
		if (operator == null) {

			while (true) {
				if (operatorURI.equals(SDFPredicates.And)) {
					operator = new SDFAndOperator(operatorURI);
					break;
				}
				if (operatorURI.equals(SDFPredicates.Or)) {
					operator = new SDFOrOperator(operatorURI);
					break;
				}
				System.out
						.println("Fehlerhafter Operator-Type: " + operatorURI);
				break;
			}
			operatorCache.put(operatorURI, operator);
		}
		return operator;
	}

}