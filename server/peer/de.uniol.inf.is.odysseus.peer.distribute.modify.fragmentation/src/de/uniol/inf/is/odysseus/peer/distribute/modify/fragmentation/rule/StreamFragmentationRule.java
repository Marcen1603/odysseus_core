package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.rule;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.FragmentationInfoBundle;

/**
 * Stream operators can not be part of a fragment for any fragmentation
 * strategies.
 * 
 * @author Michael Brand
 *
 */
public class StreamFragmentationRule implements
		IFragmentationRule<AbstractFragmentationQueryPartModificator, StreamAO> {

	@Override
	public boolean canOperatorBePartOfFragments(
			AbstractFragmentationQueryPartModificator strategy,
			StreamAO operator, AbstractFragmentationHelper helper) {

		return false;

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			StreamAO operator, AbstractFragmentationHelper helper) {

		return false;

	}

	@Override
	public StreamAO specialHandling(ILogicalQueryPart part,
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
	public Class<StreamAO> getOperatorClass() {
		
		return StreamAO.class;
		
	}

}