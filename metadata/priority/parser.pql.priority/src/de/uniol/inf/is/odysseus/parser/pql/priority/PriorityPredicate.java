package de.uniol.inf.is.odysseus.parser.pql.priority;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class PriorityPredicate extends
		AbstractPredicate<IMetaAttributeContainer<? extends IPriority>> {

	private static final long serialVersionUID = -8530604545098107300L;

	@Override
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
