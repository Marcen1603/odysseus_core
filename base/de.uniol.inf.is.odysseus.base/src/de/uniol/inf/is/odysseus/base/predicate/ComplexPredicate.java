package de.uniol.inf.is.odysseus.base.predicate;

/**
 * @author Jonas Jacobi
 */
public abstract class ComplexPredicate<T> extends AbstractPredicate<T> {
	private IPredicate<? super T> left;

	private IPredicate<? super T> right;

	public ComplexPredicate() {
		
	}
	
	public ComplexPredicate(IPredicate<? super T> left,
			IPredicate<? super T> right) {
		this.left = left;
		this.right = right;
	}

	public ComplexPredicate(ComplexPredicate<T> pred) {
		this.left = pred.left.clone();
		this.right = pred.right.clone();
	}

	public IPredicate<? super T> getLeft() {
		return left;
	}

	public void setLeft(IPredicate<? super T> left) {
		this.left = left;
	}

	public IPredicate<? super T> getRight() {
		return right;
	}

	public void setRight(IPredicate<? super T> right) {
		this.right = right;
	}

	@SuppressWarnings("unchecked")
	public ComplexPredicate<T> clone() {
		ComplexPredicate<T> newPred;
		newPred = (ComplexPredicate<T>) super.clone();
		newPred.left = this.left.clone();
		newPred.right = this.right.clone();
		return newPred;
	}
	
	public void init(){
		this.left.init();
		this.right.init();
	}

}
