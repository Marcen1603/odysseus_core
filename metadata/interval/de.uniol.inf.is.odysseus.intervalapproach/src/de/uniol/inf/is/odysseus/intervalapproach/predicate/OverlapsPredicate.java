package de.uniol.inf.is.odysseus.intervalapproach.predicate;

import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;

/**
 * Singleton because no object state is needed
 * 
 * @author Jonas Jacobi
 */
public class OverlapsPredicate extends
		AbstractPredicate<IMetaAttribute<? extends ITimeInterval>> {

	private static final OverlapsPredicate predicate = new OverlapsPredicate();

	public static OverlapsPredicate getInstance() {
		return predicate;
	}

	public boolean evaluate(IMetaAttribute<? extends ITimeInterval> input) {
		throw new UnsupportedOperationException();
	}

	public boolean evaluate(IMetaAttribute<? extends ITimeInterval> left,
			IMetaAttribute<? extends ITimeInterval> right) {
		return TimeInterval.overlaps(left.getMetadata(), right.getMetadata());
	}

	@Override
	public OverlapsPredicate clone() {
		return this;
	}

	private OverlapsPredicate() {
	}

}
