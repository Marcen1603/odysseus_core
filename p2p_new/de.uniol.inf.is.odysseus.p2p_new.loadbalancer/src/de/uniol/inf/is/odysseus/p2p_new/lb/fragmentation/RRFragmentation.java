package de.uniol.inf.is.odysseus.p2p_new.lb.fragmentation;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.distribute.logicaloperator.RRFragmentAO;

/**
 * The class for a round-robin data fragmentation strategy. <br />
 * {@link #insertOperatorForDistribution(Collection, int, QueryBuildConfiguration)} will insert {@link RRFragmentAO}s and
 * {@link #createOperatorForJunction() will create an {@link UnionAO}.
 * @author Michael Brand
 */
public class RRFragmentation extends AbstractDataFragmentation {
	
	/**
	 * @see #getName()
	 */
	public static final String NAME = "Round-Robin";

	@Override
	public Class<? extends ILogicalOperator> getOperatorForDistributionClass() {
		
		return RRFragmentAO.class;
		
	}

	@Override
	public ILogicalOperator createOperatorForJunction() {
		
		return new UnionAO();
		
	}

	@Override
	public Class<? extends ILogicalOperator> getOperatorForJunctionClass() {
		
		return UnionAO.class;
		
	}

	@Override
	public String getName() {
		
		return RRFragmentation.NAME;
		
	}

	@Override
	protected ILogicalOperator createOperatorForDistribution(
			int degreeOfParallelism, QueryBuildConfiguration parameters) {
		
		RRFragmentAO fragmentAO = new RRFragmentAO();
		fragmentAO.setNumberOfFragments(degreeOfParallelism);
		return fragmentAO;
		
	}

}