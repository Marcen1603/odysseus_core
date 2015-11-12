package de.uniol.inf.is.odysseus.net.querydistribute;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.net.util.INamedInterface;

public interface IDistributionChecker extends INamedInterface {

	public void check( Collection<ILogicalOperator> operators, ILogicalQuery query, QueryBuildConfiguration config) throws DistributionCheckException;
}
