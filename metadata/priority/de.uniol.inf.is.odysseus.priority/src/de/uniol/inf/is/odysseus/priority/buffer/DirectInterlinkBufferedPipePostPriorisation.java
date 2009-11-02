package de.uniol.inf.is.odysseus.priority.buffer;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.priority.IPostPriorisationPipe;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.priority.PostPriorisationFunctionality;

public class DirectInterlinkBufferedPipePostPriorisation<T extends IMetaAttributeContainer<? extends IPriority>>
		extends DirectInterlinkBufferedPipe<T> implements
		IPostPriorisationPipe<T> {

	private boolean isActive = true;
	private byte defaultPriority;
	

	private PostPriorisationFunctionality<T> functionality;
	
	@Override
	public void addTimeInterval(ITimeInterval time) {
		functionality.getPriorisationIntervals().add(time);
	}

	@Override
	public void handlePostPriorisation(T next) {
		next.getMetadata().setPriority((byte) (defaultPriority+1));
		
		
		
		// TODO PostPriorisationBuffer macht erst einmal das gleiche wie PostPriorisationPO
		// => Funktionalitaet momentan noch nicht fertig

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
	public void setJoinFragment(IPredicate<? super T> fragment) {
		functionality.setJoinFragment(fragment);
	}
	
	@Override
	public void transfer(T object) {
		if(isActive) {
			functionality.executePostPriorisation(object);
		}
		
		transfer(object,0);
		
		if(isActive) {		
			updatePunctuationData(object);
		}
	}	

}
