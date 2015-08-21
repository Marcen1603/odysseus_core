package de.uniol.inf.is.odysseus.iql.qdl.typing.builder;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.iql.basic.typing.builder.IIQLTypeBuilder;

public interface IQDLTypeBuilder extends IIQLTypeBuilder{

	void removeSource(ILogicalOperator source);

	void addSource(ILogicalOperator source);

	void addOperator(IOperatorBuilder opBuilder);

}
