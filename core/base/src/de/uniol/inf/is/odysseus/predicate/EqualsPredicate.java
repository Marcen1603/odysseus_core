package de.uniol.inf.is.odysseus.predicate;

public class EqualsPredicate<T> extends AbstractPredicate<T> {

	private static final long serialVersionUID = 405493232110297596L;
	@SuppressWarnings({ "rawtypes" })
	private static EqualsPredicate predicate = new EqualsPredicate();

	@Override
	public boolean evaluate(T input) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean evaluate(T left, T right) {
		return left.equals(right);
	}

	@SuppressWarnings("unchecked")
	public static <T> EqualsPredicate<T> getInstance() {
		return (EqualsPredicate<T>) predicate;
	}

	@Override
	@SuppressWarnings("unchecked")
	public EqualsPredicate<T> clone() {
		return predicate;
	}
	
	@Override
	public boolean equals(IPredicate pred) {
		if(!(pred instanceof EqualsPredicate)) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isContainedIn(Object o) {
		if(!(o instanceof EqualsPredicate)) {
			return false;
		}
		return true;
	}
}
