package de.uniol.inf.is.odysseus.base.predicate;

import java.io.Serializable;

/**
 * This is an abstract superclass for all predicates, that provides an empty init method.
 * @author Andre Bolles
 *
 */
public abstract class AbstractPredicate<T> implements IPredicate<T>, Serializable {


	private static final long serialVersionUID = -2182745249884399237L;

	public void init(){
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public AbstractPredicate<T> clone(){
		try{
			return (AbstractPredicate<T>)super.clone();
		}catch(CloneNotSupportedException e){
			throw new RuntimeException(e);
		}
	}
}
