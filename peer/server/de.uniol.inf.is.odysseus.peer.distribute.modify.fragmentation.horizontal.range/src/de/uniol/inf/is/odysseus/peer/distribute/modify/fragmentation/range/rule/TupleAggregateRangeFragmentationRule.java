package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.range.rule;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.TupleAggregateHorizontalFragmentationRule;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.range.RangeFragmentationQueryPartModificator;

/**
 * A tuple aggregation can not be part of a fragment for range horizontal
 * fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class TupleAggregateRangeFragmentationRule
		extends
		TupleAggregateHorizontalFragmentationRule<RangeFragmentationQueryPartModificator> {

	@Override
	public Class<RangeFragmentationQueryPartModificator> getStrategyClass() {

		return RangeFragmentationQueryPartModificator.class;

	}

}