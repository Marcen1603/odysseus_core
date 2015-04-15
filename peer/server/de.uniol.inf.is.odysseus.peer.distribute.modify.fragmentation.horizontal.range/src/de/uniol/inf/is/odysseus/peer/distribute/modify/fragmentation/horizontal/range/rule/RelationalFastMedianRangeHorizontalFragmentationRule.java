package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.rule;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.RangeHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.RelationalFastMedianHorizontalFragmentationRule;

/**
 * A relational fast median can not be part of a fragment for range horizontal
 * fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class RelationalFastMedianRangeHorizontalFragmentationRule
		extends
		RelationalFastMedianHorizontalFragmentationRule<RangeHorizontalFragmentationQueryPartModificator> {

	@Override
	public Class<RangeHorizontalFragmentationQueryPartModificator> getStrategyClass() {

		return RangeHorizontalFragmentationQueryPartModificator.class;

	}

}