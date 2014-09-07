package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.rule;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.newimpl.RoundRobinHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.AbstractStateMapHorizontalFragmentationRule;

/**
 * A state map can not be part of a fragment for round robin horizontal
 * fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class StateMapRoundRobinHorizontalFragmentationRule
		extends
		AbstractStateMapHorizontalFragmentationRule<RoundRobinHorizontalFragmentationQueryPartModificator> {

	@Override
	public Class<RoundRobinHorizontalFragmentationQueryPartModificator> getStrategyClass() {

		return RoundRobinHorizontalFragmentationQueryPartModificator.class;

	}

}