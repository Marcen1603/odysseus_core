package de.uniol.inf.is.odysseus.priority;

import java.util.List;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.AbstractPunctuationPipe;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public class PostPriorisationPO<T extends IMetaAttributeContainer<? extends IPriority>>
extends AbstractPunctuationPipe<T,T> implements IPostPriorisationPipe<T>{
 
	private boolean isActive = true;
	private PriorityPO<?> priorisationOwner = null;
	

	@SuppressWarnings("unchecked")
	private IPostPriorisationFunctionality functionality;
	@SuppressWarnings("unchecked")
	private List predicates;

	
	@SuppressWarnings("unchecked")
	public IPostPriorisationFunctionality getPostPriorisationFunctionality() {
		return functionality;
	}
	
	public PostPriorisationPO(PostPriorisationAO<T> postAO) {
		super();
		this.isActive = postAO.isActive();
		this.predicates = postAO.getPredicates();
	}	
	
	@SuppressWarnings("unchecked")
	public void setPostPriorisationFunctionality(IPostPriorisationFunctionality functionality) {
		this.functionality = functionality;
		setJoinFragment(predicates);
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
	public void handlePostPriorisation(T next, boolean deactivate,
			boolean matchPredicate) {

		ITimeInterval time = (ITimeInterval) next.getMetadata();
		sendPunctuation(time.getStart());
		
		if(deactivate) {
			this.setActive(false);
		}
	}

	@Override
	public boolean cleanInternalStates(PointInTime punctuation,
			IMetaAttributeContainer<?> current) {
		return true;
	}


	
	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	@Override
	public void setJoinFragment(List<IPredicate<? super T>> fragment) {
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
	public List<IPredicate<? super T>> getJoinFragment() {
		return functionality.getJoinFragment();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addTimeInterval(IMetaAttributeContainer<?>  time) {
		setActive(true);
		functionality.getPriorisationIntervals().add(time.clone());
	}


}
