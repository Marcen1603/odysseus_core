package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.impl;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.newimpl.RangeHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.AbstractFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.IFragmentationRule;
import de.uniol.inf.is.odysseus.relational_interval.logicaloperator.RelationalFastMedianAO;

/**
 * A relational fast median can not be part of a fragment for range horizontal
 * fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class RelationalFastMedianRangeHorizontalFragmentationRule
		implements
		IFragmentationRule<RangeHorizontalFragmentationQueryPartModificator, RelationalFastMedianAO> {

	@Override
	public boolean canOperatorBePartOfFragments(
			RangeHorizontalFragmentationQueryPartModificator strategy,
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
	public Class<RangeHorizontalFragmentationQueryPartModificator> getStrategyClass() {
		
		return RangeHorizontalFragmentationQueryPartModificator.class;
		
	}

	@Override
	public Class<RelationalFastMedianAO> getOperatorClass() {
		
		return RelationalFastMedianAO.class;
		
	}

}