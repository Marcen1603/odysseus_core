package de.uniol.inf.is.odysseus.priority;

import java.util.List;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public interface IPostPriorisationPipe<T extends IMetaAttributeContainer<? extends IPriority>>{
	public void handlePostPriorisation(T next, boolean deactivate);
	public void setJoinFragment(List<IPredicate<? super T>> fragment);
	public List<IPredicate<? super T>> getJoinFragment();
	public void addTimeInterval(ITimeInterval time);
	public boolean isActive();
	public void setActive(boolean active);
	public void setPhysicalPostPriorisationRoot(PriorityPO<?> priorityPO);
	public PriorityPO<?> getPhysicalPostPriorisationRoot();
}
