package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;

public class OrRangePredicate<T> extends ComplexRangePredicate<T>{

	
	public OrRangePredicate(IRangePredicate<T> left, IRangePredicate<T> right){
		super(left, right);
	}
	
	/**
	 * Produces the following output
	 * If the left predicate has intervals a, b, c
	 * and the right predicate has interval d, e, f
	 * then a, b, c, d, e, f are in the result. No union
	 * of overlapping intervals will be done, since this will
	 * be to expensive for stream processing. It will be
	 * faster to compare some more intervals.
	 */
	public List<ITimeInterval> evaluate(T input){
		List<ITimeInterval> resultRanges = new ArrayList<ITimeInterval>();
		List<ITimeInterval> leftRanges = this.left.evaluate(input);
		List<ITimeInterval> rightRanges = this.right.evaluate(input);
		
		resultRanges.addAll(leftRanges);
		resultRanges.addAll(rightRanges);
		
		return resultRanges;
		
	}
	
	/**
	 * Produces the following output
	 * If the left predicate has intervals a, b, c
	 * and the right predicate has interval d, e, f
	 * then a, b, c, d, e, f are in the result. No union
	 * of overlapping intervals will be done, since this will
	 * be to expensive for stream processing. It will be
	 * faster to compare some more intervals.
	 */
	public List<ITimeInterval> evaluate(T left, T right){
		List<ITimeInterval> resultRanges = new ArrayList<ITimeInterval>();
		List<ITimeInterval> leftRanges = this.left.evaluate(left, right);
		List<ITimeInterval> rightRanges = this.right.evaluate(left, right);
		
		resultRanges.addAll(leftRanges);
		resultRanges.addAll(rightRanges);
		
		return resultRanges;
	}
	
	public boolean equals(Object other){
		if(!(other instanceof OrRangePredicate)){
			return false;
		}
		else{
			return this.left.equals(((OrRangePredicate)other).getLeft()) && this.right.equals(((OrRangePredicate)other).getRight());
		}
	}
	
	public int hashCode(){
		return 53 * this.left.hashCode() + 41 * this.right.hashCode();
	}
	
	public String toString(){
		return this.left.toString() + " OR " + this.right.toString();
	}
}
