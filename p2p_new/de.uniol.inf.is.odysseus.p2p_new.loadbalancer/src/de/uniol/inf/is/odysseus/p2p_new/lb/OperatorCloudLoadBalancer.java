package de.uniol.inf.is.odysseus.p2p_new.lb;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.distribute.DistributionHelper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPart;

/**
 * The <code>OperatorCloudLoadBalancer</code> splits an {@link ILogicalQuery} after each {@link ILogicalOperator} 
 * and parallelizes each {@link ILogicalOperator} on different peers. <br />
 * The copies of an {@link ILogicalOperator} are assigned to a peer via round robin without the local peer. 
 * But the local peer will get all results and returns them to the {@link IExecutor}. <br />
 * To use the <code>OperatorCloudLoadBalancer</code> use the following pre-parser-keywords: <br />
 * #DODISTRIBUTE true <br />
 * #DISTRIBUTIONTYPE operatorcloud <code>degree of parallelism</code> <br />
 * where <code>degree of parallelism</code> has to be an integer greater than zero, "min" for <code>1</code> or 
 * "max" for {@link Integer#MAX_VALUE}.
 * @author Michael Brand
 */
public class OperatorCloudLoadBalancer extends AbstractLoadBalancer {

	@Override
	public String getName() {
		
		return "operatorcloud";
		
	}

	/**
	 * Returns one {@link Querypart} for each {@link ILogicalOperator} within the {@link ILogicalQuery}.
	 */
	@Override
	protected List<QueryPart> determineQueryParts(List<ILogicalOperator> operators, QueryPart localPart) {
		
		Preconditions.checkNotNull(operators, "operators must be not null!");
		Preconditions.checkArgument(operators.size() > 0, "operators must be not empty!");
		
		List<QueryPart> parts = Lists.newArrayList();
		for(ILogicalOperator operator : operators) {
			
			parts.add(DistributionHelper.replaceStreamAOs(new QueryPart(Lists.newArrayList(operator))));
			
		}
		
		return parts;
		
	}

}