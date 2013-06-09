package de.uniol.inf.is.odysseus.p2p_new.lb;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPart;

/**
 * The <code>InterqueryLoadBalancer</code> distributes different {@link ILogicalQuery}s to peers. So the execution can be done in parallel. <br />
 * The {@link ILogicalQuery}s are assigned to a peer via round robin without the local peer. 
 * But the local peer will get all results and returns them to the {@link IExecutor}. <br />
 * To use the <code>InterqueryLoadBalancer</code> use the following pre-parser-keywords: <br />
 * #DODISTRIBUTE true <br />
 * #DISTRIBUTIONTYPE interquery
 * @author Michael Brand
 */
public class InterqueryLoadBalancer extends AbstractLoadBalancer {

	@Override
	public String getName() {
		
		return "interquery";
		
	}
	
	/**
	 * Returns one {@link Querypart} for the whole {@link ILogicalQuery}.
	 */
	@Override
	protected List<QueryPart> determineQueryParts(List<ILogicalOperator> operators) {
		
		Preconditions.checkNotNull(operators, "operators must be not null!");
		Preconditions.checkArgument(operators.size() > 0, "operators must be not empty!");
		
		return Lists.newArrayList(new QueryPart(operators));
		
	}

	/**
	 * @return <code>1</code>
	 */
	@Override
	protected int getDegreeOfParallelismn(int wantedDegree, int maxDegree) {
		
		return 1;
		
	}

}