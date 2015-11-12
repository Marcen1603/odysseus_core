package de.uniol.inf.is.odysseus.net.querydistribute.util;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;

public interface IOperatorGenerator {

	public void beginDisconnect(ILogicalQueryPart sourceQueryPart, ILogicalOperator sourceOperator, ILogicalQueryPart sinkQueryPart, ILogicalOperator sinkOperator);

	public ILogicalOperator createSinkOfSource( ILogicalQueryPart sourceQueryPart, ILogicalOperator source);
	public ILogicalOperator createSourceofSink( ILogicalQueryPart sinkQueryPart, ILogicalOperator sink );
	
	public void endDisconnect();
}
