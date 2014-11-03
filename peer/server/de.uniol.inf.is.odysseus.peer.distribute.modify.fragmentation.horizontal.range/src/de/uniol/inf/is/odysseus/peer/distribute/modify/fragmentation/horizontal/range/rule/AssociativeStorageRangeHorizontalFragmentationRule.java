package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.rule;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AssociativeStorageAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.RangeHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.rule.IFragmentationRule;

/**
 * An associative storage can not be part of a fragment for range horizontal
 * fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class AssociativeStorageRangeHorizontalFragmentationRule
		implements
		IFragmentationRule<RangeHorizontalFragmentationQueryPartModificator, AssociativeStorageAO> {

	@Override
	public boolean canOperatorBePartOfFragments(
			RangeHorizontalFragmentationQueryPartModificator strategy,
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
	public Class<RangeHorizontalFragmentationQueryPartModificator> getStrategyClass() {

		return RangeHorizontalFragmentationQueryPartModificator.class;

	}

	@Override
	public Class<AssociativeStorageAO> getOperatorClass() {

		return AssociativeStorageAO.class;

	}

}