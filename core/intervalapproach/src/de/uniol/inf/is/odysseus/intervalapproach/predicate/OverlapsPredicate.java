package de.uniol.inf.is.odysseus.intervalapproach.predicate;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

/**
 * Singleton because no object state is needed
 * 
 * @author Jonas Jacobi
 */
public class OverlapsPredicate extends
		AbstractPredicate<IMetaAttributeContainer<? extends ITimeInterval>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7697071354543915488L;
	private static final OverlapsPredicate predicate = new OverlapsPredicate();

	public static OverlapsPredicate getInstance() {
		return predicate;
	}

	public boolean evaluate(IMetaAttributeContainer<? extends ITimeInterval> input) {
		throw new UnsupportedOperationException();
	}

	public boolean evaluate(IMetaAttributeContainer<? extends ITimeInterval> left,
			IMetaAttributeContainer<? extends ITimeInterval> right) {
		return TimeInterval.overlaps(left.getMetadata(), right.getMetadata());
	}

	@Override
	public OverlapsPredicate clone() {
		return this;
	}

	private OverlapsPredicate() {
	}

	public boolean equals(IPredicate pred) {
		if(!(pred instanceof OverlapsPredicate)) {
			return false;
		}
		return true;
	}
}
