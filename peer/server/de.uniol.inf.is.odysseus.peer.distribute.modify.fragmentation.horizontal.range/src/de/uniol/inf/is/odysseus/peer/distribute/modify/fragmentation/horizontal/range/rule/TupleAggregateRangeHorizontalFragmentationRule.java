package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.rule;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.RangeHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.TupleAggregateHorizontalFragmentationRule;

/**
 * A tuple aggregation can not be part of a fragment for range horizontal
 * fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class TupleAggregateRangeHorizontalFragmentationRule
		extends
		TupleAggregateHorizontalFragmentationRule<RangeHorizontalFragmentationQueryPartModificator> {

	@Override
	public Class<RangeHorizontalFragmentationQueryPartModificator> getStrategyClass() {

		return RangeHorizontalFragmentationQueryPartModificator.class;

	}

}