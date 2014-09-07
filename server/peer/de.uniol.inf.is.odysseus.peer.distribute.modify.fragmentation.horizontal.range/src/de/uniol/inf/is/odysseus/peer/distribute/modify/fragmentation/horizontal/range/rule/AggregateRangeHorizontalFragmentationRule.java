package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.rule;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.newimpl.RangeHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.AbstractAggregateHorizontalFragmentationRule;

/**
 * An aggregation can be part of a fragment for range horizontal fragmentation
 * strategies, if the aggregation function is AVG COUNT or SUM.
 * 
 * @author Michael Brand
 *
 */
public class AggregateRangeHorizontalFragmentationRule
		extends
		AbstractAggregateHorizontalFragmentationRule<RangeHorizontalFragmentationQueryPartModificator> {

	@Override
	public Class<RangeHorizontalFragmentationQueryPartModificator> getStrategyClass() {

		return RangeHorizontalFragmentationQueryPartModificator.class;

	}

}