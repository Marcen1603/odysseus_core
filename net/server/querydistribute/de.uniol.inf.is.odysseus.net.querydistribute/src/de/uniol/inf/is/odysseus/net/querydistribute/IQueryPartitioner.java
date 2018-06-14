package de.uniol.inf.is.odysseus.net.querydistribute;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.net.util.INamedInterface;

public interface IQueryPartitioner extends INamedInterface {

	public Collection<ILogicalQueryPart> partition( Collection<ILogicalOperator> operators, ILogicalQuery query, QueryBuildConfiguration config, List<String> partitionerParameters) throws QueryPartitionException;
}
