package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.hash;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.HorizontalFragmentationParameterHelper;

/**
 * An fragmentation helper provides useful methods for fragmentation.
 * 
 * @author Michael Brand
 */
public class HashFragmentationParameterHelper extends
		HorizontalFragmentationParameterHelper {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(HashFragmentationParameterHelper.class);

	/**
	 * The index of the parameter identifying the first attribute to build a
	 * hash key.
	 */
	public static final int PARAMETER_INDEX_FIRST_HASHKEY_ATTRIBUTE = 2;

	/**
	 * Creates a new fragmentation helper.
	 * 
	 * @param fragmentationParameters
	 *            The parameters for fragmentation.
	 * @param strategy
	 *            The fragmentation strategy.
	 */
	public HashFragmentationParameterHelper(
			List<String> fragmentationParameters) {

		super(fragmentationParameters);

	}

	/**
	 * Determines the attributes to build a hash key, if given.
	 * 
	 * @return A list containing the names of the attributes to build a hash
	 *         key. {@link Optional#absent()} if no attributes could be found.
	 */
	public Optional<List<String>> determineKeyAttributes() {

		if (this.mFragmentationParameters.size() <= HashFragmentationParameterHelper.PARAMETER_INDEX_FIRST_HASHKEY_ATTRIBUTE) {

			HashFragmentationParameterHelper.LOG
					.debug("Found no attributes to form a key.");
			return Optional.absent();

		}

		List<String> attributes = Lists
				.newArrayList(this.mFragmentationParameters
						.subList(
								HashFragmentationParameterHelper.PARAMETER_INDEX_FIRST_HASHKEY_ATTRIBUTE,
								this.mFragmentationParameters.size()));
		HashFragmentationParameterHelper.LOG.debug("Found '" + attributes
				+ "' as attributes to form a key.");
		return Optional.of(attributes);

	}

}