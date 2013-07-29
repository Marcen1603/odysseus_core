package de.uniol.inf.is.odysseus.p2p_new.lb.fragmentation;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.distribution.IDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.lb.logicaloperator.ReplicationMergeAO;

/**
 * The class for data replication which is therefore an exception of data fragmentation strategies. <br />
 * {@link #insertOperatorForDistribution(Collection, int, QueryBuildConfiguration)} will not insert any operators.
 * @author Michael Brand
 */
public class Replication implements IDataFragmentation {

	/**
	 * @see #getName()
	 */
	public static final String NAME = "replication";
	
	@Override
	public String getName() {
		
		return Replication.NAME;
		
	}
	
	@Override
	public Collection<ILogicalOperator> insertOperatorForDistribution(
			Collection<ILogicalOperator> operators, String sourceName, 
			int degreeOfParallelism, QueryBuildConfiguration parameters) {
		
		return operators;
		
	}

	@Override
	public Class<? extends ILogicalOperator> getOperatorForDistributionClass() {
		
		return null;
		
	}

	@Override
	public ILogicalOperator createOperatorForJunction() {
		
		return new ReplicationMergeAO();
		
	}

	@Override
	public Class<? extends ILogicalOperator> getOperatorForJunctionClass() {
		
		return ReplicationMergeAO.class;
		
	}

}