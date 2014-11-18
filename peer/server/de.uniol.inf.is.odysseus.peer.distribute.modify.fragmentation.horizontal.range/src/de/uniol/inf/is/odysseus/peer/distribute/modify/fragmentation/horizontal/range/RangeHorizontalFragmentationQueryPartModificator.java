package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range;

import java.util.List;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.AbstractHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.RangeFragmentAO;

/**
 * The fragmentation uses the given query parts and informations from
 * Odysseus-Script to fragment a data stream for each query part as follows: <br />
 * 1. Make as many copies as the degree of fragmentation from the query parts to
 * build fragments. <br />
 * 2. Insert fragment operators and reunion operators for those fragments. <br />
 * 3. Attach all query parts not to build fragments to the modified fragments. <br />
 * <br />
 * A horizontal fragmentation splits the data streams by routing complete
 * elements to different operators. <br />
 * <br />
 * The range algorithm uses given lower limits of attribute values. Those limits
 * determine the next operator for the element.
 * 
 * @author Michael Brand
 */
public class RangeHorizontalFragmentationQueryPartModificator extends
		AbstractHorizontalFragmentationQueryPartModificator {

	@Override
	public String getName() {

		return "fragmentation_horizontal_range";

	}
	
	@Override
	protected AbstractFragmentationParameterHelper createFragmentationHelper(
			List<String> fragmentationParameters) {

		return new RangeHorizontalFragmentationParameterHelper(fragmentationParameters);

	}

	@Override
	protected FragmentationInfoBundle createFragmentationInfoBundle(
			AbstractFragmentationParameterHelper helper) {

		Preconditions.checkNotNull(helper,
				"Fragmentation helper must be not null!");
		Preconditions.checkArgument(
				helper instanceof RangeHorizontalFragmentationParameterHelper,
				"The fragmentation helper must be a RangeFragmentationHelper!");

		RangeHorizontalFragmentationInfoBundle bundle = new RangeHorizontalFragmentationInfoBundle();
		bundle.setAttributeForRanges(((RangeHorizontalFragmentationParameterHelper) helper)
				.determineFullQualifiedAttribute());
		bundle.setRanges(((RangeHorizontalFragmentationParameterHelper) helper)
				.determineRanges());
		return bundle;

	}

	@Override
	protected ILogicalOperator createFragmentOperator(int numFragments,
			FragmentationInfoBundle bundle)
			throws QueryPartModificationException {

		Preconditions.checkNotNull(bundle,
				"Fragmentation info bundle must be not null!");
		Preconditions
				.checkArgument(
						bundle instanceof RangeHorizontalFragmentationInfoBundle,
						"The fragmentation info bundle must be a RangeFragmentationInfoBundle!");

		if (numFragments < 1)
			throw new QueryPartModificationException(
					"Invalid number of fragments: " + numFragments);

		RangeFragmentAO fragmentAO = new RangeFragmentAO();
		fragmentAO
				.setAttribute(((RangeHorizontalFragmentationInfoBundle) bundle)
						.getAttributeForRanges());
		fragmentAO.setRanges(((RangeHorizontalFragmentationInfoBundle) bundle)
				.getRanges());
		return fragmentAO;

	}

}