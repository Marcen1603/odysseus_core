package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

public interface IHasPredicates {
	
	List<IPredicate<?>> getPredicates();

	void setPredicates(List<IPredicate<?>> predicates);

}
