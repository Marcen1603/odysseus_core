package de.uniol.inf.is.odysseus.peer.distribute;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

public interface IDistributionChecker {

	public void check( Collection<ILogicalOperator> operators, QueryBuildConfiguration config) throws DistributionCheckException;
}
