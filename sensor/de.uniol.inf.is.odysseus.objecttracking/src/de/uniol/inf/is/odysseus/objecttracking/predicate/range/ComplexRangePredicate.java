package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

import java.util.List;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;

public abstract class ComplexRangePredicate<T> extends
		AbstractRangePredicate<T> {

	protected IRangePredicate<T> left;
	public IRangePredicate<T> getLeft() {
		return left;
	}

	public void setLeft(IRangePredicate<T> left) {
		this.left = left;
	}

	public IRangePredicate<T> getRight() {
		return right;
	}

	public void setRight(IRangePredicate<T> right) {
		this.right = right;
	}

	protected IRangePredicate<T> right;
	
	public ComplexRangePredicate(IRangePredicate<T> left, IRangePredicate<T> right){
		super();
		this.left = left;
		this.right = right;
	}
	
	public ComplexRangePredicate<T> clone(){
		ComplexRangePredicate<T> newPred = (ComplexRangePredicate<T>)super.clone();
		newPred.left = this.left.clone();
		newPred.right = this.right.clone();
		return newPred;
	}
	
}
