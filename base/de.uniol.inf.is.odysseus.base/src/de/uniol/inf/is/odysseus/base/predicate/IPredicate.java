package de.uniol.inf.is.odysseus.base.predicate;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.base.IClone;

/**
 * @author Jonas Jacobi
 */
public interface IPredicate<T> extends IClone, Serializable {
	boolean evaluate(T input);
	boolean evaluate(T left, T right);
	public IPredicate<T> clone();
	public void init();
}
