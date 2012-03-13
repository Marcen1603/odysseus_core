/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;

public class OrRangePredicate<T> extends ComplexRangePredicate<T>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1540830902126498827L;

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
	@Override
	public List<ITimeInterval> evaluate(T input){
		List<ITimeInterval> resultRanges = new ArrayList<ITimeInterval>();
		List<ITimeInterval> leftRanges = this.left.evaluate(input);
		List<ITimeInterval> rightRanges = this.right.evaluate(input);
		
		resultRanges.addAll(leftRanges);
		resultRanges.addAll(rightRanges);
		
		Collections.sort(resultRanges);
		
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
	@Override
	public List<ITimeInterval> evaluate(T left, T right){
		List<ITimeInterval> resultRanges = new ArrayList<ITimeInterval>();
		List<ITimeInterval> leftRanges = this.left.evaluate(left, right);
		List<ITimeInterval> rightRanges = this.right.evaluate(left, right);
		
		resultRanges.addAll(leftRanges);
		resultRanges.addAll(rightRanges);
		
		Collections.sort(resultRanges);
		
		return resultRanges;
	}
	
	@Override
	@SuppressWarnings({"rawtypes"})
	public boolean equals(Object other){
		if(!(other instanceof OrRangePredicate)){
			return false;
		}
        return this.left.equals(((OrRangePredicate)other).getLeft()) && this.right.equals(((OrRangePredicate)other).getRight());
	}
	
	@Override
	public int hashCode(){
		return 53 * this.left.hashCode() + 41 * this.right.hashCode();
	}
	
	@Override
	public String toString(){
		return this.left.toString() + " OR " + this.right.toString();
	}
}
