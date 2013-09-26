package de.uniol.inf.is.odysseus.p2p_new.lb.fragmentation;

import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.lb.logicaloperator.ReplicationMergeAO;

/**
 * The class for data replication which is therefore an exception of data fragmentation strategies, 
 * because replication of source accesses remain and no fragmentation operator will be inserted.
 * @author Michael Brand
 */
public class Replication extends AbstractPrimaryHorizontalDataFragmentation {

	/**
	 * @see #getName()
	 */
	public static final String NAME = "replication";
	
	@Override
	public String getName() {
		
		return Replication.NAME;
		
	}
	
	/**
	 * Does nothing.
	 */
	@Override
	protected List<List<ILogicalOperator>> insertOperatorForFragmentation(List<ILogicalOperator> logicalPlans, QueryBuildConfiguration parameters, String sourceName) {
		
		// The return value
		List<List<ILogicalOperator>> operatorsPerLogicalPlan = Lists.newArrayList();
		
		for(ILogicalOperator logicalPlan : logicalPlans) {
		
			List<ILogicalOperator> operators = Lists.newArrayList();
			RestructHelper.collectOperators(logicalPlan, operators);
			RestructHelper.removeTopAOs(operators);
			operatorsPerLogicalPlan.add(operators);
			
		}
		
		return operatorsPerLogicalPlan;
		
		
	}

	/**
	 * @return null.
	 */
	@Override
	protected ILogicalOperator createOperatorForFragmentation(int numFragments,
			QueryBuildConfiguration parameters) {
		
		return null;
		
	}
	
	/**
	 * Creates a new {@link ReplicationMergeAO}.
	 */
	protected ILogicalOperator createOperatorForDataReunion() {
		
		return new ReplicationMergeAO();
		
	}

}