package de.uniol.inf.is.odysseus.predicate;

/**
 * Diese Klasse dient als Migrationshilfe um eine einfache Umstellung von 
 * complex predicates auf Mep Predicates zu realisieren
 * @author Marco Grawunder
 *
 */

public class ComplexPredicateBuilder {

	public static IPredicate createAndPredicate(IPredicate leftPredicate, IPredicate rightPredicate){
		return new AndPredicate(leftPredicate, rightPredicate);
	}
	
	public static IPredicate createOrPredicate(IPredicate leftPredicate, IPredicate rightPredicate){
		return new OrPredicate(leftPredicate, rightPredicate);
	}

	public static IPredicate createNotPredicate(IPredicate predicate){
		return new NotPredicate(predicate);
	}

}
