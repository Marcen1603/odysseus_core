package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class SDFSimplePredicateFactory {
	private static HashMap<String, SDFSimplePredicate> predicateCache = new HashMap<String, SDFSimplePredicate>();

	protected SDFSimplePredicateFactory() {
	}

	public static SDFSimplePredicate createSimplePredicate(String URI, SDFExpression left, SDFCompareOperator op, SDFExpression right) {
		SDFSimplePredicate sPred = predicateCache.get(URI);
		if (sPred != null)
			return sPred;
		
		sPred = new SDFSimplePredicate(URI, left, op, right);
		predicateCache.put(URI,sPred);
		return sPred;
	}
}