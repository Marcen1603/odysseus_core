package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.rule;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.newimpl.RangeHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.AbstractTupleAggregateHorizontalFragmentationRule;

/**
 * A tuple aggregation can not be part of a fragment for range horizontal
 * fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class TupleAggregateRangeHorizontalFragmentationRule
		extends
		AbstractTupleAggregateHorizontalFragmentationRule<RangeHorizontalFragmentationQueryPartModificator> {

	@Override
	public Class<RangeHorizontalFragmentationQueryPartModificator> getStrategyClass() {

		return RangeHorizontalFragmentationQueryPartModificator.class;

	}

}