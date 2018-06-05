package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public interface IAccessAO extends ILogicalOperator{

	Resource getAccessAOName();

	boolean isSemanticallyEqual(IAccessAO operator);

}
