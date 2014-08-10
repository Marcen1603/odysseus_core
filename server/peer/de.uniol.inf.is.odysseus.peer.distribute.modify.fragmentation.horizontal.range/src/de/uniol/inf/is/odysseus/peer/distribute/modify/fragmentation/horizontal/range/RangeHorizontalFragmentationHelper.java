package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.range;

import java.util.List;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.HorizontalFragmentationHelper;

/**
 * An fragmentation helper provides useful methods for fragmentation.
 * 
 * @author Michael Brand
 */
public class RangeHorizontalFragmentationHelper extends
		HorizontalFragmentationHelper {

	/**
	 * The index of the parameter identifying the attribute to build ranges.
	 */
	public static final int PARAMETER_INDEX_ATTRIBUTE = 0;

	/**
	 * The index of the first parameter identifying the ranges.
	 */
	public static final int PARAMETER_INDEX_FIRST_RANGE = 1;

	/**
	 * Creates a new fragmentation helper.
	 * 
	 * @param fragmentationParameters
	 *            The parameters for fragmentation.
	 * @param strategy
	 *            The fragmentation strategy.
	 */
	public RangeHorizontalFragmentationHelper(
			List<String> fragmentationParameters) {

		super(fragmentationParameters);

	}

	/**
	 * Determines the attribute to be fragmented.
	 * 
	 * @return The attribute to be fragmented.
	 */
	public String determineFullQualifiedAttribute() {

		Preconditions
				.checkArgument(
						this.mFragmentationParameters.size() <= RangeHorizontalFragmentationHelper.PARAMETER_INDEX_ATTRIBUTE,
						"Attribute to build ranges must be given!");

		return this.mFragmentationParameters
				.get(RangeHorizontalFragmentationHelper.PARAMETER_INDEX_ATTRIBUTE);

	}

	/**
	 * Determines the ranges.
	 * 
	 * @return The lower limits of the ranges.
	 */
	public List<String> determineRanges() {

		Preconditions
				.checkArgument(
						this.mFragmentationParameters.size() <= RangeHorizontalFragmentationHelper.PARAMETER_INDEX_FIRST_RANGE,
						"Lower limits for the ranges must be giben!");

		List<String> ranges = this.mFragmentationParameters.subList(
				RangeHorizontalFragmentationHelper.PARAMETER_INDEX_FIRST_RANGE,
				this.mFragmentationParameters.size());
		return ranges;

	}

	@Override
	public int determineDegreeOfFragmentation()
			throws QueryPartModificationException {

		// Preconditions
		if (!this.mFragmentationParameters.isEmpty()) {

			throw new QueryPartModificationException(
					"Fragmentation parameters must at least contain one value!");

		}

		return this.mFragmentationParameters.size(); // +1 for the default
														// output port

	}

}