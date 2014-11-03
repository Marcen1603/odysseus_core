package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash.rule;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AssociativeStorageAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash.HashHorizontalFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash.HashHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.rule.IFragmentationRule;

/**
 * An associative storage can be part of a fragment for hash horizontal
 * fragmentation strategies, if the hierarchy matches the hash key.
 * 
 * @author Michael Brand
 *
 */
public class AssociativeStorageHashHorizontalFragmentationRule
		implements
		IFragmentationRule<HashHorizontalFragmentationQueryPartModificator, AssociativeStorageAO> {

	@Override
	public boolean canOperatorBePartOfFragments(
			HashHorizontalFragmentationQueryPartModificator strategy,
			AssociativeStorageAO operator,
			AbstractFragmentationParameterHelper helper) {

		return !this.needSpecialHandlingForQueryPart(null, operator, helper);

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			AssociativeStorageAO operator,
			AbstractFragmentationParameterHelper helper) {

		Preconditions.checkArgument(
				helper instanceof HashHorizontalFragmentationParameterHelper,
				"Fragmentation helper must be a HashFragmentationHelper");

		Optional<List<String>> attributes = ((HashHorizontalFragmentationParameterHelper) helper)
				.determineKeyAttributes();
		if (!attributes.isPresent()) {

			return true;

		}

		Collection<SDFAttribute> hierarchyAttributes = operator
				.getHierarchyAttributes();
		for (SDFAttribute attribute : hierarchyAttributes) {

			if (attributes.get().contains(attribute.getAttributeName())) {

				return false;

			}

		}

		return true;

	}

	@Override
	public AssociativeStorageAO specialHandling(ILogicalQueryPart part,
			AbstractFragmentationParameterHelper helper,
			FragmentationInfoBundle bundle)
			throws QueryPartModificationException {

		// Nothing to do
		return null;

	}

	@Override
	public Class<HashHorizontalFragmentationQueryPartModificator> getStrategyClass() {

		return HashHorizontalFragmentationQueryPartModificator.class;

	}

	@Override
	public Class<AssociativeStorageAO> getOperatorClass() {

		return AssociativeStorageAO.class;

	}

}