package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.FragmentationInfoBundle;

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
			ILogicalOperator operator, AbstractFragmentationParameterHelper helper) {

		return !operator.isSinkOperator() || operator.isSourceOperator();

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			ILogicalOperator operator, AbstractFragmentationParameterHelper helper) {

		return false;

	}

	@Override
	public ILogicalOperator specialHandling(ILogicalQueryPart part,
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
	public Class<ILogicalOperator> getOperatorClass() {
		
		return ILogicalOperator.class;
		
	}

}