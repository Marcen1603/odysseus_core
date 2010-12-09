package de.uniol.inf.is.odysseus.intervalapproach.predicate;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.predicate.EqualsPredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

/**
 * Singleton because no object state is needed.
 * 
 * This will also be needed parallel to TotallyBeforePredicate, because it is
 * impossible to switch between Order.LeftRight and Order.RightLeft in complex
 * predicates.
 * 
 * @author Andre Bolles
 */
public class TotallyAfterPredicate extends AbstractPredicate<IMetaAttributeContainer<? extends ITimeInterval>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1432746295344925590L;
	private static final TotallyAfterPredicate predicate = new TotallyAfterPredicate();

	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends ITimeInterval> input) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends ITimeInterval> left,
			IMetaAttributeContainer<? extends ITimeInterval> right) {
		return TimeInterval.totallyAfter(left.getMetadata(), right
				.getMetadata());
	}

	@Override
	public TotallyAfterPredicate clone() {
		return this;
	}

	public static TotallyAfterPredicate getInstance() {
		return predicate;
	}

	private TotallyAfterPredicate() {

	}
	
	@Override
	public boolean equals(IPredicate pred) {
		return (pred instanceof TotallyAfterPredicate);
	}
	
	@Override
	public boolean isContainedIn(Object o) {
		if(!(o instanceof TotallyAfterPredicate)) {
			return false;
		}
		return true;
	}

}
