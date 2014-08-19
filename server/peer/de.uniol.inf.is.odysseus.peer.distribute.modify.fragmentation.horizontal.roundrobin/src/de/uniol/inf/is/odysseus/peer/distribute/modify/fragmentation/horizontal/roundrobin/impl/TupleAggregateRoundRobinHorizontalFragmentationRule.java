package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.impl;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.TupleAggregateAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.newimpl.RoundRobinHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.AbstractFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.IFragmentationRule;

/**
 * A tuple aggregation can not be part of a fragment for range horizontal
 * fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public class TupleAggregateRoundRobinHorizontalFragmentationRule
		implements
		IFragmentationRule<RoundRobinHorizontalFragmentationQueryPartModificator, TupleAggregateAO> {

	@Override
	public boolean canOperatorBePartOfFragments(
			RoundRobinHorizontalFragmentationQueryPartModificator strategy,
			TupleAggregateAO operator) {

		return false;

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			TupleAggregateAO operator, AbstractFragmentationHelper helper) {

		return false;

	}

	@Override
	public TupleAggregateAO specialHandling(ILogicalQueryPart part,
			AbstractFragmentationHelper helper, FragmentationInfoBundle bundle)
			throws QueryPartModificationException {
		
		// Nothing to do
		return null;
		
	}
	
	@Override
	public Class<RoundRobinHorizontalFragmentationQueryPartModificator> getStrategyClass() {
		
		return RoundRobinHorizontalFragmentationQueryPartModificator.class;
		
	}

	@Override
	public Class<TupleAggregateAO> getOperatorClass() {
		
		return TupleAggregateAO.class;
		
	}

}