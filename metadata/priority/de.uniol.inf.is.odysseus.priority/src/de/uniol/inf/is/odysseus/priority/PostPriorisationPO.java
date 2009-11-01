package de.uniol.inf.is.odysseus.priority;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.AbstractPunctuationPipe;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public class PostPriorisationPO<T extends IMetaAttributeContainer<? extends IPriority>>
extends AbstractPunctuationPipe<T,T> implements IPostPriorisationPipe<T>{
 
	private boolean isActive = true;
	private byte defaultPriority;
	

	private PostPriorisationFunctionality<T> functionality;

	
	public PostPriorisationPO(PostPriorisationAO<T> postAO) {
		super();
		this.isActive = postAO.isActive();
		this.defaultPriority = postAO.getDefaultPriority();
		this.functionality = new PostPriorisationFunctionality<T>(this);
	}	
	
	@Override	
	public boolean isActive() {
		return isActive;
	}
	
	@Override	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}	
	
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(T next, int port) {
		transfer(next);
	}
	
	@Override	
	public void handlePostPriorisation(T next) {
		next.getMetadata().setPriority((byte) (defaultPriority+1));
	}

	@Override
	public boolean cleanInternalStates(PointInTime punctuation,
			IMetaAttributeContainer<?> current) {
		return true;
	}

	@Override	
	public void addTimeInterval(ITimeInterval time) {
		functionality.getPriorisationIntervals().add(time);
	}
	
	public void transfer(T object) {
		if(isActive) {
			functionality.executePostPriorisation(object);
		}
		
		transfer(object,0);
		
		if(isActive) {		
			updatePunctuationData(object);
		}
	}

	@Override
	public void setJoinFragment(IPredicate<? super T> fragment) {
		functionality.setJoinFragment(fragment);
	};		

}
