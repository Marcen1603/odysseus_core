package de.uniol.inf.is.odysseus.priority;

import java.util.List;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public interface IPostPriorisationFunctionality<T extends IMetaAttributeContainer<? extends IPriority>> {
	public List<ITimeInterval> getPriorisationIntervals();
	public void executePostPriorisation(T next);
	@SuppressWarnings("unchecked")
	public void setPipe(IPostPriorisationPipe pipe);
	public void setJoinFragment(List<IPredicate<? super T>> fragment);
	public List<IPredicate<? super T>> getJoinFragment();
	@SuppressWarnings("unchecked")
	public IPostPriorisationFunctionality<IMetaAttributeContainer<? extends IPriority>> newInstance(IPostPriorisationPipe pipe);
}
