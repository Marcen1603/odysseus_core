package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.impl;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.newimpl.AbstractHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.AbstractFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.IFragmentationRule;

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
			AbstractWindowAO operator) {

		return (operator instanceof AbstractWindowAO)
				&& ((AbstractWindowAO) operator).getWindowType().equals(
						WindowType.TIME);

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			AbstractWindowAO operator, AbstractFragmentationHelper helper) {

		return false;

	}

	@Override
	public AbstractWindowAO specialHandling(ILogicalQueryPart part,
			AbstractFragmentationHelper helper, FragmentationInfoBundle bundle)
			throws QueryPartModificationException {

		// Nothing to do
		return null;

	}

}