package de.uniol.inf.is.odysseus.incubation.crypt.predicates;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

/**
 * This interface should be implemented by every SecurityPredicate<br>
 * A SecuritryPredicate can contain a encrypted version of a predicate.
 * 
 * @author MarkMilster
 *
 */
public interface ISecurityPredicate<T> extends IPredicate<T> {

	/**
	 * Returns the name of the Operator, this Predicate is used in
	 * @return
	 */
	public String getOperatorName();

}
