package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.roundrobin.rule;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.AggregateHorizontalFragmentationRule;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.roundrobin.RoundRobinFragmentationQueryPartModificator;

/**
 * An aggregation can be part of a fragment for round robin horizontal
 * fragmentation strategies, if the aggregation function is AVG COUNT or SUM.
 * 
 * @author Michael Brand
 *
 */
public class AggregateRoundRobinFragmentationRule
		extends
		AggregateHorizontalFragmentationRule<RoundRobinFragmentationQueryPartModificator> {

	@Override
	public Class<RoundRobinFragmentationQueryPartModificator> getStrategyClass() {

		return RoundRobinFragmentationQueryPartModificator.class;

	}

}