package de.uniol.inf.is.odysseus.priority;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public class PostPriorisationFunctionality<T extends IMetaAttributeContainer<? extends IPriority>> {
	
	private IPredicate<? super T> joinFragment = null;

	public IPredicate<? super T> getJoinFragment() {
		return joinFragment;
	}

	public void setJoinFragment(IPredicate<? super T> joinFragment) {
		this.joinFragment = joinFragment;
	}


	private List<ITimeInterval> priorisationIntervals = new ArrayList<ITimeInterval>();

	private IPostPriorisationPipe<T> pipe;

	public PostPriorisationFunctionality(IPostPriorisationPipe<T> pipe) {
		this.pipe = pipe;
	}
	
	public boolean hasToBePrefered(ITimeInterval current) {
		
		Iterator<ITimeInterval> it = priorisationIntervals.iterator();
		
		boolean result = false;
		
		while(it.hasNext()) {
			ITimeInterval each = it.next();
			if(TimeInterval.inside(current, each)) {
				result = true;
			} else if(TimeInterval.startsBefore(each, current)){
				it.remove();
			}
		}
		
		return result;
	}	
	
	public List<ITimeInterval> getPriorisationIntervals() {
		return priorisationIntervals;
	}


	public void setPriorisationIntervals(List<ITimeInterval> priorisationIntervals) {
		this.priorisationIntervals = priorisationIntervals;
	}	
	
	public void executePostPriorisation(T next) {

		ITimeInterval currentInterval = (ITimeInterval) next.getMetadata();
		
		if(hasToBePrefered(currentInterval) || 
				(joinFragment != null && joinFragment.evaluate(next))) {
			pipe.handlePostPriorisation(next);
		}	
	}
	
	
}
