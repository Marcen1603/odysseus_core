package de.uniol.inf.is.odysseus.base.predicate;

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

	public boolean evaluate(T input) {
		return !this.predicate.evaluate(input);
	}

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
}
