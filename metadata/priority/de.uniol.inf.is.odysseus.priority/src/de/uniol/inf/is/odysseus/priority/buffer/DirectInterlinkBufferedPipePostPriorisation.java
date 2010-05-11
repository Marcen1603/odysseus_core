package de.uniol.inf.is.odysseus.priority.buffer;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.AbstractPunctuationBuffer;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.priority.IPostPriorisationFunctionality;
import de.uniol.inf.is.odysseus.priority.IPostPriorisationPipe;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.priority.PriorityPO;

public class DirectInterlinkBufferedPipePostPriorisation<T extends IMetaAttributeContainer<? extends IPriority>>
		extends AbstractPunctuationBuffer<T, T> implements
		IPostPriorisationPipe<IMetaAttributeContainer<? extends IPriority>> {
	
	Lock directLinkLock = new ReentrantLock();
	@SuppressWarnings("unchecked")
	private IPostPriorisationFunctionality functionality;
	private PriorityPO<?> postPriorisationRoot;
	private boolean active;
	private POEvent finished = new POEvent(this, POEventType.PostPriorisation);

	public DirectInterlinkBufferedPipePostPriorisation(){};
	
	public DirectInterlinkBufferedPipePostPriorisation(
			DirectInterlinkBufferedPipePostPriorisation<T> directInterlinkBufferedPipePostPriorisation) {
		super(directInterlinkBufferedPipePostPriorisation);
		functionality = directInterlinkBufferedPipePostPriorisation.functionality.clone();
		postPriorisationRoot = directInterlinkBufferedPipePostPriorisation.postPriorisationRoot;
		active = directInterlinkBufferedPipePostPriorisation.active;
	}

	@Override
	public void transferNext() {
		directLinkLock.lock();
		super.transferNext();
		directLinkLock.unlock();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	protected synchronized void process_next(T object, int port) {
		
		storage.setCurrentPort(port);
		
		if(active && functionality != null) {
			functionality.executePostPriorisation(object);
		}

		sendElement(object);
	}
	
	public void sendElement(T object) {
		byte prio = object.getMetadata().getPriority();

		// Load Shedding
		if (prio < 0) {
			return;
		}

		if (prio > 0) {
			directLinkLock.lock();
			transfer(object);
			directLinkLock.unlock();
		} else {
			synchronized (this.buffer) {
				this.buffer.add(object);
			}
		}
	}

	@Override
	public boolean cleanInternalStates(PointInTime punctuation,
			IMetaAttributeContainer<?> current) {
		return true;
	}

	@Override
	public PriorityPO<?> getPhysicalPostPriorisationRoot() {
		return postPriorisationRoot;
	}

	@Override
	public void handlePostPriorisation(
			IMetaAttributeContainer<? extends IPriority> next,
			boolean deactivate, boolean matchPredicate) {

		// TODO Punctuations nur senden, sobald sie auch im Gesamtsystem aktiviert sind
		//ITimeInterval time = (ITimeInterval) next.getMetadata();
		//sendPunctuation(time.getStart());

		if (deactivate) {
			this.setActive(false);
		}
		
		if (matchPredicate) {
			fire(finished);
		}
		
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public void setPhysicalPostPriorisationRoot(PriorityPO<?> priorityPO) {
		this.postPriorisationRoot = priorityPO;
		
	}

	@Override
	public void setPostPriorisationFunctionality(
			IPostPriorisationFunctionality<IMetaAttributeContainer<? extends IPriority>> functionality) {
		this.functionality = functionality;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addTimeInterval(IMetaAttributeContainer<?>  time) {
		setActive(true);
		try {
			functionality.getPriorisationIntervals().add(time.clone());
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("No clone method");
		}
		
		Iterator<T> it = buffer.iterator();

		// Puffer bei jeder potenziellen Nachpriorisierung nachpriorisieren
		while (it.hasNext()) {
			T object = it.next();
			functionality.executePostPriorisation(object);
			if (object.getMetadata().getPriority() > 0) {
				sendElement(object);
				it.remove();
			}
		}		
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IPredicate<? super IMetaAttributeContainer<? extends IPriority>>> getJoinFragment() {
		return functionality.getJoinFragment();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setJoinFragment(List fragment) {
		functionality.setJoinFragment(fragment);
	}
	
	public DirectInterlinkBufferedPipePostPriorisation<T> clone(){
		return new DirectInterlinkBufferedPipePostPriorisation<T>(this);
	}
}