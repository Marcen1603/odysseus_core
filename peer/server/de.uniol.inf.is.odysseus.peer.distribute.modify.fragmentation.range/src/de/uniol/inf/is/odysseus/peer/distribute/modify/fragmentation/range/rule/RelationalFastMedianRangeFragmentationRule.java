package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.range.rule;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.RelationalFastMedianHorizontalFragmentationRule;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.range.RangeFragmentationQueryPartModificator;

/**
 * A relational fast median can not be part of a fragment for range horizontal
 * fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class RelationalFastMedianRangeFragmentationRule
		extends
		RelationalFastMedianHorizontalFragmentationRule<RangeFragmentationQueryPartModificator> {

	@Override
	public Class<RangeFragmentationQueryPartModificator> getStrategyClass() {

		return RangeFragmentationQueryPartModificator.class;

	}

}