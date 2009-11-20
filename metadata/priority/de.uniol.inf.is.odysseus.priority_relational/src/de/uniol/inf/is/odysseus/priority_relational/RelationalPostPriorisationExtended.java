package de.uniol.inf.is.odysseus.priority_relational;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.priority.IPostPriorisationPipe;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class RelationalPostPriorisationExtended<T extends IMetaAttributeContainer<? extends IPriority>>
		extends RelationalPostPriorisationFunctionality<T> {

	public RelationalPostPriorisationExtended(IPostPriorisationPipe<T> pipe) {
		this.pipe = pipe;
	}

	public RelationalPostPriorisationExtended() {
	}

	public void executePostPriorisation(T next) {

		if (next.getMetadata().getPriority() > 0) {
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

					if (current instanceof IRelationalPredicate) {
						if (!((RelationalPredicate) current)
								.isSetOperator(OPERATOR_EQUALS)) {
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

			if (currentPriority == null) {
				return;
			}

			if (joinFragment != null && joinFragment.size() > 0
					&& matchPredicate) {
				next.getMetadata().setPriority(currentPriority);
			} else if (joinFragment == null || joinFragment.size() == 0) {
				next.getMetadata().setPriority(currentPriority);
			} else {
				return;
			}

			if (!cancelPostPriorisation) {
				pipe.handlePostPriorisation(next, deactivate, matchPredicate);
			}
		}

	}

}
