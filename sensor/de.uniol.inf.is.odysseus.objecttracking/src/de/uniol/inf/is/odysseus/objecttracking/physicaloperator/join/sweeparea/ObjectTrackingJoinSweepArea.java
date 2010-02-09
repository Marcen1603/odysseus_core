package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.sweeparea;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IApplicationTime;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.IRangePredicate;
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
@SuppressWarnings("unchecked")
public class ObjectTrackingJoinSweepArea<M extends IPredictionFunctionKey & ITimeInterval & IProbability & IApplicationTime, T extends MVRelationalTuple<M>> extends JoinTISweepArea<T>{

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
	
	public Iterator<T> query(T element, Order order){
		LinkedList<T> result = new LinkedList<T>();
		synchronized(this.elements){
			for(T next : this.elements){
				if(TimeInterval.totallyBefore(next.getMetadata(), element.getMetadata())){
					continue;
				}
				if(TimeInterval.totallyAfter(next.getMetadata(), element.getMetadata())){
					break;
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
				AndPredicate newPredFctKey = new AndPredicate((IPredicate)left.getMetadata().getPredictionFunctionKey(), (IPredicate)right.getMetadata().getPredictionFunctionKey());
				
				IRangePredicate rangePredicate = this.rangePredicates.get(newPredFctKey);
				List<ITimeInterval> intervals = rangePredicate.evaluate(left, right);
				
				this.compareCounter++;
				
				if(!intervals.isEmpty()){
					result.add(next);
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
