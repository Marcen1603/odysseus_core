package de.uniol.inf.is.odysseus.predicate;

import java.io.Serializable;
import java.util.Map;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;

/**
 * This is an abstract superclass for all predicates, that provides an empty init method.
 * @author Andre Bolles
 *
 */
public abstract class AbstractPredicate<T> implements IPredicate<T>, Serializable {


	private static final long serialVersionUID = -2182745249884399237L;

	@Override
	public void init(){
	}


	public AbstractPredicate() {
	}

	public AbstractPredicate(AbstractPredicate<T> pred) {
	}
	
	@Override
	abstract public AbstractPredicate<T> clone();
	
	@Override
	public void updateAfterClone(Map<ILogicalOperator,ILogicalOperator> updated) {};
	
	// TODO: IMplement in Child Classes... 
	@Override
	public boolean equals(IPredicate<T> pred) {
		return false;
	}
	
	@Override
	public boolean isContainedIn(Object o) {
		return false;
	}
}
