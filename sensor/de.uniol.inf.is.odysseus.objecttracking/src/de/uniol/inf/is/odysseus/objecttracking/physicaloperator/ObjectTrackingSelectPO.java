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

@SuppressWarnings("unchecked")
public class ObjectTrackingSelectPO<T extends IMetaAttributeContainer<M>, M extends IPredictionFunctionKey & IApplicationTime> extends AbstractPipe<T, T> {

	private Map<IPredicate<? super T>, IRangePredicate> rangePredicates;
	
	public ObjectTrackingSelectPO(Map<IPredicate<? super T>, IRangePredicate> rangePredicates){
		this.rangePredicates = rangePredicates;
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
		
		List<ITimeInterval> appIntervals = rangePredicate.evaluate(object);
		
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
	
	@Override
	public ObjectTrackingSelectPO<T, M> clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}

