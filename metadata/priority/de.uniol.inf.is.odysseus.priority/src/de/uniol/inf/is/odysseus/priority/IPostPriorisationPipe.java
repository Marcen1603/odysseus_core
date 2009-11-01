package de.uniol.inf.is.odysseus.priority;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public interface IPostPriorisationPipe<T extends IMetaAttributeContainer<? extends IPriority>>{
	public void handlePostPriorisation(T next);
	public void setJoinFragment(IPredicate<? super T> fragment);
	public void addTimeInterval(ITimeInterval time);
	public boolean isActive();
	public void setActive(boolean active);
}
