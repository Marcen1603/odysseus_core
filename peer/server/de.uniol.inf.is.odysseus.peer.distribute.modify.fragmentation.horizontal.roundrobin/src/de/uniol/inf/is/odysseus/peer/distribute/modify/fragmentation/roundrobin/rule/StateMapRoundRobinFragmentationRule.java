package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.roundrobin.rule;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.StateMapHorizontalFragmentationRule;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.roundrobin.RoundRobinFragmentationQueryPartModificator;

/**
 * A state map can not be part of a fragment for round robin horizontal
 * fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class StateMapRoundRobinFragmentationRule
		extends
		StateMapHorizontalFragmentationRule<RoundRobinFragmentationQueryPartModificator> {

	@Override
	public Class<RoundRobinFragmentationQueryPartModificator> getStrategyClass() {

		return RoundRobinFragmentationQueryPartModificator.class;

	}

}