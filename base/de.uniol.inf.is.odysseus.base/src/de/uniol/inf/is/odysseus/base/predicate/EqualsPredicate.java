package de.uniol.inf.is.odysseus.base.predicate;

public class EqualsPredicate<T> extends AbstractPredicate<T> {

	private static final long serialVersionUID = 405493232110297596L;
	@SuppressWarnings("unchecked")
	private static EqualsPredicate predicate;

	public boolean evaluate(T input) {
		throw new UnsupportedOperationException();
	}

	public boolean evaluate(T left, T right) {
		return left.equals(right);
	}

	@SuppressWarnings("unchecked")
	public static <T> EqualsPredicate<T> getInstance() {
		return (EqualsPredicate<T>) predicate;
	}

	@SuppressWarnings("unchecked")
	public EqualsPredicate<T> clone() {
		return predicate;
	}
}
