package de.uniol.inf.is.odysseus.p2p_new.lb;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.distribute.DistributionHelper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPart;

/**
 * The <code>OperatorSetCloudLoadBalancer</code> splits an {@link ILogicalQuery} before each {@link IStatefulOperator} 
 * and parallelizes each {@link QueryPart} on different peers. <br />
 * The copies of an {@link QueryPart} are assigned to a peer via round robin without the local peer. 
 * But the local peer will get all results and returns them to the {@link IExecutor}. <br />
 * To use the <code>OperatorSetCloudLoadBalancer</code> use the following pre-parser-keywords: <br />
 * #DODISTRIBUTE true <br />
 * #DISTRIBUTIONTYPE operatorsetcloud <code>degree of parallelism</code> <br />
 * where <code>degree of parallelism</code> has to be an integer greater than zero, "min" for <code>1</code> or 
 * "max" for {@link Integer#MAX_VALUE}.
 * @author Michael Brand
 */
public class OperatorSetCloudLoadBalancer extends AbstractLoadBalancer {

	@Override
	public String getName() {
		
		return "operatorsetcloud";
		
	}

	/**
	 * Returns one {@link Querypart} for each {@link IStatefulOperator} within the {@link ILogicalQuery} plus one {@link QueryPart} with 
	 * all operators before the first {@link QueryPart}.
	 */
	@Override
	protected List<QueryPart> determineQueryParts(List<ILogicalOperator> operators) {
		
		Preconditions.checkNotNull(operators, "operators must be not null!");
		Preconditions.checkArgument(operators.size() > 0, "operators must be not empty!");
		
		List<QueryPart> parts = Lists.newArrayList();
		List<ILogicalOperator> opsForCurrentPart = Lists.newArrayList();
		for(int opIndex = operators.size() - 1; opIndex >= 0; opIndex--) {
			// list of operators starts beneath TopAOs, not with StreamAOs
			
			if(operators.get(opIndex) instanceof IStatefulOperator) {
				
				parts.add(DistributionHelper.replaceStreamAOs(new QueryPart(opsForCurrentPart)));
				opsForCurrentPart.clear();
				opsForCurrentPart.add(operators.get(opIndex));
				
			} else opsForCurrentPart.add(operators.get(opIndex));
			
		}
		
		parts.add(DistributionHelper.replaceStreamAOs(new QueryPart(opsForCurrentPart)));
		
		return parts;
		
	}

}