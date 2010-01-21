package de.uniol.inf.is.odysseus.base.predicate;

/**
 * @author Jonas Jacobi
 */
public class OrPredicate<T> extends ComplexPredicate<T> {
	private static final long serialVersionUID = -5476180354530944122L;

	public OrPredicate() {
		super();
	}

	public OrPredicate(IPredicate<? super T> leftPredicate,
			IPredicate<? super T> rightPredicate) {
		super(leftPredicate, rightPredicate);
	}

	public OrPredicate(OrPredicate<T> pred) {
		super(pred);
	}

	public boolean evaluate(T input) {
		return getLeft().evaluate(input) || getRight().evaluate(input);
	}

	public boolean evaluate(T left, T right) {
		return getLeft().evaluate(left, right)
				|| getRight().evaluate(left, right);
	}

	@Override
	public OrPredicate<T> clone() {
		return new OrPredicate<T>(this);
	}

	@Override
	public String toString() {
		return "(" + getLeft().toString() + ") OR (" + getRight().toString()
				+ ")";
	}
	
	public boolean equals(Object other){
		if(!(other instanceof OrPredicate)){
			return false;
		}
		else{
			return this.getLeft().equals(((OrPredicate)other).getLeft()) && this.getRight().equals(((OrPredicate)other).getRight());
		}
	}
	
	public int hashCode(){
		return 19 * this.getLeft().hashCode() + 19 * this.getRight().hashCode();
	}
}
