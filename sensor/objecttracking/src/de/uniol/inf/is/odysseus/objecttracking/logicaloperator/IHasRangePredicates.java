package de.uniol.inf.is.odysseus.objecttracking.logicaloperator;

import java.util.Map;

import de.uniol.inf.is.odysseus.objecttracking.predicate.range.IRangePredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

public interface IHasRangePredicates {	
	public Map<IPredicate, IRangePredicate> getRangePredicates();
	public boolean isInitialized();
	public void setInitialized(boolean b);
}
