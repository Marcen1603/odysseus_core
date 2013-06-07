package de.uniol.inf.is.odysseus.p2p_new.lb;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPart;
import de.uniol.inf.is.odysseus.p2p_new.lb.distributionmerger.logicaloperator.DistributionMergeAO;

/**
 * The <code>IntraqueryLoadBalancer</code> parallelizes each {@link ILogicalQuery} on different peers. <br />
 * The copies of an {@link ILogicalQuery} are assigned to a peer via round robin without the local peer. 
 * But the local peer will get all results and returns them to the {@link IExecutor}. <br />
 * To use the <code>IntraqueryLoadBalancer</code> use the following pre-parser-keywords: <br />
 * #DODISTRIBUTE true <br />
 * #DISTRIBUTIONTYPE intraquery
 * @author Michael Brand
 */
public class IntraqueryLoadBalancer extends AbstractLoadBalancer {

	@Override
	public String getName() {
		
		return "intraquery";
		
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
	
	@Override
	protected QueryPart createLocalPart(List<QueryPart> parts) {
		
		Preconditions.checkNotNull(parts, "parts must be not null!");
		Preconditions.checkArgument(parts.size() > 0, "parts must be not empty!");
		
		final List<ILogicalOperator> operators = Lists.newArrayList();
		
		// All queryparts are equal
		for(@SuppressWarnings("unused") ILogicalOperator sink : parts.get(0).getRealSinks())
			operators.add(new DistributionMergeAO());
		
		return new QueryPart(operators, AbstractLoadBalancer.getLocalDestinationName());
	
	}

	@Override
	protected int getDegreeOfParallelismn(int wantedDegree, int maxDegree) {
		
		Preconditions.checkArgument(wantedDegree > 0, "wantedDegree must be greater than zero!");
		Preconditions.checkArgument(maxDegree > 0, "maxDegree must be greater than zero!");
		
		// XXX First fix the TODO-Mark in AbstractLoadBalancer. M.B.
		// return Math.min(wantedDegree, maxDegree);
		return 2;
		
	}

}