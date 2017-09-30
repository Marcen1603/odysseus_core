package de.uniol.inf.is.odysseus.net.querydistribute.partition.operatorcloud;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.net.querydistribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartitionException;

public class OperatorCloudPartitioner implements IQueryPartitioner {

	private static final long serialVersionUID = -1308477904592508422L;
	
	private static final String PARTITIONER_NAME = "OPERATORCLOUD";

	@Override
	public Collection<ILogicalQueryPart> partition(Collection<ILogicalOperator> operators, ILogicalQuery query, QueryBuildConfiguration config, List<String> partitionerParameters) throws QueryPartitionException {
		Collection<ILogicalQueryPart> parts = Lists.newArrayList();

		for (ILogicalOperator operator : operators)
			parts.add(new LogicalQueryPart(operator));

		// Handling of RenameAOs: Every RenameAO will be in the query part of
		// the operator subscribed by the RenameAO.

		for (ILogicalQueryPart part : parts.toArray(new ILogicalQueryPart[0])) {

			// There is only one operator within the part
			if (!(part.getOperators().iterator().next() instanceof RenameAO))
				continue;

			final ILogicalOperator renameAO = part.getOperators().iterator().next();
			final ILogicalOperator source = renameAO.getSubscribedToSource().iterator().next().getSource();

			Optional<ILogicalQueryPart> partOfSource = determineQueryPart(source, parts);
			if (!partOfSource.isPresent())
				throw new QueryPartitionException("Part of " + source + " is not present!");

			partOfSource.get().addOperator(renameAO);
			parts.remove(part);

		}

		return parts;
	}

	@Override
	public String getName() {
		return PARTITIONER_NAME;
	}

	private static Optional<ILogicalQueryPart> determineQueryPart(ILogicalOperator operator, Collection<ILogicalQueryPart> queryParts) {
		for (ILogicalQueryPart part : queryParts) {
			if (part.getOperators().contains(operator)) {
				return Optional.of(part);
			}
		}
		return Optional.absent();
	}
}
