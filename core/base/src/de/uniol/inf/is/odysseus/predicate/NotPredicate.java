package de.uniol.inf.is.odysseus.predicate;

/**
 * @author Jonas Jacobi
 */
public class NotPredicate<T> extends AbstractPredicate<T> {
	private static final long serialVersionUID = -3214605315259491423L;
	IPredicate<? super T> predicate;

	public NotPredicate() {
	}
	
	public NotPredicate(NotPredicate<T> predicate) {
		this.predicate = predicate.predicate;
	}

	public NotPredicate(IPredicate<? super T> predicate) {
		this.predicate = predicate;
	}
	
	public IPredicate<? super T> getChild() {
		return this.predicate;
	}

	@Override
	public boolean evaluate(T input) {
		return !this.predicate.evaluate(input);
	}

	@Override
	public boolean evaluate(T left, T right) {
		return !this.predicate.evaluate(left, right);
	}
	
	@Override
	public NotPredicate<T> clone() {
		return new NotPredicate<T>(this);
	}
	
	@Override
	public String toString() {
		return "NOT (" + getChild() + ")";
	}
	
	@Override
	public boolean equals(IPredicate<T> pred) {
		if(!(pred instanceof NotPredicate)) {
			return false;
		}
		if(((NotPredicate<T>)pred).getChild().equals(this.predicate)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isContainedIn(Object o) {
		if(!(o instanceof NotPredicate)) {
			return false;
		} else {
			if(((NotPredicate<?>)o).getChild().isContainedIn(this.predicate)) {
				return true;
			}
		}
		return false;
	}
}
