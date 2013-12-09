package de.uniol.inf.is.odysseus.peer.distribute;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;

public interface IQueryPartitioner extends INamedInterface {

	public Collection<ILogicalQueryPart> partition( Collection<ILogicalOperator> operators, QueryBuildConfiguration config ) throws QueryPartitionException;
}
