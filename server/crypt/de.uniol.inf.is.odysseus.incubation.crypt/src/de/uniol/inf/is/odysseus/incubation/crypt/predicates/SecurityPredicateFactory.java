package de.uniol.inf.is.odysseus.incubation.crypt.predicates;

import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

/**
 * This Factory provides methods to create SecurityPredicates
 * 
 * @author MarkMilster
 *
 */
public class SecurityPredicateFactory {

	/**
	 * This methods creates a SecurityPunctuation, which contains all
	 * information of the given IPredicate.
	 * 
	 * @param name
	 *            The name of the operator, the Predicate is used in
	 * @param predicate
	 *            The original IPredicate, which will be copied.
	 * @return
	 */
	public static ISecurityPredicate<?> createSecurityPredicate(String name, IPredicate<?> predicate) {
		ISecurityPredicate<?> securityPredicate = null;
		// TODO here you can implement every kind of predicate
		if (predicate instanceof RelationalExpression) {
			securityPredicate = new SecurityRelationalExpression<>(name, (RelationalExpression<?>) predicate);
		}
		return securityPredicate;
	}

}
