package de.uniol.inf.is.odysseus.iql.qdl.types.impl.query;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class DefaultQDLSource<T extends ILogicalOperator> extends AbstractQDLSource<T> {

	public DefaultQDLSource(T operator) {
		super(operator);
	}



}
