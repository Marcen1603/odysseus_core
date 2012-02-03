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
package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.sweeparea;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.IPredictionFunctionKeyTimeIntervalProbabilityApplicationTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.IRangePredicate;
import de.uniol.inf.is.odysseus.objecttracking.util.Pair;
import de.uniol.inf.is.odysseus.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;



/**
 * This is a sweep area, that uses prediction functions for evaluating predicates.
 * If two elements overlap, only their values at max(left.ts, right.ts) will
 * be used for evaluating p_query
 * 
 * @TODO If anyone has an idea, how to substitute the casts by generics, tell me!
 * 
 * @author abolles
 *
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class ObjectTrackingJoinSweepArea<M extends IPredictionFunctionKeyTimeIntervalProbabilityApplicationTime, T extends MVRelationalTuple<M>> extends JoinTISweepArea<T>{
	
	SDFAttributeList leftSchema;
	SDFAttributeList rightSchema;
	
	Map<IPredicate, IRangePredicate> rangePredicates;
	
	public int compareCounter;
	
	public ObjectTrackingJoinSweepArea(SDFAttributeList leftSchema, SDFAttributeList rightSchema){
		super();
		this.leftSchema = leftSchema;
		this.rightSchema = rightSchema;
		this.compareCounter = 0;
	}
	
	public void setRangePredicates(Map<IPredicate, IRangePredicate> rangePredicates){
		this.rangePredicates = rangePredicates;
	}
	
	public Iterator<Pair<T, List<ITimeInterval>>> queryOT(T element, Order order){
		LinkedList<Pair<T, List<ITimeInterval>>> result = new LinkedList<Pair<T, List<ITimeInterval>>>();
		synchronized(this.elements){
			for(T next : this.elements){
				if(TimeInterval.totallyBefore(next.getMetadata(), element.getMetadata())){
					continue;
				}
				if(TimeInterval.totallyAfter(next.getMetadata(), element.getMetadata())){
					break;
				}
				
				// comparing application times
				boolean appTimeOverlapping = false;
				outer:
				for(ITimeInterval curIntervalNext : next.getMetadata().getAllApplicationTimeIntervals()){
					for(ITimeInterval curIntervalElem : element.getMetadata().getAllApplicationTimeIntervals()){
						if(TimeInterval.overlaps(curIntervalNext, curIntervalElem)){
							appTimeOverlapping = true;
							break outer;
						}
					}
				}
				
				
				if(!appTimeOverlapping){
					// try the next element
					continue;
				}
				
				T left = null;
				T right = null;
				
				switch (order) {
					case LeftRight:
						left = element;
						right = next;
						break;
					case RightLeft:
						left = next;
						right = element;
						break;
				}
				
				// Build the prediction function key
				// get the correct rangePredicate
				// evaluate the rangePredicate and
				// add the SA element to result
				IPredicate newPredFctKey = ComplexPredicateHelper.createAndPredicate((IPredicate)left.getMetadata().getPredictionFunctionKey(), (IPredicate)right.getMetadata().getPredictionFunctionKey());
				
				IRangePredicate rangePredicate = this.rangePredicates.get(newPredFctKey);
				List<ITimeInterval> intervals = rangePredicate.evaluate(left, right);
				
				this.compareCounter++;
				
				// if there are already some intervals
				// these have to be AND concatenated with
				// the new ones.
				
				if(!intervals.isEmpty()){
					result.add(new Pair<T, List<ITimeInterval>>(next, intervals));
				}
			}
		}		
		return result.iterator();
	}
	
	@Override
	public void init(){
		// do nothing, the predicates have already been initialized.
	}
}
