package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.hash.rule;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TupleAggregateAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.hash.HashFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.hash.HashFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.TupleAggregateHorizontalFragmentationRule;

/**
 * A tuple aggregation can only be part of a fragment for hash horizontal
 * fragmentation strategies, if the grouping aggregates match the hash key.
 * 
 * @author Michael Brand
 *
 */
public class TupleAggregateHashFragmentationRule
		extends
		TupleAggregateHorizontalFragmentationRule<HashFragmentationQueryPartModificator> {

	@Override
	public boolean canOperatorBePartOfFragments(
			HashFragmentationQueryPartModificator strategy,
			TupleAggregateAO operator, AbstractFragmentationParameterHelper helper) {

		return !this.needSpecialHandlingForQueryPart(null, operator, helper);

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			TupleAggregateAO operator, AbstractFragmentationParameterHelper helper) {

		Preconditions.checkArgument(
				helper instanceof HashFragmentationParameterHelper,
				"Fragmentation helper must be a HashFragmentationHelper");

		Optional<List<String>> attributes = ((HashFragmentationParameterHelper) helper)
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
	public Class<HashFragmentationQueryPartModificator> getStrategyClass() {

		return HashFragmentationQueryPartModificator.class;

	}

	@Override
	public Class<TupleAggregateAO> getOperatorClass() {

		return TupleAggregateAO.class;

	}

}