package de.uniol.inf.is.odysseus.dsp;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class ConstPartialAggregate<T> extends AbstractPartialAggregate<T> {

	private static final long serialVersionUID = 6847721673109069345L;

	@Override
	public IPartialAggregate<T> clone() {
		return new ConstPartialAggregate<>();
	}

}
