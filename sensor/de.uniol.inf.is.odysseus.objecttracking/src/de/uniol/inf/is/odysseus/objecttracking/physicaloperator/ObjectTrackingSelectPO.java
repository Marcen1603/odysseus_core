package de.uniol.inf.is.odysseus.objecttracking.physicaloperator;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IApplicationTime;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.IRangePredicate;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

public class ObjectTrackingSelectPO<T extends IMetaAttributeContainer<M>, M extends IPredictionFunctionKey & IApplicationTime> extends AbstractPipe<T, T> {

	private Map<IPredicate<? super T>, IRangePredicate> rangePredicates;
	private IRangePredicate defaultRangePredicate;
	
	public ObjectTrackingSelectPO(Map<IPredicate<? super T>, IRangePredicate> rangePredicates, IRangePredicate defaultRangePredicate){
		this.rangePredicates = rangePredicates;
		this.defaultRangePredicate = defaultRangePredicate;
	}
	
	public int hashCode() {
		final int prime = 29;
		int result = 7;
		result = prime * result
				+ ((this.rangePredicates == null) ? 0 : this.rangePredicates.hashCode());
		return result;
	}

	@Override
	public OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		// first get the range predicate
		IRangePredicate rangePredicate = this.rangePredicates.get(object.getMetadata().getPredictionFunctionKey());
		
		List<ITimeInterval> appIntervals = null;
		// if the range predicate is null
		// the default range predicate has to be used.
		if(rangePredicate == null){
			appIntervals = this.defaultRangePredicate.evaluate(object);
		}
		else{
			appIntervals = rangePredicate.evaluate(object);
		}
		
		// only transfer the object, if it is valid in future!
		// What is with elements, that are valid in the past
		// but not in the future?
		if(!appIntervals.isEmpty()){
			// set the time intervals and return the tuple
			// TODO intervals setzen.
			object.getMetadata().setApplicationIntervals(appIntervals);
			
			transfer(object);
		}
	}
}
