package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.roundrobin.rule;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.RelationalFastMedianHorizontalFragmentationRule;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.roundrobin.RoundRobinFragmentationQueryPartModificator;

/**
 * A relational fast median can not be part of a fragment for round robin
 * horizontal fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class RelationalFastMedianRoundRobinFragmentationRule
		extends
		RelationalFastMedianHorizontalFragmentationRule<RoundRobinFragmentationQueryPartModificator> {

	@Override
	public Class<RoundRobinFragmentationQueryPartModificator> getStrategyClass() {

		return RoundRobinFragmentationQueryPartModificator.class;

	}

}