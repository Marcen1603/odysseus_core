package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.roundrobin.rule;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.TupleAggregateHorizontalFragmentationRule;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.roundrobin.RoundRobinFragmentationQueryPartModificator;

/**
 * A tuple aggregation can not be part of a fragment for range horizontal
 * fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class TupleAggregateRoundRobinFragmentationRule
		extends
		TupleAggregateHorizontalFragmentationRule<RoundRobinFragmentationQueryPartModificator> {

	@Override
	public Class<RoundRobinFragmentationQueryPartModificator> getStrategyClass() {

		return RoundRobinFragmentationQueryPartModificator.class;

	}

}