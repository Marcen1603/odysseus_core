package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.AbstractHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.rule.IFragmentationRule;

/**
 * A window using time parameters can not be part of a fragment for horizontal
 * fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class WindowHorizontalFragmentationRule
		implements
		IFragmentationRule<AbstractHorizontalFragmentationQueryPartModificator, AbstractWindowAO> {

	@Override
	public boolean canOperatorBePartOfFragments(
			AbstractHorizontalFragmentationQueryPartModificator strategy,
			AbstractWindowAO operator, AbstractFragmentationParameterHelper helper) {

		return operator.getWindowType().equals(WindowType.TIME);

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			AbstractWindowAO operator, AbstractFragmentationParameterHelper helper) {

		return false;

	}

	@Override
	public AbstractWindowAO specialHandling(ILogicalQueryPart part,
			AbstractFragmentationParameterHelper helper, FragmentationInfoBundle bundle)
			throws QueryPartModificationException {

		// Nothing to do
		return null;

	}

	@Override
	public Class<AbstractHorizontalFragmentationQueryPartModificator> getStrategyClass() {

		return AbstractHorizontalFragmentationQueryPartModificator.class;

	}

	@Override
	public Class<AbstractWindowAO> getOperatorClass() {

		return AbstractWindowAO.class;

	}

}