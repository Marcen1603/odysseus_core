package de.uniol.inf.is.odysseus.peer.distribute.partition.operatorcloud;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
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
	public Collection<ILogicalQueryPart> partition(Collection<ILogicalOperator> operators, ILogicalQuery query, QueryBuildConfiguration config, List<String> partitionParameters) throws QueryPartitionException {
		Collection<ILogicalQueryPart> parts = Lists.newArrayList();
		
		// Handling of RenameAOs: Every RenameAO will be in the query part of the operator subscribed by the RenameAO.
		
		List<ILogicalOperator> processedOperators = Lists.newArrayList();
		
		for(ILogicalOperator operator : operators) {
			
			if(processedOperators.contains(operator))
				continue;
			
			List<ILogicalOperator> operatorsForPart = Lists.newArrayList(operator);
			
			for(LogicalSubscription subToSink : operator.getSubscriptions()) {
				
				if(subToSink.getTarget() instanceof RenameAO)
					operatorsForPart.add(subToSink.getTarget());
				
			}
			
			parts.add(new LogicalQueryPart(operatorsForPart));
		}
		
		return parts;
	}

}
