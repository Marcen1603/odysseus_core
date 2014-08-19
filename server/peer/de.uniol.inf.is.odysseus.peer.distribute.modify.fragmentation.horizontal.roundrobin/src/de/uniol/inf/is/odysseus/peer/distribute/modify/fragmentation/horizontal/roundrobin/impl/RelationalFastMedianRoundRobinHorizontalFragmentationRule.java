package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.impl;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.newimpl.RoundRobinHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.AbstractFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.IFragmentationRule;
import de.uniol.inf.is.odysseus.relational_interval.logicaloperator.RelationalFastMedianAO;

/**
 * A relational fast median can not be part of a fragment for round robin
 * horizontal fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class RelationalFastMedianRoundRobinHorizontalFragmentationRule
		implements
		IFragmentationRule<RoundRobinHorizontalFragmentationQueryPartModificator, RelationalFastMedianAO> {

	@Override
	public boolean canOperatorBePartOfFragments(
			RoundRobinHorizontalFragmentationQueryPartModificator strategy,
			RelationalFastMedianAO operator) {

		return false;

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			RelationalFastMedianAO operator, AbstractFragmentationHelper helper) {

		return false;

	}

	@Override
	public RelationalFastMedianAO specialHandling(ILogicalQueryPart part,
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
	public Class<RelationalFastMedianAO> getOperatorClass() {
		
		return RelationalFastMedianAO.class;
		
	}

}