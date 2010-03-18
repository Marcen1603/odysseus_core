package de.uniol.inf.is.odysseus.base.predicate;

import java.io.Serializable;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;

/**
 * This is an abstract superclass for all predicates, that provides an empty init method.
 * @author Andre Bolles
 *
 */
public abstract class AbstractPredicate<T> implements IPredicate<T>, Serializable {


	private static final long serialVersionUID = -2182745249884399237L;

	public void init(){
	}


	public AbstractPredicate() {
	}

	public AbstractPredicate(AbstractPredicate<T> pred) {
	}
	
	@Override
	//@SuppressWarnings("unchecked")
	public AbstractPredicate<T> clone(){
		// TODO Auskommentierung wieder entfernt, damit getestet werden kann
		try{
			return (AbstractPredicate<T>)super.clone();
		}catch(CloneNotSupportedException e){
			throw new RuntimeException("Clone not supported");
		}
	}
	
	public void updateAfterClone(Map<ILogicalOperator,ILogicalOperator> updated) {};
}
