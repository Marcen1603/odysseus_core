package de.uniol.inf.is.odysseus.objecttracking.logicaloperator;

import java.util.Map;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.IRangePredicate;

public interface IHasRangePredicates {	
	public Map<IPredicate, IRangePredicate> getRangePredicates();
	public boolean isNotInitialized();
}
