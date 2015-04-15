package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.HorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.rule.IFragmentationRule;
import de.uniol.inf.is.odysseus.relational_interval.logicaloperator.RelationalFastMedianAO;

/**
 * A relational fast median can not be part of a fragment for fragmentation
 * strategies.
 * 
 * @author Michael Brand
 *
 */
public abstract class RelationalFastMedianHorizontalFragmentationRule<Strategy extends HorizontalFragmentationQueryPartModificator>
		implements IFragmentationRule<Strategy, RelationalFastMedianAO> {

	@Override
	public Class<RelationalFastMedianAO> getOperatorClass() {

		return RelationalFastMedianAO.class;

	}

	@Override
	public boolean canOperatorBePartOfFragments(Strategy strategy,
			RelationalFastMedianAO operator, AbstractFragmentationParameterHelper helper) {

		return false;

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			RelationalFastMedianAO operator, AbstractFragmentationParameterHelper helper) {

		return false;

	}

	@Override
	public RelationalFastMedianAO specialHandling(ILogicalQueryPart part,
			AbstractFragmentationParameterHelper helper, FragmentationInfoBundle bundle)
			throws QueryPartModificationException {

		// Nothing to do
		return null;

	}

}