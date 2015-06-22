package de.uniol.inf.is.odysseus.multithreaded.interoperator.helper;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.util.GenericDownstreamGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.GenericUpstreamGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.OperatorIdLogicalGraphVisitor;

public class LogicalGraphHelper {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ILogicalOperator findDownstreamOperatorWithId(
			String endParallelizationId, ILogicalOperator logicalOperator) {
		OperatorIdLogicalGraphVisitor<ILogicalOperator> idVisitor = new OperatorIdLogicalGraphVisitor<ILogicalOperator>(
				endParallelizationId);
		GenericDownstreamGraphWalker walker = new GenericDownstreamGraphWalker();
		walker.prefixWalk(logicalOperator, idVisitor);
		return idVisitor.getResult().get(endParallelizationId);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ILogicalOperator findUpstreamOperatorWithId(
			String endParallelizationId, ILogicalOperator logicalOperator) {
		OperatorIdLogicalGraphVisitor<ILogicalOperator> idVisitor = new OperatorIdLogicalGraphVisitor<ILogicalOperator>(
				endParallelizationId);
		GenericUpstreamGraphWalker walker = new GenericUpstreamGraphWalker();
		walker.prefixWalk(logicalOperator, idVisitor);
		return idVisitor.getResult().get(endParallelizationId);
	}
}
