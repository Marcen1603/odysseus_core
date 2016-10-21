package de.uniol.inf.is.odysseus.incubation.crypt.predicates;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

/**
 * @author MarkMilster
 *
 */
public interface ISecurityPredicate<T> extends IPredicate<T> {

	public String getOperatorName();

}
