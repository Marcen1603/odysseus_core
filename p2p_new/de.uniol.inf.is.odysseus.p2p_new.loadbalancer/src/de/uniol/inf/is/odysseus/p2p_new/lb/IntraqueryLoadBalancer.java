package de.uniol.inf.is.odysseus.p2p_new.lb;

import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPart;

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
	 * Creates one {@link Querypart} for the whole {@link ILogicalQuery}.
	 */
	@Override
	protected List<QueryPart> determineQueryParts(List<ILogicalOperator> operators) {
		
		return Lists.newArrayList(new QueryPart(operators));
		
	}

	@Override
	protected QueryPart createLocalPart() {
	
		// XXX SweepArea instead of union. M.B.
		final UnionAO unionAO = new UnionAO();
		return new QueryPart(Lists.newArrayList((ILogicalOperator) unionAO), AbstractLoadBalancer.getLocalDestinationName());
	
	}

	@Override
	protected int getDegreeOfParallelismn(int wantedDegree, int maxDegree) {
		
		// XXX First fix the other wworkaround in this class. M.B.
		// return Math.min(wantedDegree, maxDegree);
		return 2;
		
	}

}