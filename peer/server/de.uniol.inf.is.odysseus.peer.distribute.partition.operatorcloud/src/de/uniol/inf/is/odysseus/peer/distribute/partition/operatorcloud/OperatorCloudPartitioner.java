package de.uniol.inf.is.odysseus.peer.distribute.partition.operatorcloud;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartitionException;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

public class OperatorCloudPartitioner implements IQueryPartitioner {

	@Override
	public String getName() {
		return "operatorcloud";
	}

	@Override
	public Collection<ILogicalQueryPart> partition(Collection<ILogicalOperator> operators, ILogicalQuery query, QueryBuildConfiguration config, List<String> partitionParameters) throws QueryPartitionException {
		Collection<ILogicalQueryPart> parts = Lists.newArrayList();
		
		for(ILogicalOperator operator : operators)
			parts.add(new LogicalQueryPart(operator));
		
		// Handling of RenameAOs: Every RenameAO will be in the query part of the operator subscribed by the RenameAO.
		
		for(ILogicalQueryPart part : parts.toArray(new ILogicalQueryPart[0])) {
			
			// There is only one operator within the part
			if(!(part.getOperators().iterator().next() instanceof RenameAO))
				continue;
			
			final ILogicalOperator renameAO = part.getOperators().iterator().next();
			final ILogicalOperator source = renameAO.getSubscribedToSource().iterator().next().getTarget();
			
			Optional<ILogicalQueryPart> partOfSource = LogicalQueryHelper.determineQueryPart(parts, source);
			if(!partOfSource.isPresent())
				throw new QueryPartitionException("Part of " + source + " is not present!");
			
			partOfSource.get().addOperator(renameAO);
			parts.remove(part);
			
		}
		
		return parts;
	}

}
