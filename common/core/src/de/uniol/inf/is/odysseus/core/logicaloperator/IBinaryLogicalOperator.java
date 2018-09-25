package de.uniol.inf.is.odysseus.core.logicaloperator;

public interface IBinaryLogicalOperator extends ILogicalOperator {

	ILogicalOperator getLeftInput();

	ILogicalOperator getRightInput();

}
