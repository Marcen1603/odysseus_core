package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.rule;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.newimpl.RangeHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.AbstractRelationalFastMedianHorizontalFragmentationRule;

/**
 * A relational fast median can not be part of a fragment for range horizontal
 * fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class RelationalFastMedianRangeHorizontalFragmentationRule
		extends
		AbstractRelationalFastMedianHorizontalFragmentationRule<RangeHorizontalFragmentationQueryPartModificator> {

	@Override
	public Class<RangeHorizontalFragmentationQueryPartModificator> getStrategyClass() {

		return RangeHorizontalFragmentationQueryPartModificator.class;

	}

}