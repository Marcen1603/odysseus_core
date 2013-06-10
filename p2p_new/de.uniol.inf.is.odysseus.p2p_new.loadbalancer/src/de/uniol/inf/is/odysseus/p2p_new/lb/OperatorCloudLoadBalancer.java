package de.uniol.inf.is.odysseus.p2p_new.lb;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPart;

// TODO javaDoc M.B.
public class OperatorCloudLoadBalancer extends AbstractLoadBalancer {

	@Override
	public String getName() {
		
		return "operatorcloud";
		
	}

	/**
	 * Returns one {@link Querypart} for each {@link ILogicalOperator} within the {@link ILogicalQuery}.
	 */
	@Override
	protected List<QueryPart> determineQueryParts(List<ILogicalOperator> operators) {
		
		Preconditions.checkNotNull(operators, "operators must be not null!");
		Preconditions.checkArgument(operators.size() > 0, "operators must be not empty!");
		
		List<QueryPart> parts = Lists.newArrayList();
		for(int index = 1; index < operators.size(); index++) {
			
			ILogicalOperator nextOp = operators.get(index);
			ILogicalOperator operator = operators.get(index - 1);
			
			if(operator instanceof StreamAO)
				continue;
			
			List<ILogicalOperator> opsForPart = Lists.newArrayList(operator);
			
			if(nextOp instanceof StreamAO)
				opsForPart.add(nextOp);
			
			parts.add(new QueryPart(opsForPart));
			
		}
		
		return parts;
		
	}

}