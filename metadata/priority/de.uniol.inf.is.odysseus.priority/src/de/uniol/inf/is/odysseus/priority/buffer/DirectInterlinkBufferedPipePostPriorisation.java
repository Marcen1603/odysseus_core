package de.uniol.inf.is.odysseus.priority.buffer;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.AbstractPunctuationBuffer;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
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

	private boolean isActive = false;
	private PriorityPO<?> priorisationOwner = null;
	@SuppressWarnings("unchecked")
	private IPostPriorisationFunctionality functionality;
	
	private final POEvent finished = new POEvent(this, POEventType.PostPriorisation);

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	public void setActive(boolean isActive) {
		this.isActive = isActive;
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
		
		if (isActive() && functionality != null) {
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

	@SuppressWarnings("unchecked")
	@Override
	public void addTimeInterval(IMetaAttributeContainer<?>  time) {
		if (functionality != null) {
			setActive(true);
			functionality.getPriorisationIntervals().add(time.clone());

			Iterator<T> it = buffer.iterator();

			// Puffer bei jeder potenziellen Nachpriorisierung nachpriorisieren
			while (it.hasNext()) {
				T object = it.next();
				functionality.executePostPriorisation(object);
				if (object.getMetadata().getPriority() > 0) {
					sendElement(object);
				}
			}
		}
	}

	@Override
	public void handlePostPriorisation(
			IMetaAttributeContainer<? extends IPriority> next,
			boolean deactivate, boolean matchPredicate) {

		ITimeInterval time = (ITimeInterval) next.getMetadata();
		sendPunctuation(time.getStart());

		if (deactivate) {
			this.setActive(false);
		}
		
		if (matchPredicate) {
			fire(finished);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void setJoinFragment(
			List<IPredicate<? super IMetaAttributeContainer<? extends IPriority>>> fragment) {
		functionality.setJoinFragment(fragment);
	}

	@Override
	public PriorityPO<?> getPhysicalPostPriorisationRoot() {
		return priorisationOwner;
	}

	@Override
	public void setPhysicalPostPriorisationRoot(PriorityPO<?> priorityPO) {
		priorisationOwner = priorityPO;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IPredicate<? super IMetaAttributeContainer<? extends IPriority>>> getJoinFragment() {
		return functionality.getJoinFragment();
	};


	@Override
	public void setPostPriorisationFunctionality(
			IPostPriorisationFunctionality<IMetaAttributeContainer<? extends IPriority>> functionality) {
		this.functionality = functionality;

	}

}