/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.predicates;

import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

/**
 * @author MarkMilster
 *
 */
public class SecurityPredicateFactory {

	public static ISecurityPredicate<?> createSecurityPredicate(String name, IPredicate<?> predicate) {
		ISecurityPredicate<?> securityPredicate = null;
		// TODO here you can implement every kind of predicate
		if (predicate instanceof RelationalExpression) {
			securityPredicate = new SecurityRelationalExpression<>(name, (RelationalExpression<?>) predicate);
		}
		return securityPredicate;
	}

}
