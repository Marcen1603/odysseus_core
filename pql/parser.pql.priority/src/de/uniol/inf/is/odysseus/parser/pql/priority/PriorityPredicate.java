package de.uniol.inf.is.odysseus.parser.pql.priority;

import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class PriorityPredicate extends
		AbstractPredicate<IMetaAttributeContainer<? extends IPriority>> {

	private static final long serialVersionUID = -8530604545098107300L;

	public PriorityPredicate clone() {
		return this;
	}

	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends IPriority> input) {
		return input.getMetadata().getPriority() > 0;
	}
	

	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends IPriority> left,
			IMetaAttributeContainer<? extends IPriority> right) {
		throw new UnsupportedOperationException();
	}

}
