package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.AbstractHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.rule.IFragmentationRule;

/**
 * A state map can not be part of a fragment for horizontal fragmentation
 * strategies.
 * 
 * @author Michael Brand
 *
 */
public abstract class AbstractStateMapHorizontalFragmentationRule<Strategy extends AbstractHorizontalFragmentationQueryPartModificator>
		implements IFragmentationRule<Strategy, StateMapAO> {

	@Override
	public Class<StateMapAO> getOperatorClass() {

		return StateMapAO.class;

	}

	@Override
	public boolean canOperatorBePartOfFragments(Strategy strategy,
			StateMapAO operator, AbstractFragmentationParameterHelper helper) {

		return false;

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			StateMapAO operator, AbstractFragmentationParameterHelper helper) {

		return false;

	}

	@Override
	public StateMapAO specialHandling(ILogicalQueryPart part,
			AbstractFragmentationParameterHelper helper, FragmentationInfoBundle bundle)
			throws QueryPartModificationException {

		// Nothing to do
		return null;

	}

}