package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.rule;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.newimpl.RoundRobinHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.AbstractRelationalFastMedianHorizontalFragmentationRule;

/**
 * A relational fast median can not be part of a fragment for round robin
 * horizontal fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class RelationalFastMedianRoundRobinHorizontalFragmentationRule
		extends
		AbstractRelationalFastMedianHorizontalFragmentationRule<RoundRobinHorizontalFragmentationQueryPartModificator> {

	@Override
	public Class<RoundRobinHorizontalFragmentationQueryPartModificator> getStrategyClass() {

		return RoundRobinHorizontalFragmentationQueryPartModificator.class;

	}

}