package de.uniol.inf.is.odysseus.p2p_new.lb;

import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
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
	 * Creates one {@link Querypart} for the whole {@link ILogicalQuery}.
	 */
	@Override
	protected List<QueryPart> determineQueryParts(List<ILogicalOperator> operators) {
		
		return Lists.newArrayList(new QueryPart(operators));
		
	}

	/**
	 * @return A {@link RenameAO} with no operation.
	 */
	@Override
	protected QueryPart createLocalPart() {
		
		final RenameAO renameAO = new RenameAO();
		renameAO.setNoOp(true);
		renameAO.addParameterInfo("isNoOp", "'true'");
		return new QueryPart(Lists.newArrayList((ILogicalOperator) renameAO), AbstractLoadBalancer.getLocalDestinationName());
		
	}

	/**
	 * @return <code>1</code>
	 */
	@Override
	protected int getDegreeOfParallelismn(int wantedDegree, int maxDegree) {
		
		return 1;
		
	}

}