package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.rule;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.newimpl.RoundRobinHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.AbstractTupleAggregateHorizontalFragmentationRule;

/**
 * A tuple aggregation can not be part of a fragment for range horizontal
 * fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class TupleAggregateRoundRobinHorizontalFragmentationRule
		extends
		AbstractTupleAggregateHorizontalFragmentationRule<RoundRobinHorizontalFragmentationQueryPartModificator> {

	@Override
	public Class<RoundRobinHorizontalFragmentationQueryPartModificator> getStrategyClass() {

		return RoundRobinHorizontalFragmentationQueryPartModificator.class;

	}

}