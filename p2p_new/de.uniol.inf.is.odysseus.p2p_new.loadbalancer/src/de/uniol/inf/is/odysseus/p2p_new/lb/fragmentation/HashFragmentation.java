package de.uniol.inf.is.odysseus.p2p_new.lb.fragmentation;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.distribute.logicaloperator.FragmentAO;

/**
 * The class for a hash data fragmentation strategy. <br />
 * {@link #insertOperatorForDistribution(Collection, int, QueryBuildConfiguration)} will insert {@link FragmentAO}s and
 * {@link #createOperatorForJunction() will create an {@link UnionAO}.
 * @author Michael Brand
 */
public class HashFragmentation extends AbstractDataFragmentation {
	
	/**
	 * @see #getName()
	 */
	public static final String NAME = "hash";

	@Override
	public Class<? extends ILogicalOperator> getOperatorForDistributionClass() {
		
		return FragmentAO.class;
		
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
		
		return HashFragmentation.NAME;
		
	}

	@Override
	protected ILogicalOperator createOperatorForDistribution(
			int degreeOfParallelism, QueryBuildConfiguration parameters) {
		
		FragmentAO fragmentAO = new FragmentAO();
		fragmentAO.setType(HashFragmentation.NAME);
		fragmentAO.setNumberOfFragments(degreeOfParallelism);
		return fragmentAO;
		
	}

}