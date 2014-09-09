package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.TupleAggregateAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.AbstractHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.rule.IFragmentationRule;

/**
 * A tuple aggregation can not be part of a fragment for horizontal
 * fragmentation strategies.
 * 
 * @author Michael Brand
 *
 */
public abstract class AbstractTupleAggregateHorizontalFragmentationRule<Strategy extends AbstractHorizontalFragmentationQueryPartModificator>
		implements IFragmentationRule<Strategy, TupleAggregateAO> {

	@Override
	public Class<TupleAggregateAO> getOperatorClass() {

		return TupleAggregateAO.class;

	}

	@Override
	public boolean canOperatorBePartOfFragments(Strategy strategy,
			TupleAggregateAO operator, AbstractFragmentationHelper helper) {

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

}