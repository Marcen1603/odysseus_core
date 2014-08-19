package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.impl;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.newimpl.RoundRobinHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.AbstractFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.IFragmentationRule;

/**
 * A state map can not be part of a fragment for round robin horizontal
 * fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class StateMapRoundRobinHorizontalFragmentationRule
		implements
		IFragmentationRule<RoundRobinHorizontalFragmentationQueryPartModificator, StateMapAO> {

	@Override
	public boolean canOperatorBePartOfFragments(
			RoundRobinHorizontalFragmentationQueryPartModificator strategy,
			StateMapAO operator) {

		return false;

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			StateMapAO operator, AbstractFragmentationHelper helper) {

		return false;

	}

	@Override
	public StateMapAO specialHandling(ILogicalQueryPart part,
			AbstractFragmentationHelper helper, FragmentationInfoBundle bundle)
			throws QueryPartModificationException {

		// Nothing to do
		return null;

	}
	
	@Override
	public Class<RoundRobinHorizontalFragmentationQueryPartModificator> getStrategyClass() {
		
		return RoundRobinHorizontalFragmentationQueryPartModificator.class;
		
	}

	@Override
	public Class<StateMapAO> getOperatorClass() {
		
		return StateMapAO.class;
		
	}

}