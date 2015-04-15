package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.rule;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.RoundRobinHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.RelationalFastMedianHorizontalFragmentationRule;

/**
 * A relational fast median can not be part of a fragment for round robin
 * horizontal fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class RelationalFastMedianRoundRobinHorizontalFragmentationRule
		extends
		RelationalFastMedianHorizontalFragmentationRule<RoundRobinHorizontalFragmentationQueryPartModificator> {

	@Override
	public Class<RoundRobinHorizontalFragmentationQueryPartModificator> getStrategyClass() {

		return RoundRobinHorizontalFragmentationQueryPartModificator.class;

	}

}