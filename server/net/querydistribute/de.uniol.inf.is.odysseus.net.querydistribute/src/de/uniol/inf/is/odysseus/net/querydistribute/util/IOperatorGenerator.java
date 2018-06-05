package de.uniol.inf.is.odysseus.net.querydistribute.util;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartTransmissionException;

public interface IOperatorGenerator {

	public void beginDisconnect(ILogicalQueryPart sourceQueryPart, ILogicalOperator sourceOperator, IOdysseusNode sourceNode, ILogicalQueryPart sinkQueryPart, ILogicalOperator sinkOperator, IOdysseusNode sinkNode) throws QueryPartTransmissionException;

	public ILogicalOperator createSinkOfSource( ILogicalQueryPart sourceQueryPart, ILogicalOperator source, IOdysseusNode sourceNode);
	public ILogicalOperator createSourceofSink( ILogicalQueryPart sinkQueryPart, ILogicalOperator sink, IOdysseusNode sinkNode );
	
	public void endDisconnect();
}
