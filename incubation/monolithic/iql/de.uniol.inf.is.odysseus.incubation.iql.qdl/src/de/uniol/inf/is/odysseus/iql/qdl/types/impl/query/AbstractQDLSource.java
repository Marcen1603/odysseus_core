package de.uniol.inf.is.odysseus.iql.qdl.types.impl.query;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.iql.qdl.types.source.IQDLSource;

public abstract class AbstractQDLSource extends AbstractQDLOperator implements IQDLSource {

	public AbstractQDLSource(ILogicalOperator operator) {
		super(operator);
	}

}
