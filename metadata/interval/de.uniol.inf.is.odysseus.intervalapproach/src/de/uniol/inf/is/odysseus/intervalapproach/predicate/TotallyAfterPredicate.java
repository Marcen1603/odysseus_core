package de.uniol.inf.is.odysseus.intervalapproach.predicate;

import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

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

	private static final TotallyAfterPredicate predicate = new TotallyAfterPredicate();

	public boolean evaluate(IMetaAttributeContainer<? extends ITimeInterval> input) {
		throw new UnsupportedOperationException();
	}

	public boolean evaluate(IMetaAttributeContainer<? extends ITimeInterval> left,
			IMetaAttributeContainer<? extends ITimeInterval> right) {
		return TimeInterval.totallyAfter(left.getMetadata(), right
				.getMetadata());
	}

	public TotallyAfterPredicate clone() {
		return this;
	}

	public static TotallyAfterPredicate getInstance() {
		return predicate;
	}

	private TotallyAfterPredicate() {

	}

}
