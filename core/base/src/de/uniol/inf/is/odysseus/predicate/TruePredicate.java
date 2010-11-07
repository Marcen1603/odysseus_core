package de.uniol.inf.is.odysseus.predicate;

public class TruePredicate<T> extends AbstractPredicate<T> {
	private static final long serialVersionUID = 7701679660439284127L;

	@Override
	public boolean evaluate(T input) {
		return true;
	}

	@Override
	public boolean evaluate(T left, T right) {
		return true;
	}

	@Override
	public AbstractPredicate<T> clone() {
		return new TruePredicate<T>();
	}
	
	@Override
	public boolean equals(IPredicate<T> pred) {
		return (pred instanceof TruePredicate);
	}
	
	@Override
	public boolean isContainedIn(Object o) {
		if(!(o instanceof TruePredicate)) {
			return false;
		}
		return true;
	}
	
}
