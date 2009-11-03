package de.uniol.inf.is.odysseus.priority;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public class PostPriorisationFunctionality<T extends IMetaAttributeContainer<? extends IPriority>> {
	
	private List<IPredicate<? super T>> joinFragment = null;
	
	private boolean deactivate = false;

	public List<IPredicate<? super T>> getJoinFragment() {
		return joinFragment;
	}

	public void setJoinFragment(List<IPredicate<? super T>> fragment) {
		this.joinFragment = fragment;
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
		// Wenn Gueltigkeitszeitraum verlassen wird, dann kann Nachpriorisierung abgebrochen werden
		if(priorisationIntervals.size() == 0) {
			deactivate = true;
		}
		
		return result;
	}	
	
	public List<ITimeInterval> getPriorisationIntervals() {
		pipe.setActive(true);
		return priorisationIntervals;
	}


	public void setPriorisationIntervals(List<ITimeInterval> priorisationIntervals) {
		this.priorisationIntervals = priorisationIntervals;
	}	
	
	public void executePostPriorisation(T next) {
		deactivate = false;
		
		ITimeInterval currentInterval = (ITimeInterval) next.getMetadata();
		
		if(hasToBePrefered(currentInterval)) {		
			
			Iterator<IPredicate<? super T>> it = joinFragment.iterator();
			
			while(it.hasNext()) {
				IPredicate<? super T> current = it.next();
				// Wenn Aquivalenzvergleich erfuellt wird, dann kann Nachpriorisierung abgebrochen werden
				if(current.evaluate(next)) {
					deactivate = true;
				}
			}
			
			pipe.handlePostPriorisation(next, deactivate);
		}	
	}
	
	
}
