package de.uniol.inf.is.odysseus.peer.distribute.util;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public interface IOperatorGenerator {

	public void beginDisconnect(ILogicalOperator sourceOperator, ILogicalOperator sinkOperator);

	public ILogicalOperator createSinkOfSource( ILogicalOperator source);
	public ILogicalOperator createSourceofSink( ILogicalOperator sink );
	
	public void endDisconnect();
}
