package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.range.rule;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.AggregateHorizontalFragmentationRule;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.range.RangeFragmentationQueryPartModificator;

/**
 * An aggregation can be part of a fragment for range horizontal fragmentation
 * strategies, if the aggregation function is AVG COUNT or SUM.
 * 
 * @author Michael Brand
 *
 */
public class AggregateRangeFragmentationRule
		extends
		AggregateHorizontalFragmentationRule<RangeFragmentationQueryPartModificator> {

	@Override
	public Class<RangeFragmentationQueryPartModificator> getStrategyClass() {

		return RangeFragmentationQueryPartModificator.class;

	}

}