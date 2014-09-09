package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash.rule;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TupleAggregateAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash.HashHorizontalFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash.HashHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.AbstractTupleAggregateHorizontalFragmentationRule;

/**
 * A tuple aggregation can only be part of a fragment for hash horizontal
 * fragmentation strategies, if the grouping aggregates match the hash key.
 * 
 * @author Michael Brand
 *
 */
public class TupleAggregateHashHorizontalFragmentationRule
		extends
		AbstractTupleAggregateHorizontalFragmentationRule<HashHorizontalFragmentationQueryPartModificator> {

	@Override
	public boolean canOperatorBePartOfFragments(
			HashHorizontalFragmentationQueryPartModificator strategy,
			TupleAggregateAO operator, AbstractFragmentationHelper helper) {

		return !this.needSpecialHandlingForQueryPart(null, operator, helper);

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			TupleAggregateAO operator, AbstractFragmentationHelper helper) {

		Preconditions.checkArgument(
				helper instanceof HashHorizontalFragmentationHelper,
				"Fragmentation helper must be a HashFragmentationHelper");

		Optional<List<String>> attributes = ((HashHorizontalFragmentationHelper) helper)
				.determineKeyAttributes();
		if (!attributes.isPresent()) {

			return true;

		}

		SDFAttribute aggregationAttributes = operator.getAttribute();
		return attributes.get().contains(
				aggregationAttributes.getSourceName() + "."
						+ aggregationAttributes.getAttributeName());

	}

	@Override
	public Class<HashHorizontalFragmentationQueryPartModificator> getStrategyClass() {

		return HashHorizontalFragmentationQueryPartModificator.class;

	}

	@Override
	public Class<TupleAggregateAO> getOperatorClass() {

		return TupleAggregateAO.class;

	}

}