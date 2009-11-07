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

	private List<IPredicate<? super T>> joinFragment = null;

	private boolean deactivate = false;

	public List<IPredicate<? super T>> getJoinFragment() {
		return joinFragment;
	}

	public void setJoinFragment(List<IPredicate<? super T>> fragment) {
		this.joinFragment = fragment;
	}

	private List<ITimeInterval> priorisationIntervals = new ArrayList<ITimeInterval>();

	private IPostPriorisationPipe<T> pipe;

	public RelationalPostPriorisationFunctionality(IPostPriorisationPipe<T> pipe) {
		this.pipe = pipe;
	}
	
	public RelationalPostPriorisationFunctionality() {
		
	}

	public boolean hasToBePrefered(ITimeInterval current) {

		Iterator<ITimeInterval> it = priorisationIntervals.iterator();

		boolean result = false;

		while (it.hasNext()) {
			ITimeInterval each = it.next();
			if (TimeInterval.inside(current, each)) {
				result = true;
			} else if (TimeInterval.startsBefore(each, current)) {
				it.remove();
			}
		}
		// Wenn Gueltigkeitszeitraum verlassen wird, dann kann Nachpriorisierung
		// abgebrochen werden
		if (priorisationIntervals.size() == 0) {
			deactivate = true;
		}

		return result;
	}

	public List<ITimeInterval> getPriorisationIntervals() {
		pipe.setActive(true);
		return priorisationIntervals;
	}

	public void setPriorisationIntervals(
			List<ITimeInterval> priorisationIntervals) {
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
						if(!((RelationalPredicate) current).isSetOperator("==")) {
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
