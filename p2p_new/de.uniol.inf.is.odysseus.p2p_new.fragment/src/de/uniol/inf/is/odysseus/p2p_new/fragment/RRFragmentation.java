package de.uniol.inf.is.odysseus.p2p_new.fragment;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.fragment.logicaloperator.FragmentAO;

/**
 * The class for a round-robin data fragmentation strategy. <br />
 * {@link FragmentAO}s will be used for data fragmentation.
 * @author Michael Brand
 */
public class RRFragmentation extends AbstractPrimaryHorizontalDataFragmentation {
	
	/**
	 * @see #getName()
	 */
	public static final String NAME = "roundrobin";

	@Override
	public String getName() {
		
		return RRFragmentation.NAME;
		
	}

	@Override
	protected ILogicalOperator createOperatorForFragmentation(int numFragments,
			QueryBuildConfiguration parameters) {
		
		// Preconditions
		Preconditions.checkArgument(numFragments > 1);
		
		FragmentAO fragmentAO = new FragmentAO();
		fragmentAO.setType(NAME);
		fragmentAO.setNumberOfFragments(numFragments);
		return fragmentAO;
		
	}

}