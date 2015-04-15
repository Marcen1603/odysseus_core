package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.hash.rule;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.hash.HashFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.hash.HashFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.RelationalFastMedianHorizontalFragmentationRule;
import de.uniol.inf.is.odysseus.relational_interval.logicaloperator.RelationalFastMedianAO;

/**
 * A relational fast median can only be part of a fragment for hash horizontal
 * fragmentation strategies, if the grouping aggregates match the hash key.
 * 
 * @author Michael Brand
 * 
 */
public class RelationalFastMedianHashFragmentationRule
		extends
		RelationalFastMedianHorizontalFragmentationRule<HashFragmentationQueryPartModificator> {

	@Override
	public boolean canOperatorBePartOfFragments(
			HashFragmentationQueryPartModificator strategy,
			RelationalFastMedianAO operator, AbstractFragmentationParameterHelper helper) {

		return !this.needSpecialHandlingForQueryPart(null, operator, helper);

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			RelationalFastMedianAO operator, AbstractFragmentationParameterHelper helper) {

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

			if (attributes.get().contains(
					attribute.getSourceName() + "."
							+ attribute.getAttributeName())) {

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