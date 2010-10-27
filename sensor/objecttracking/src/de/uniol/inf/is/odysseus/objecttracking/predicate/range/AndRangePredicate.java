package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;

public class AndRangePredicate<T> extends ComplexRangePredicate<T>{

	private static final long serialVersionUID = 5713674399965934142L;

	public AndRangePredicate(IRangePredicate<T> left, IRangePredicate<T> right){
		super(left, right);
	}
	
	/**
	 * Produces the following output (4 Intervals)
	 * 			 			  | |   | | |  |      |   |
	 * left predicate:		--|-|   |-|-|--|    --|---|
	 * right predicate: 	  |-|---|-| |--|--    |---|---
	 * 			  			  | |   | | |  |      |   |
	 */
	@Override
	public List<ITimeInterval> evaluate(T input){
		List<ITimeInterval> resultRanges = new ArrayList<ITimeInterval>();
		List<ITimeInterval> leftRanges = this.left.evaluate(input);
		List<ITimeInterval> rightRanges = this.right.evaluate(input);
		
		
		outer:
		for(ITimeInterval leftInterval: leftRanges){
			for(ITimeInterval rightInterval: rightRanges){
				if(TimeInterval.totallyAfter(rightInterval, leftInterval)){
					// TODO funktioniert das mit dem Label so?
					// Es muss mit dem nächsten Intervall aus der linken Liste
					// weitergemacht werden.
					continue outer;
				}
				
				ITimeInterval intersection = TimeInterval.intersection(leftInterval, rightInterval);
				if(intersection != null){
					resultRanges.add(intersection);
				}
			}
		}
		
		Collections.sort(resultRanges);
		
		return resultRanges;
	}
	
	/**
	 * Produces the following output (4 Intervals)
	 * 			 			  | |   | | |  |      |   |
	 * left predicate:		--|-|   |-|-|--|    --|---|
	 * right predicate: 	  |-|---|-| |--|--    |---|---
	 * 			  			  | |   | | |  |      |   |
	 */
	@Override
	public List<ITimeInterval> evaluate(T left, T right){
		List<ITimeInterval> resultRanges = new ArrayList<ITimeInterval>();
		List<ITimeInterval> leftRanges = this.left.evaluate(left, right);
		List<ITimeInterval> rightRanges = this.right.evaluate(left, right);
		
		// if one of both lists is empty, one can return an empty list.
		if(!leftRanges.isEmpty() && !rightRanges.isEmpty()){
			
			outer:
			for(ITimeInterval leftInterval: leftRanges){
				for(ITimeInterval rightInterval: rightRanges){
					if(TimeInterval.totallyAfter(rightInterval, leftInterval)){
						// TODO funktioniert das mit dem Label so?
						// Es muss mit dem nächsten Intervall aus der linken Liste
						// weitergemacht werden.
						continue outer;
					}
					
					ITimeInterval intersection = TimeInterval.intersection(leftInterval, rightInterval);
					if(intersection != null){
						resultRanges.add(intersection);
					}
				}
			}
		}
		
		Collections.sort(resultRanges);
		
		return resultRanges;
	}
	
	@Override
	public boolean equals(Object other){
		if(!(other instanceof AndRangePredicate)){
			return false;
		}
		else{
			return this.left.equals(((AndRangePredicate)other).getLeft()) && this.right.equals(((AndRangePredicate)other).getRight());
		}
	}
	
	@Override
	public int hashCode(){
		return 53 * this.left.hashCode() * 41 * this.right.hashCode();
	}
	
	@Override
	public String toString(){
		return this.left.toString() + " AND " + this.right.toString();
	}
}
