package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.range.rule;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.StateMapHorizontalFragmentationRule;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.range.RangeFragmentationQueryPartModificator;

/**
 * A state map can not be part of a fragment for range horizontal fragmentation
 * strategies.
 * 
 * @author Michael Brand
 *
 */
public class StateMapRangeFragmentationRule
		extends
		StateMapHorizontalFragmentationRule<RangeFragmentationQueryPartModificator> {

	@Override
	public Class<RangeFragmentationQueryPartModificator> getStrategyClass() {

		return RangeFragmentationQueryPartModificator.class;

	}

}