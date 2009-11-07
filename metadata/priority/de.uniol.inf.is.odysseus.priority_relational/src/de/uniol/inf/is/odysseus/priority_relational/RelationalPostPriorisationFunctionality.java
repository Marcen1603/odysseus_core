package de.uniol.inf.is.odysseus.priority_relational;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.priority.IPostPriorisationFunctionality;
import de.uniol.inf.is.odysseus.priority.IPostPriorisationPipe;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class RelationalPostPriorisationFunctionality<T extends IMetaAttributeContainer<? extends IPriority>> implements IPostPriorisationFunctionality<T> {

	public static final String OPERATOR_EQUALS = "==";
	
	private List<IPredicate<? super T>> joinFragment = null;

	private boolean deactivate = false;
	
	private Byte currentPriority = null;

	public List<IPredicate<? super T>> getJoinFragment() {
		return joinFragment;
	}

	public void setJoinFragment(List<IPredicate<? super T>> fragment) {
		this.joinFragment = fragment;
	}

	private List<IMetaAttributeContainer<? extends IPriority>> priorisationIntervals = new ArrayList<IMetaAttributeContainer<? extends IPriority>>();

	private IPostPriorisationPipe<T> pipe;

	public RelationalPostPriorisationFunctionality(IPostPriorisationPipe<T> pipe) {
		this.pipe = pipe;
	}
	
	public RelationalPostPriorisationFunctionality() {
		
	}

	public boolean hasToBePrefered(ITimeInterval current) {

		Iterator<IMetaAttributeContainer<? extends IPriority>> it = priorisationIntervals.iterator();

		while (it.hasNext()) {
			
			IMetaAttributeContainer<? extends IPriority> next = it.next();
			
			ITimeInterval each = (ITimeInterval) next.getMetadata();
			if (TimeInterval.inside(current, each)) {
				currentPriority = new Byte(((IPriority) next.getMetadata()).getPriority());
				return true;
			} else if (TimeInterval.startsBefore(each, current)) {
				it.remove();
			}
		}
		// Wenn Gueltigkeitszeitraum verlassen wird, dann kann Nachpriorisierung
		// abgebrochen werden
		if (priorisationIntervals.size() == 0) {
			deactivate = true;
		}
		return false;
	}

	public List<IMetaAttributeContainer<? extends IPriority>> getPriorisationIntervals() {
		pipe.setActive(true);
		return priorisationIntervals;
	}

	public void setPriorisationIntervals(
			List<IMetaAttributeContainer<? extends IPriority>> priorisationIntervals) {
		this.priorisationIntervals = priorisationIntervals;
	}

	public void executePostPriorisation(T next) {
		
		if(next.getMetadata().getPriority() > 0) {
			return;
		}
		
		deactivate = false;

		ITimeInterval currentInterval = (ITimeInterval) next.getMetadata();
		
		if (hasToBePrefered(currentInterval)) {

			boolean cancelPostPriorisation = false;
			boolean matchPredicate = false;
			
			if (joinFragment != null) {

				Iterator<IPredicate<? super T>> it = joinFragment.iterator();

				while (it.hasNext()) {
					matchPredicate = true;
					
					// Wenn Aquivalenzvergleich erfuellt wird, dann kann
					// Nachpriorisierung abgebrochen werden
					deactivate = true;

					IPredicate<? super T> current = it.next();
					
					if(current instanceof IRelationalPredicate) {
						if(!((RelationalPredicate) current).isSetOperator(OPERATOR_EQUALS)) {
							matchPredicate = false;
							deactivate = false;
						}
					}
					
					// Wird Datenstromelement spaeter gefiltert, dann braucht
					// nicht nachpriorisiert werden
					if (!current.evaluate(next)) {
						cancelPostPriorisation = true;
						matchPredicate = false;
						deactivate = false;
						break;
					}

				}
			}

			if(currentPriority == null) {
				return;
			}

			next.getMetadata().setPriority(currentPriority);

			if (!cancelPostPriorisation) {
				pipe.handlePostPriorisation(next, deactivate, matchPredicate);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public IPostPriorisationFunctionality<IMetaAttributeContainer<? extends IPriority>> newInstance(
			IPostPriorisationPipe pipe) {
		return new RelationalPostPriorisationFunctionality<IMetaAttributeContainer<? extends IPriority>>(pipe);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setPipe(IPostPriorisationPipe pipe) {
		this.pipe = pipe;
	}

}
