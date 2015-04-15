package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.roundrobin.rule;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AssociativeStorageAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.roundrobin.RoundRobinFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.rule.IFragmentationRule;

/**
 * An associative storage can not be part of a fragment for round robin
 * horizontal fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class AssociativeStorageRoundRobinFragmentationRule
		implements
		IFragmentationRule<RoundRobinFragmentationQueryPartModificator, AssociativeStorageAO> {

	@Override
	public boolean canOperatorBePartOfFragments(
			RoundRobinFragmentationQueryPartModificator strategy,
			AssociativeStorageAO operator,
			AbstractFragmentationParameterHelper helper) {

		return false;

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			AssociativeStorageAO operator,
			AbstractFragmentationParameterHelper helper) {

		return false;

	}

	@Override
	public AssociativeStorageAO specialHandling(ILogicalQueryPart part,
			AbstractFragmentationParameterHelper helper,
			FragmentationInfoBundle bundle)
			throws QueryPartModificationException {

		// Nothing to do
		return null;

	}

	@Override
	public Class<RoundRobinFragmentationQueryPartModificator> getStrategyClass() {

		return RoundRobinFragmentationQueryPartModificator.class;

	}

	@Override
	public Class<AssociativeStorageAO> getOperatorClass() {

		return AssociativeStorageAO.class;

	}

}