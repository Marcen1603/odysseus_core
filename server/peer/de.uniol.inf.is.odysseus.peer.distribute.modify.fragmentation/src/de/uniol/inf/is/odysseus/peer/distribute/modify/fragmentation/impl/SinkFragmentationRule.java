package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.impl;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.AbstractFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.AbstractFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.IFragmentationRule;

/**
 * A sink without being a source at the same time can not be part of a fragment
 * for any fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class SinkFragmentationRule
		implements
		IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator> {

	@Override
	public boolean canOperatorBePartOfFragments(
			AbstractFragmentationQueryPartModificator strategy,
			ILogicalOperator operator) {

		return !operator.isSinkOperator() || operator.isSourceOperator();

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			ILogicalOperator operator, AbstractFragmentationHelper helper) {

		return false;

	}

	@Override
	public ILogicalOperator specialHandling(ILogicalQueryPart part,
			AbstractFragmentationHelper helper, FragmentationInfoBundle bundle)
			throws QueryPartModificationException {

		// Nothing to do
		return null;

	}
	
	@Override
	public Class<AbstractFragmentationQueryPartModificator> getStrategyClass() {
		
		return AbstractFragmentationQueryPartModificator.class;
		
	}

	@Override
	public Class<ILogicalOperator> getOperatorClass() {
		
		return ILogicalOperator.class;
		
	}

}