package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.Activator;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

/**
 * The fragmentation uses the given query parts and informations from
 * Odysseus-Script to fragment a data stream for each query part as follows: <br />
 * 1. Make as many copies as the degree of fragmentation from the query parts to
 * build fragments. <br />
 * 2. Insert fragment operators and reunion operators for those fragments. <br />
 * 3. Attach all query parts not to build fragments to the modified fragments.
 * 
 * @author Michael Brand
 */
public abstract class AbstractFragmentationQueryPartModificator implements
		IQueryPartModificator {

	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(AbstractFragmentationQueryPartModificator.class);

	/**
	 * Determines all operators to build fragments.
	 * 
	 * @param operators
	 *            The operators to check.
	 * @param helper
	 *            The {@link AbstractFragmentationHelper} instance.
	 * @param bundle
	 *            The {@link AbstractFragmentationInfoBundle} instance.
	 * @return All operators within <code>operators</code> to build fragments.
	 */
	private static Collection<ILogicalOperator> determineOperatorsForFragment(
			Collection<ILogicalOperator> operators,
			AbstractFragmentationHelper helper,
			AbstractFragmentationInfoBundle bundle) {

		// Preconditions
		Preconditions.checkNotNull(operators,
				"List of operators must be not null!");
		Preconditions.checkNotNull(helper,
				"Fragmentation helper must be not null!");
		Preconditions.checkNotNull(bundle,
				"Fragmentation info bundle must be not null!");
		Preconditions
				.checkNotNull(bundle.getOriginStartOperator(),
						"The operator marking the start of fragmentation must be not null!");
		Preconditions
				.checkNotNull(bundle.getOriginEndOperator(),
						"The operator marking the end of fragmentation must be not null!");

		final Collection<ILogicalOperator> operatorsForFragment = Lists
				.newArrayList();

		for (ILogicalOperator operator : operators) {

			if (operator.isSinkOperator() && !operator.isSourceOperator()) {

				continue;

			} else if (AbstractFragmentationHelper.isOperatorAbove(operator,
					bundle.getOriginStartOperator())
					&& (!bundle.getOriginEndOperator().isPresent() || AbstractFragmentationHelper
							.isOperatorAbove(bundle.getOriginEndOperator()
									.get(), operator))
					&& !helper.isOperatorException(operator)) {

				operatorsForFragment.add(operator);

			}

		}

		return operatorsForFragment;

	}

	/**
	 * Determines all query parts to build fragments.
	 * 
	 * @param queryParts
	 *            The query parts to check.
	 * @param helper
	 *            The {@link AbstractFragmentationHelper} instance.
	 * @param bundle
	 *            The {@link AbstractFragmentationInfoBundle} instance.
	 * @return All operators within <code>operators</code> to build fragments.
	 */
	private static IPair<Collection<ILogicalQueryPart>, Collection<ILogicalQueryPart>> determineFragments(
			Collection<ILogicalQueryPart> queryParts,
			AbstractFragmentationHelper helper,
			AbstractFragmentationInfoBundle bundle) {

		// Preconditions
		Preconditions.checkNotNull(queryParts,
				"List of query parts must be not null!");
		Preconditions.checkNotNull(!queryParts.isEmpty(),
				"List of query parts must be not empty!");

		final Collection<ILogicalQueryPart> fragments = Lists.newArrayList();
		final Collection<ILogicalQueryPart> nonfragments = Lists.newArrayList();

		for (ILogicalQueryPart queryPart : queryParts) {

			final Collection<ILogicalOperator> operatorsForFragment = AbstractFragmentationQueryPartModificator
					.determineOperatorsForFragment(queryPart.getOperators(),
							helper, bundle);
			if (!operatorsForFragment.isEmpty()) {

				final ILogicalQueryPart fragment = new LogicalQueryPart(
						operatorsForFragment, queryPart.getAvoidingQueryParts());
				fragments.add(fragment);

			}

			if (operatorsForFragment.size() < queryPart.getOperators().size()) {

				final Collection<ILogicalOperator> operatorsForNonfragment = Lists
						.newArrayList(queryPart.getOperators());
				operatorsForNonfragment.removeAll(operatorsForFragment);

				if (!operatorsForNonfragment.isEmpty()) {

					final ILogicalQueryPart nonfragment = new LogicalQueryPart(
							operatorsForNonfragment,
							queryPart.getAvoidingQueryParts());
					nonfragments.add(nonfragment);

				}

			}

		}

		return new Pair<Collection<ILogicalQueryPart>, Collection<ILogicalQueryPart>>(
				fragments, nonfragments);

	}

	/**
	 * Makes preparations for fragmentation. <br />
	 * The degree of fragmentation, the operators marking start and end of
	 * fragmentation and the fragments are stored within<code>bundle</code>.
	 * 
	 * @param queryParts
	 *            The given query parts.
	 * @param helper
	 *            The {@link AbstractFragmentationHelper} instance.
	 * @param bundle
	 *            The {@link AbstractFragmentationInfoBundle} instance.
	 * @return True, if <code>queryParts</code> and the informations given by
	 *         Odysseus-Script are valid for fragmentation.
	 * @throws QueryPartModificationException
	 *             If the parameters for fragmentation do not contain two
	 *             parameters, if the second parameter is no integer or if the
	 *             degree of fragmentation if lower than
	 *             {@value #minDegreeOfFragmentation}. <br />
	 *             If the parameters for fragmentation is empty or if the first
	 *             parameter does not match any patterns within
	 *             {@value #startAndEndPointPatterns}.<br />
	 *             If no query parts remain to build fragments.
	 */
	private static void prepare(Collection<ILogicalQueryPart> queryParts,
			AbstractFragmentationHelper helper,
			AbstractFragmentationInfoBundle bundle)
			throws QueryPartModificationException {

		// Preconditions
		Preconditions.checkNotNull(helper,
				"Fragmentation helper must be not null!");
		Preconditions.checkNotNull(bundle,
				"Fragmentation info bundle must be not null!");

		// Determine degree of fragmentation
		final int degree = helper.determineDegreeOfFragmentation();
		bundle.setDegreeOfFragmentation(degree);

		// Determine identifiers for start point and end point for fragmentation
		final IPair<ILogicalOperator, Optional<ILogicalOperator>> startAndendPointOfFragmentation = helper
				.determineStartAndEndPoints(queryParts);
		bundle.setOriginStartOperator(startAndendPointOfFragmentation.getE1());
		bundle.setOriginEndOperator(startAndendPointOfFragmentation.getE2());

		// Determine query parts to be fragments and those to be outside of
		// fragments
		final IPair<Collection<ILogicalQueryPart>, Collection<ILogicalQueryPart>> fragmentsAndNonfragments = AbstractFragmentationQueryPartModificator
				.determineFragments(queryParts, helper, bundle);
		if (fragmentsAndNonfragments.getE1().isEmpty()) {

			throw new QueryPartModificationException(
					"No query parts given to build fragments!");

		}
		bundle.setOriginalRelevantParts(fragmentsAndNonfragments.getE1());
		bundle.setOriginalIrrelevantParts(fragmentsAndNonfragments.getE2());

	}

	/**
	 * Checks, if there are enough peers available (
	 * {@link IP2PDictionary#getRemotePeerIDs()}) for the desired degree of
	 * fragmentation.
	 * 
	 * @param bundle
	 *            The {@link AbstractFragmentationInfoBundle} instance
	 *            containing the degree of fragmentation-
	 * @return True, if the number of available peers is greater than or equal
	 *         to the degree of fragmentation.
	 */
	private static boolean validateDegreeOfFragmentation(
			AbstractFragmentationInfoBundle bundle) {

		Preconditions.checkNotNull(bundle,
				"Fragmentation info bundle must be not null!");
		Preconditions.checkNotNull(bundle.getCopyMap(),
				"Mapping of copied query parts to originals must be not null!");

		final Optional<IP2PDictionary> optP2PDictionary = Activator
				.getP2PDictionary();

		if (!optP2PDictionary.isPresent()) {

			AbstractFragmentationQueryPartModificator.log
					.error("No P2P dictionary found!");
			return false;

		}

		final int numQueryPartsToAllocate = bundle.getCopyMap().values().size();
		final int availablePeers = optP2PDictionary.get().getRemotePeerIDs()
				.size();

		if (availablePeers + 1 < numQueryPartsToAllocate) {

			AbstractFragmentationQueryPartModificator.log.warn(
					"Got {} peers. {} peers needed!", availablePeers,
					numQueryPartsToAllocate);
			return false;

		} else if (availablePeers + 1 == numQueryPartsToAllocate) {

			AbstractFragmentationQueryPartModificator.log
					.warn("Got enough peers for a suitable fragmentation only if the local peer is considered!");

		}

		return true;

	}

	/**
	 * Makes copies of the origin query parts as follows: <br />
	 * For every fragment, there will be as many copies made, as
	 * {@link AbstractFragmentationInfoBundle#getDegreeOfFragmentation()}. <br />
	 * For every other query part, there will be one copy made. <br />
	 * Each copy will be a cut off query part having o incoming or outgoing
	 * subscriptions. The result is available as
	 * {@link AbstractFragmentationInfoBundle#getCopyMap()}.
	 * 
	 * @param bundle
	 *            The {@link AbstractFragmentationInfoBundle} instance.
	 */
	private static void makeCopies(AbstractFragmentationInfoBundle bundle) {

		Preconditions.checkNotNull(bundle,
				"Fragmentation info bundle must be not null!");
		Preconditions.checkNotNull(bundle.getOriginalRelevantParts(),
				"Relevant parts must be not null!");
		Preconditions.checkNotNull(bundle.getOriginalIrrelevantParts(),
				"Irrelevant parts must be not null!");

		// Copy the query parts
		final Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiedFragmentsToOriginals = LogicalQueryHelper
				.copyAndCutQueryParts(bundle.getOriginalRelevantParts(),
						bundle.getDegreeOfFragmentation());
		final Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiedNonFragmentsToOriginals = LogicalQueryHelper
				.copyAndCutQueryParts(bundle.getOriginalIrrelevantParts(), 1);

		// Put the maps together
		final Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOriginals = Maps
				.newHashMap();
		copiesToOriginals.putAll(copiedFragmentsToOriginals);
		copiesToOriginals.putAll(copiedNonFragmentsToOriginals);
		bundle.setCopyMap(copiesToOriginals);

		// Collect the copies of the start operator
		final Collection<ILogicalOperator> copiesOfStartOperator = AbstractFragmentationHelper
				.findCopies(bundle.getOriginStartOperator(),
						copiedNonFragmentsToOriginals);
		Preconditions
				.checkArgument(
						copiesOfStartOperator.size() == 1,
						"The copies of the start operator are not valid! There must be exactly one copy!");
		bundle.setCopiedStartOperators(copiesOfStartOperator);

		// Collect copies of the end operator
		Collection<ILogicalOperator> copiesOfEndOperator = Lists.newArrayList();
		if (bundle.getOriginEndOperator().isPresent()) {

			copiesOfEndOperator = AbstractFragmentationHelper.findCopies(bundle
					.getOriginEndOperator().get(),
					copiedNonFragmentsToOriginals);
			Preconditions
					.checkArgument(copiesOfEndOperator.size() == 1,
							"The copies of the end operator are not valid! There must be exactly one copy!");

		}
		bundle.setCopiedEndOperators(copiesOfEndOperator);

		// Check, if the degree of fragmentation is suitable for the number of
		// available peers
		if (!AbstractFragmentationQueryPartModificator
				.validateDegreeOfFragmentation(bundle)) {

			AbstractFragmentationQueryPartModificator.log
					.warn("Got not enough peers for a suitable fragmentation!");

		}

		bundle.setCopyMap(copiesToOriginals);

	}

	/**
	 * Creates the info bundle for the fragmentation.
	 * 
	 * @return A new instance of {@link AbstractFragmentationInfoBundle}.
	 */
	protected abstract AbstractFragmentationInfoBundle createFragmentationInfoBundle();

	/**
	 * Creates the helper for the fragmentation.
	 * 
	 * @param fragmentationParameters
	 *            The parameters for fragmentation.
	 * @return A new instance of {@link AbstractFragmentationHelper}.
	 */
	protected abstract AbstractFragmentationHelper createFragmentationHelper(
			List<String> modificatorParameters);

	@Override
	public Collection<ILogicalQueryPart> modify(
			Collection<ILogicalQueryPart> queryParts, ILogicalQuery query,
			QueryBuildConfiguration config, List<String> modificatorParameters)
			throws QueryPartModificationException {

		// The helper instance
		final AbstractFragmentationHelper helper = this
				.createFragmentationHelper(modificatorParameters);

		// The bundle of informations for the fragmentation
		final AbstractFragmentationInfoBundle bundle = this
				.createFragmentationInfoBundle();

		// Preconditions
		if (queryParts == null || queryParts.isEmpty()) {

			AbstractFragmentationQueryPartModificator.log
					.warn("No query parts given for fragmentation");
			return queryParts;

		}

		// 1. Preparation based on the query parts and parameters
		AbstractFragmentationQueryPartModificator.prepare(queryParts, helper,
				bundle);
		AbstractFragmentationQueryPartModificator.log.debug(
				"State of fragmentation after preparation:\n{}", bundle);

		// 2. Make loose working copies of the query parts
		AbstractFragmentationQueryPartModificator.makeCopies(bundle);
		AbstractFragmentationQueryPartModificator.log.debug(
				"State of fragmentation after making copies:\n{}", bundle);

		// TODO create map for inserted fragment operators
		// TODO create map for inserted reunion operators

		// TODO insert fragment operators
		// TODO insert union operators
		// TODO append other query parts
		// TODO set query parts to avoid
		// TODO create return value
		return queryParts;

	}

}