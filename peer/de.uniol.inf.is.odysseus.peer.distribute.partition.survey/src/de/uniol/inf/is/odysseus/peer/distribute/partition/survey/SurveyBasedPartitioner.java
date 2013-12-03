package de.uniol.inf.is.odysseus.peer.distribute.partition.survey;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartitionException;

public class SurveyBasedPartitioner implements IQueryPartitioner {

	@Override
	public String getName() {
		return "survey";
	}

	@Override
	public Collection<ILogicalQueryPart> partition(Collection<ILogicalOperator> operators, QueryBuildConfiguration config) throws QueryPartitionException {
		return null;
	}

}
