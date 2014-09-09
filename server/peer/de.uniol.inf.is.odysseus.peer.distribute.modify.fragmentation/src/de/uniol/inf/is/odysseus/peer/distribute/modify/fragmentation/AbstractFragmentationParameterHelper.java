package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modification.IModificationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modification.ModificationHelper;

/**
 * An fragmentation helper provides useful methods for fragmentation.
 * 
 * @author Michael Brand
 */
public abstract class AbstractFragmentationParameterHelper implements
		IModificationParameterHelper {

	/**
	 * The index of the parameter identifying the start and end point of
	 * fragmentation.
	 */
	public static final int PARAMETER_INDEX_START_AND_END_POINT_IDS = 0;

	/**
	 * The index of the parameter identifying the degree of fragmentation.
	 */
	public static final int PARAMETER_INDEX_DEGREE_OF_FRAGMENTATION = 1;

	/**
	 * The minimum degree of fragmentation.
	 */
	public static final int MIN_DEGREE_OF_FRAGMENTATION = 2;

	/**
	 * The parameters for the fragmentation.
	 */
	protected ImmutableList<String> mFragmentationParameters;

	/**
	 * Creates a new fragmentation helper.
	 * 
	 * @param fragmentationParameters
	 *            The parameters for fragmentation.
	 */
	public AbstractFragmentationParameterHelper(
			List<String> fragmentationParameters) {

		Preconditions.checkNotNull(fragmentationParameters,
				"Fragmentation parameters must be not null!");

		this.mFragmentationParameters = ImmutableList
				.copyOf(fragmentationParameters);

	}

	@Override
	public int determineDegreeOfModification()
			throws QueryPartModificationException {

		// Preconditions
		if (this.mFragmentationParameters.size() < AbstractFragmentationParameterHelper.PARAMETER_INDEX_DEGREE_OF_FRAGMENTATION + 1) {

			throw new QueryPartModificationException(
					"Fragmentation parameters must at least contain two values!");

		}

		return ModificationHelper
				.determineDegreeOfModification(
						this.mFragmentationParameters
								.get(AbstractFragmentationParameterHelper.PARAMETER_INDEX_DEGREE_OF_FRAGMENTATION),
						AbstractFragmentationParameterHelper.MIN_DEGREE_OF_FRAGMENTATION);

	}

	@Override
	public IPair<ILogicalOperator, Optional<ILogicalOperator>> determineStartAndEndPoints(
			Collection<ILogicalQueryPart> queryParts)
			throws QueryPartModificationException {

		// Preconditions
		if (this.mFragmentationParameters.size() < AbstractFragmentationParameterHelper.PARAMETER_INDEX_START_AND_END_POINT_IDS + 1) {

			throw new QueryPartModificationException(
					"Fragmentation parameters must at least contain one value!");

		}

		String startAndEndPointParameter = this.mFragmentationParameters
				.get(AbstractFragmentationParameterHelper.PARAMETER_INDEX_START_AND_END_POINT_IDS);

		return ModificationHelper.determineStartAndEndPoints(queryParts,
				startAndEndPointParameter);

	}

}