package de.uniol.inf.is.odysseus.net.querydistribute.partition.query;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.net.querydistribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartitionException;

public class QueryCloudPartitioner implements IQueryPartitioner {

	private static final long serialVersionUID = -7272387339519176651L;

	@Override
	public Collection<ILogicalQueryPart> partition(Collection<ILogicalOperator> operators, ILogicalQuery query, QueryBuildConfiguration config, List<String> partitionerParameters) throws QueryPartitionException {
		ILogicalQueryPart part = new LogicalQueryPart(operators);
		return Lists.newArrayList(part);
	}

	@Override
	public String getName() {
		return "querycloud";
	}

}
