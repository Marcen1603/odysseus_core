package de.uniol.inf.is.odysseus.priority;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.AbstractPunctuationPipe;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public class PostPriorisationPO<T extends IMetaAttributeContainer<? extends IPriority>>
extends AbstractPunctuationPipe<T,T>{
 

	private boolean doPostPriorisation = false;
	private List<ITimeInterval> priorisationIntervals = new ArrayList<ITimeInterval>();

	private boolean isActive = true;
	private byte defaultPriority;
	
	/*private IPredicate<? super T> joinFragment = null;*/

	
	public PostPriorisationPO(PostPriorisationAO<T> postAO) {
		super();
		this.isActive = postAO.isActive();
		this.defaultPriority = postAO.getDefaultPriority();
	}	
	
	
	
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}	
	
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(T next, int port) {
		if (isActive) {
			doPostPriorisation = true;
			
			ITimeInterval currentInterval = (ITimeInterval) next.getMetadata();
			
			if(hasToBePrefered(currentInterval) /*|| 
					(joinFragment != null && joinFragment.evaluate(next))*/) {
				next.getMetadata().setPriority((byte) (defaultPriority+1));
			}		
		} 
		
		transfer(next);
	}
	
	@Override
	public boolean cleanInternalStates(PointInTime punctuation,
			IMetaAttributeContainer<?> current) {
		return true;
	}

	@Override
	public void sendPunctuation(PointInTime punctuation) {
		if (doPostPriorisation ) {
			synchronized (this.subscriptions) {
				super.sendPunctuation(punctuation);
			}
			doPostPriorisation = false;
		}
	}

	public void addTimeInterval(ITimeInterval time) {
		priorisationIntervals.add(time);
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

}
