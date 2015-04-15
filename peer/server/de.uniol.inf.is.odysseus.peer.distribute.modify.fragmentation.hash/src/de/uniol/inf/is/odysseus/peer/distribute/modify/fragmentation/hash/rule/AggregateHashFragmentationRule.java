package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.hash.rule;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.hash.HashFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.hash.HashFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.AggregateHorizontalFragmentationRule;

/**
 * An aggregation can be part of a fragment for hash horizontal fragmentation
 * strategies, if the grouping aggregates match the hash key or if the
 * aggregation function is AVG COUNT or SUM.
 * 
 * @author Michael Brand
 *
 */
public class AggregateHashFragmentationRule
		extends
		AggregateHorizontalFragmentationRule<HashFragmentationQueryPartModificator> {

	@Override
	public boolean canOperatorBePartOfFragments(
			HashFragmentationQueryPartModificator strategy,
			AggregateAO operator, AbstractFragmentationParameterHelper helper) {

		if (!this.needSpecialHandlingForQueryPart(null, operator, helper)) {

			return true;

		}

		return super.canOperatorBePartOfFragments(strategy, operator, helper);

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			AggregateAO operator, AbstractFragmentationParameterHelper helper) {

		Preconditions.checkArgument(
				helper instanceof HashFragmentationParameterHelper,
				"Fragmentation helper must be a HashFragmentationHelper");

		Optional<List<String>> attributes = ((HashFragmentationParameterHelper) helper)
				.determineKeyAttributes();
		if (!attributes.isPresent()) {

			return true;

		}

		Collection<SDFAttribute> aggregationAttributes = operator
				.getGroupingAttributes();
		for (SDFAttribute attribute : aggregationAttributes) {

			if (attributes.get().contains(attribute.getAttributeName())) {

				return false;

			}

		}

		return true;

	}

	@Override
	public Class<HashFragmentationQueryPartModificator> getStrategyClass() {

		return HashFragmentationQueryPartModificator.class;

	}

}