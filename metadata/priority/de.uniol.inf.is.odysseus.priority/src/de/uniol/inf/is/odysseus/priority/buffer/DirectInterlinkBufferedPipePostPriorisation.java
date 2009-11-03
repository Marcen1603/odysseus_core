package de.uniol.inf.is.odysseus.priority.buffer;

import java.util.Iterator;
import java.util.List;

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
		
		/*
		 * Wird ein neues Element als Basis fuer die Nachpriorisierung
		 * hinzugefuegt, muessen alle zwischengespeicherten Datenstromelemente
		 * auf ihre moegliche Nachpriorisierung hin geprueft werden.
		 */
		if(isActive) {
			
			Iterator<T> it = buffer.iterator();
			
			while(it.hasNext()) {
				functionality.executePostPriorisation(it.next());
			}
		}		
		
	}

	@Override
	public void handlePostPriorisation(T next, boolean deactivate) {

		next.getMetadata().setPriority((byte) (defaultPriority+1));
		
		transfer(next);
		
		ITimeInterval time = (ITimeInterval) next.getMetadata();
		sendPunctuation(time.getStart());
		
		if(deactivate) {
			this.setActive(false);
		}
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
	public void setJoinFragment(List<IPredicate<? super T>> fragment) {
		functionality.setJoinFragment(fragment);
	};
	
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
