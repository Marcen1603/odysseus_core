package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash.rule;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StateMapAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash.HashHorizontalFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash.HashHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.AbstractStateMapHorizontalFragmentationRule;

/**
 * A state map can only be part of a fragment for hash horizontal fragmentation
 * strategies, if the grouping aggregates match the hash key.
 * 
 * @author Michael Brand
 * 
 */
public class StateMapHashHorizontalFragmentationRule
		extends
		AbstractStateMapHorizontalFragmentationRule<HashHorizontalFragmentationQueryPartModificator> {

	@Override
	public boolean canOperatorBePartOfFragments(
			HashHorizontalFragmentationQueryPartModificator strategy,
			StateMapAO operator, AbstractFragmentationHelper helper) {

		return !this.needSpecialHandlingForQueryPart(null, operator, helper);

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			StateMapAO operator, AbstractFragmentationHelper helper) {

		Preconditions.checkArgument(
				helper instanceof HashHorizontalFragmentationHelper,
				"Fragmentation helper must be a HashFragmentationHelper");

		Optional<List<String>> attributes = ((HashHorizontalFragmentationHelper) helper)
				.determineKeyAttributes();
		if (!attributes.isPresent()) {

			return true;

		}

		Collection<SDFAttribute> aggregationAttributes = operator
				.getGroupingAttributes();
		for (SDFAttribute attribute : aggregationAttributes) {

			if (attributes.get().contains(
					attribute.getSourceName() + "."
							+ attribute.getAttributeName())) {

				return false;

			}

		}

		return true;

	}

	@Override
	public Class<HashHorizontalFragmentationQueryPartModificator> getStrategyClass() {

		return HashHorizontalFragmentationQueryPartModificator.class;

	}

}