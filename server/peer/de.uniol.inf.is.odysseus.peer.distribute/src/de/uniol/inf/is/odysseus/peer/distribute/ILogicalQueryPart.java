package de.uniol.inf.is.odysseus.peer.distribute;

import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public interface ILogicalQueryPart {

	public ImmutableCollection<ILogicalOperator> getOperators();
}
