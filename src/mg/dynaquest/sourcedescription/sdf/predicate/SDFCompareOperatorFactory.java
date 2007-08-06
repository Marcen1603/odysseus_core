package mg.dynaquest.sourcedescription.sdf.predicate;

import java.util.HashMap;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates;

public class SDFCompareOperatorFactory {
	private static HashMap<String, SDFCompareOperator> operatorCache = new HashMap<String, SDFCompareOperator>();

	protected SDFCompareOperatorFactory() {
	}


	public static SDFCompareOperator getCompareOperator(String operatorURI) {

		SDFCompareOperator operator = operatorCache.get(operatorURI);
		if (operator == null) {

			while (true) {
				if ((operatorURI).equals(SDFPredicates.Equal)) {
					operator = new SDFEqualOperator();
					break;
				}
				if (operatorURI.equals(SDFPredicates.Unequal)) {
					operator = new SDFUnequalOperator();
					break;
				}
				if (operatorURI.equals(SDFPredicates.LowerThan)) {
					operator = new SDFLowerThanOperator();
					break;
				}
				if (operatorURI.equals(SDFPredicates.LowerOrEqualThan)) {
					operator = new SDFLowerOrEqualThanOperator();
					break;
				}
				if (operatorURI.equals(SDFPredicates.GreaterOrEqualThan)) {
					operator = new SDFGreaterOrEqualThanOperator();
					break;
				}
				if (operatorURI.equals(SDFPredicates.GreaterThan)) {
					operator = new SDFGreaterThanOperator();
					break;
				}
                System.err.println("Operator nicht implementiert "+operatorURI);
				throw new NotImplementedException();
				//break;
			}
			operatorCache.put(operatorURI, operator);
		}
		return operator;
	}
}