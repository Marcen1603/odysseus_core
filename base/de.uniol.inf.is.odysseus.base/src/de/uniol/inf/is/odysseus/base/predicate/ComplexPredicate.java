package de.uniol.inf.is.odysseus.base.predicate;

/**
 * @author Jonas Jacobi
 */
public abstract class ComplexPredicate<T> extends AbstractPredicate<T> {
	private static final long serialVersionUID = 5112319812675937729L;

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

	@Override
	public ComplexPredicate<T> clone() {
		ComplexPredicate<T> newPred;
		newPred = (ComplexPredicate<T>) super.clone();
		newPred.left = this.left.clone();
		newPred.right = this.right.clone();
		return newPred;
	}
	
	@Override
	public void init(){
		this.left.init();
		this.right.init();
	}
	
	@Override
	public boolean equals(Object other){
		if(!(other instanceof ComplexPredicate)){
			return false;
		}
		else{
			return this.left.equals(((ComplexPredicate)other).left) &&
				this.right.equals(((ComplexPredicate)other).right);
		}
	}
	
	@Override
	public int hashCode(){
		return 37 * this.left.hashCode() + 41 * this.right.hashCode();
	}

}
