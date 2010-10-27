package de.uniol.inf.is.odysseus.predicate;

import java.io.Serializable;
import java.util.Map;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;

/**
 * @author Jonas Jacobi
 */
public interface IPredicate<T> extends IClone, Serializable {
	boolean evaluate(T input);

	boolean evaluate(T left, T right);

	@Override
	public IPredicate<T> clone();

	public void init();

	public void updateAfterClone(Map<ILogicalOperator, ILogicalOperator> updated);
	
	public boolean equals(IPredicate<T> pred);
}
