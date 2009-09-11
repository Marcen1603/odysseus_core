package de.uniol.inf.is.odysseus.intervalapproach.predicate;

import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

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

	private static final TotallyBeforePredicate predicate = new TotallyBeforePredicate();

	public boolean evaluate(IMetaAttributeContainer<? extends ITimeInterval> input) {
		throw new UnsupportedOperationException();
	}

	public boolean evaluate(IMetaAttributeContainer<? extends ITimeInterval> left,
			IMetaAttributeContainer<? extends ITimeInterval> right) {
		return TimeInterval.totallyBefore(left.getMetadata(), right
				.getMetadata());
	}

	public TotallyBeforePredicate clone() {
		return this;
	}

	public static TotallyBeforePredicate getInstance() {
		return predicate;
	}

	private TotallyBeforePredicate() {

	}

}
