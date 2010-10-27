package de.uniol.inf.is.odysseus.predicate;

/**
 * @author Jonas Jacobi
 */
public class AndPredicate<T> extends ComplexPredicate<T> {

	private static final long serialVersionUID = -3438130138466305862L;

	public AndPredicate() {
		super();
	}

	public AndPredicate(IPredicate<? super T> leftPredicate,
			IPredicate<? super T> rightPredicate) {
		super(leftPredicate, rightPredicate);
	}

	public AndPredicate(AndPredicate<T> pred) {
		super(pred);
	}

	@Override
	public boolean evaluate(T input) {
		return getLeft().evaluate(input) && getRight().evaluate(input);
	}

	@Override
	public boolean evaluate(T left, T right) {
		return getLeft().evaluate(left, right)
				&& getRight().evaluate(left, right);
	}

	@Override
	public AndPredicate<T> clone() {
		return new AndPredicate<T>(this);
	}

	@Override
	public String toString() {
		return "(" + getLeft().toString() + ") AND (" + getRight().toString()
				+ ")";
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object other) {
		if (!(other instanceof AndPredicate)) {
			return false;
		} else {
			return this.getLeft().equals(((AndPredicate) other).getLeft())
					&& this.getRight()
							.equals(((AndPredicate) other).getRight());
		}
	}

	@Override
	public int hashCode() {
		return 17 * this.getLeft().hashCode() * 19 * this.getRight().hashCode();
	}
	
	@Override
	public boolean equals(IPredicate pred) {
		if(!(pred instanceof AndPredicate)) {
			return false;
		}
		AndPredicate ap = (AndPredicate) pred;
		return this.getLeft().equals(ap.getLeft()) && this.getRight().equals(ap.getRight());
	}

}
