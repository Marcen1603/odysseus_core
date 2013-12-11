package de.uniol.inf.is.odysseus.peer.distribute.partition.operatorcloud;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartitionException;

public class OperatorCloudPartitioner implements IQueryPartitioner {

	@Override
	public String getName() {
		return "operatorcloud";
	}

	@Override
	public Collection<ILogicalQueryPart> partition(Collection<ILogicalOperator> operators, QueryBuildConfiguration config, List<String> partitionParameters) throws QueryPartitionException {
		Collection<ILogicalQueryPart> parts = Lists.newArrayList();
		
		for( ILogicalOperator operator : operators ) {
			parts.add( new LogicalQueryPart(operator));
		}
		
		return parts;
	}

}
