package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.newimpl.AbstractHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.AbstractFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.IFragmentationRule;
import de.uniol.inf.is.odysseus.relational_interval.logicaloperator.RelationalFastMedianAO;

/**
 * A relational fast median can not be part of a fragment for fragmentation
 * strategies.
 * 
 * @author Michael Brand
 *
 */
public abstract class AbstractRelationalFastMedianHorizontalFragmentationRule<Strategy extends AbstractHorizontalFragmentationQueryPartModificator>
		implements IFragmentationRule<Strategy, RelationalFastMedianAO> {

	@Override
	public Class<RelationalFastMedianAO> getOperatorClass() {

		return RelationalFastMedianAO.class;

	}

	@Override
	public boolean canOperatorBePartOfFragments(Strategy strategy,
			RelationalFastMedianAO operator, AbstractFragmentationHelper helper) {

		return false;

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			RelationalFastMedianAO operator, AbstractFragmentationHelper helper) {

		return false;

	}

	@Override
	public RelationalFastMedianAO specialHandling(ILogicalQueryPart part,
			AbstractFragmentationHelper helper, FragmentationInfoBundle bundle)
			throws QueryPartModificationException {

		// Nothing to do
		return null;

	}

}