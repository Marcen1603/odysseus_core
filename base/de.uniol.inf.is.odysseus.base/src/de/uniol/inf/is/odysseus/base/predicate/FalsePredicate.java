package de.uniol.inf.is.odysseus.base.predicate;

/**
 * 
 * @author Tobias Witt
 *
 * @param <T>
 */
public class FalsePredicate<T> extends AbstractPredicate<T> {
	private static final long serialVersionUID = -134272927237350983L;

	@Override
	public boolean evaluate(T input) {
		return false;
	}

	@Override
	public boolean evaluate(T left, T right) {
		return false;
	}

}
