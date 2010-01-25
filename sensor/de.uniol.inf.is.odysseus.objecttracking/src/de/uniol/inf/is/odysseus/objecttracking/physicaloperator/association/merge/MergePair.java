package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.association.merge;

/**
 * This class is used in a prediction join and the corresponding SweepAreas.
 * In the SweepAreas elements have to be predicted first and then evaluated.
 * They must then be predicted again in the join if the query predicate is true.
 * So, in a prediction join we return both, the predicted element and the element
 * from the SweepArea, so that we do not have to predict them again in the join
 * for merging.
 * 
 * @author abolles
 *
 * @param <T> The element carried by this class. Both, left and right, must be
 * of the same type.
 */
public class MergePair<T>{

	T left;
	T right;
	
	public MergePair(T left, T right){
		this.left = left;
		this.right = right;
	}
	
	public T getLeft(){
		return left;
	}
	
	public T getRight(){
		return right;
	}
	
	public void setLeft(T left){
		this.left = left;
	}
	
	public void setRight(T right){
		this.right = right;
	}
}
