package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.rule;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.FragmentationInfoBundle;

/**
 * Access operators can not be part of a fragment for any fragmentation
 * strategies.
 * 
 * @author Michael Brand
 *
 */
public class AccessFragmentationRule
		implements
		IFragmentationRule<AbstractFragmentationQueryPartModificator, AbstractAccessAO> {

	@Override
	public boolean canOperatorBePartOfFragments(
			AbstractFragmentationQueryPartModificator strategy,
			AbstractAccessAO operator, AbstractFragmentationParameterHelper helper) {

		return false;

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			AbstractAccessAO operator, AbstractFragmentationParameterHelper helper) {

		return false;

	}

	@Override
	public AbstractAccessAO specialHandling(ILogicalQueryPart part,
			AbstractFragmentationParameterHelper helper, FragmentationInfoBundle bundle)
			throws QueryPartModificationException {

		// Nothing to do
		return null;

	}

	@Override
	public Class<AbstractFragmentationQueryPartModificator> getStrategyClass() {
		
		return AbstractFragmentationQueryPartModificator.class;
		
	}

	@Override
	public Class<AbstractAccessAO> getOperatorClass() {
		
		return AbstractAccessAO.class;
		
	}

}