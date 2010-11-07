package de.uniol.inf.is.odysseus.intervalapproach.predicate;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.predicate.EqualsPredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

/**
 * Singleton because no object state is needed.
 * 
 * This will also be needed parallel to TotallyAfterPredicate, because it is
 * impossible to switch between Order.LeftRight and Order.RightLeft in complex
 * predicates.
 * 
 * @author Jonas Jacobi
 */
public class TotallyBeforePredicate extends AbstractPredicate<IMetaAttributeContainer<? extends ITimeInterval>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3010416001774544889L;
	private static final TotallyBeforePredicate predicate = new TotallyBeforePredicate();

	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends ITimeInterval> input) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends ITimeInterval> left,
			IMetaAttributeContainer<? extends ITimeInterval> right) {
		return TimeInterval.totallyBefore(left.getMetadata(), right
				.getMetadata());
	}

	@Override
	public TotallyBeforePredicate clone() {
		return this;
	}

	public static TotallyBeforePredicate getInstance() {
		return predicate;
	}

	private TotallyBeforePredicate() {

	}
	
	@Override
	public boolean equals(IPredicate pred) {
		return (pred instanceof TotallyBeforePredicate);
	}
	
	@Override
	public boolean isContainedIn(Object o) {
		if(!(o instanceof TotallyBeforePredicate)) {
			return false;
		}
		return true;
	}

}
