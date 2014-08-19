package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.newimpl.HorizontalFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range.newimpl.RangeHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.AbstractFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.IFragmentationRule;

/**
 * An aggregation can be part of a fragment for range horizontal fragmentation
 * strategies.
 * 
 * @author Michael Brand
 *
 */
public class AggregateRangeHorizontalFragmentationRule
		implements
		IFragmentationRule<RangeHorizontalFragmentationQueryPartModificator, AggregateAO> {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(AggregateRangeHorizontalFragmentationRule.class);

	@Override
	public boolean canOperatorBePartOfFragments(
			RangeHorizontalFragmentationQueryPartModificator strategy,
			AggregateAO operator) {

		return true;

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			AggregateAO operator, AbstractFragmentationHelper helper) {

		return true;

	}

	@Override
	public AggregateAO specialHandling(ILogicalQueryPart part,
			AbstractFragmentationHelper helper, FragmentationInfoBundle bundle)
			throws QueryPartModificationException {

		Preconditions.checkNotNull(part, "Query part must be not null!");
		Preconditions.checkNotNull(helper,
				"Fragmentation helper must be not null!");
		Preconditions
				.checkArgument(helper instanceof HorizontalFragmentationHelper,
						"Fragmentation helper must be a HorizontalFragmentationHelper!");
		Preconditions.checkNotNull(bundle,
				"Fragmentation info bundle must be not null!");

		AggregateAO aggregation = null;

		for (ILogicalOperator operator : part.getOperators()) {

			if (operator instanceof AggregateAO) {

				if (!this.needSpecialHandlingForQueryPart(part, aggregation,
						helper)) {

					continue;

				} else if (aggregation == null) {

					aggregation = HorizontalFragmentationHelper
							.changeAggregation(part, aggregation, bundle);
					AggregateRangeHorizontalFragmentationRule.LOG
							.debug("Found {} as an aggregation, which needs to be changed in {}",
									operator, part);

				} else
					throw new QueryPartModificationException(
							"Can not fragment a query part containing more than one aggregation!");

			}

		}

		if (aggregation == null) {

			throw new QueryPartModificationException(
					"No aggregation found within " + part + "!");

		}

		return aggregation;

	}
	
	@Override
	public Class<RangeHorizontalFragmentationQueryPartModificator> getStrategyClass() {
		
		return RangeHorizontalFragmentationQueryPartModificator.class;
		
	}

	@Override
	public Class<AggregateAO> getOperatorClass() {
		
		return AggregateAO.class;
		
	}

}