package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation;

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
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modification.ModificationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.rule.FragmentationRuleHelper;
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
public abstract class AbstractFragmentationQueryPartModificator implements IQueryPartModificator {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(AbstractFragmentationQueryPartModificator.class);

	/**
	 * Makes copies of the origin query parts as follows: <br />
	 * For every fragment, there will be as many copies made, as
	 * {@link FragmentationInfoBundle#getDegreeOfFragmentation()}. <br />
	 * For every other query part, there will be one copy made. <br />
	 * Each copy will be a cut off query part having o incoming or outgoing
	 * subscriptions. The result is available as
	 * {@link FragmentationInfoBundle#getCopyMap()}.
	 * 
	 * @param bundle
	 *            The {@link FragmentationInfoBundle} instance.
	 */
	private static void makeCopies(FragmentationInfoBundle bundle) {

		Preconditions.checkNotNull(bundle, "Fragmentation info bundle must be not null!");
		Preconditions.checkNotNull(bundle.getOriginalRelevantParts(), "Relevant parts must be not null!");
		Preconditions.checkNotNull(bundle.getOriginalIrrelevantParts(), "Irrelevant parts must be not null!");

		// Copy the query parts
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiedFragmentsToOriginals = LogicalQueryHelper.copyAndCutQueryParts(bundle.getOriginalRelevantParts(), bundle.getDegreeOfFragmentation());
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiedNonFragmentsToOriginals = LogicalQueryHelper.copyAndCutQueryParts(bundle.getOriginalIrrelevantParts(), 1);

		// Put the maps together
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOriginals = Maps.newHashMap();
		copiesToOriginals.putAll(copiedFragmentsToOriginals);
		copiesToOriginals.putAll(copiedNonFragmentsToOriginals);
		bundle.setCopyMap(copiesToOriginals);

		// Check, if the degree of fragmentation is suitable for the number of
		// available peers
		if (!ModificationHelper.validateDegreeOfModification(ModificationHelper.calcSize(bundle.getCopyMap().values()))) {

			AbstractFragmentationQueryPartModificator.LOG.warn("Got not enough peers for a suitable fragmentation!");

		}

	}

	/**
	 * Sets for a query part the parts to be avoided, which are part of a
	 * fragment.
	 * 
	 * @param originalPart
	 *            The given query part.
	 * @param fragmentOrReunionOperator
	 *            The fragment or reunion operator within
	 *            <code>originalPart</code>.
	 * @param bundle
	 *            The parameters for fragmentation.
	 * @param fragmentOperator
	 *            True, if <code>fragmentOrReunionOperator</code> is a fragment
	 *            operator. False if it's a reunion operator.
	 * @return The same collection as <code>parts</code> except the avoided
	 *         parts being set.
	 */
	private static Collection<ILogicalQueryPart> setFragmentsToAvoid(ILogicalQueryPart originalPart, ILogicalOperator fragmentOrReunionOperator, FragmentationInfoBundle bundle, boolean fragmentOperator) {

		Preconditions.checkNotNull(originalPart, "Querypart must be not null!");
		Preconditions.checkNotNull(fragmentOperator, "Fragment/reunion operator must be not null!");
		Preconditions.checkNotNull(bundle, "Fragmentation info bundle must be not null!");
		Preconditions.checkNotNull(bundle.getCopyMap(), "Copy map must be not null!");

		Collection<ILogicalQueryPart> avoidedParts = Lists.newArrayList();
		for (ILogicalQueryPart part : bundle.getCopyMap().keySet()) {

			if (part.equals(originalPart) || bundle.getCopyMap().get(part).size() == 1) {

				continue;

			}

			ILogicalQueryPart copiedPart = bundle.getCopyMap().get(part).iterator().next();
			for (ILogicalOperator operator : copiedPart.getOperators()) {

				if (fragmentOperator && ModificationHelper.isOperatorAbove(operator, fragmentOrReunionOperator)) {

					avoidedParts.addAll(bundle.getCopyMap().get(part));
					break;

				} else if (!fragmentOperator && ModificationHelper.isOperatorAbove(fragmentOrReunionOperator, operator)) {

					avoidedParts.addAll(bundle.getCopyMap().get(part));
					break;

				}

			}

		}

		return avoidedParts;

	}

	/**
	 * Sets for each query part the parts to be avoided. <br />
	 * A part being a fragment will set all other fragments, the fragmentation
	 * part and the reunion part to be avoided. <br />
	 * A part containing a fragment operator will set all fragments to be
	 * avoided. <br />
	 * A part containing a reunion operator will set all fragments to be
	 * avoided.
	 * 
	 * @param bundle
	 *            The parameters for fragmentation.
	 * @return The same collection as <code>parts</code> except the avoided
	 *         parts being set.
	 */
	private static Collection<ILogicalQueryPart> setQueryPartsToAvoid(FragmentationInfoBundle bundle) {

		Preconditions.checkNotNull(bundle, "Fragmentation info bundle must be not null!");
		Preconditions.checkNotNull(bundle.getCopyMap(), "Copy map must be not null!");

		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> avoidedMap = Maps.newHashMap();

		for (ILogicalQueryPart originalPart : bundle.getCopyMap().keySet().toArray(new ILogicalQueryPart[bundle.getCopyMap().keySet().size()])) {

			for (ILogicalQueryPart copiedPart : bundle.getCopyMap().get(originalPart)) {

				Collection<ILogicalQueryPart> avoidedParts = Lists.newArrayList();
				avoidedParts.addAll(bundle.getCopyMap().get(originalPart));
				avoidedParts.remove(copiedPart);

				for (ILogicalOperator fragmentOperator : bundle.getFragmentOperators()) {

					if (copiedPart.contains(fragmentOperator)) {

						Collection<ILogicalQueryPart> parts = AbstractFragmentationQueryPartModificator.setFragmentsToAvoid(originalPart, fragmentOperator, bundle, true);

						for (ILogicalQueryPart part : parts) {

							if (!avoidedParts.contains(part)) {

								avoidedParts.add(part);

							}

						}

					}

				}

				for (ILogicalOperator reunionOperator : bundle.getReunionOperators()) {

					if (copiedPart.contains(reunionOperator)) {

						Collection<ILogicalQueryPart> parts = AbstractFragmentationQueryPartModificator.setFragmentsToAvoid(originalPart, reunionOperator, bundle, false);

						for (ILogicalQueryPart part : parts) {

							if (!avoidedParts.contains(part)) {

								avoidedParts.add(part);

							}

						}

					}

				}

				avoidedMap.put(copiedPart, avoidedParts);
				AbstractFragmentationQueryPartModificator.LOG.debug("Set avoided parts for {}: {}", copiedPart, avoidedParts);

			}

		}

		for (ILogicalQueryPart part : avoidedMap.keySet().toArray(new ILogicalQueryPart[avoidedMap.keySet().size()])) {

			part.addAvoidingQueryParts(avoidedMap.get(part));

		}
		return avoidedMap.keySet();

	}

	/**
	 * Determines all operators to build fragments.
	 * 
	 * @param operators
	 *            The operators to check.
	 * @param helper
	 *            The {@link AbstractFragmentationParameterHelper} instance.
	 * @param bundle
	 *            The {@link FragmentationInfoBundle} instance.
	 * @return All operators within <code>operators</code> to build fragments.
	 */
	private Collection<ILogicalOperator> determineOperatorsForFragment(Collection<ILogicalOperator> operators, AbstractFragmentationParameterHelper helper, FragmentationInfoBundle bundle) {

		// Preconditions
		Preconditions.checkNotNull(operators, "List of operators must be not null!");
		Preconditions.checkNotNull(helper, "Fragmentation helper must be not null!");
		Preconditions.checkNotNull(bundle, "Fragmentation info bundle must be not null!");
		Preconditions.checkNotNull(bundle.getOriginStartOperator(), "The operator marking the start of fragmentation must be not null!");
		Preconditions.checkNotNull(bundle.getOriginEndOperator(), "The operator marking the end of fragmentation must be not null!");

		Collection<ILogicalOperator> operatorsForFragment = Lists.newArrayList();

		for (ILogicalOperator operator : operators) {

			if (ModificationHelper.isOperatorAbove(operator, bundle.getOriginStartOperator()) && (!bundle.getOriginEndOperator().isPresent() || ModificationHelper.isOperatorAbove(bundle.getOriginEndOperator().get(), operator)) && FragmentationRuleHelper.canOperatorBePartOfFragment(this, operator, helper)) {

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
	 *            The {@link AbstractFragmentationParameterHelper} instance.
	 * @param bundle
	 *            The {@link FragmentationInfoBundle} instance.
	 * @return All operators within <code>operators</code> to build fragments.
	 */
	private IPair<Collection<ILogicalQueryPart>, Collection<ILogicalQueryPart>> determineFragments(Collection<ILogicalQueryPart> queryParts, AbstractFragmentationParameterHelper helper, FragmentationInfoBundle bundle) {

		// Preconditions
		Preconditions.checkNotNull(queryParts, "List of query parts must be not null!");
		Preconditions.checkNotNull(!queryParts.isEmpty(), "List of query parts must be not empty!");

		Collection<ILogicalQueryPart> fragments = Lists.newArrayList();
		Collection<ILogicalQueryPart> nonfragments = Lists.newArrayList();

		for (ILogicalQueryPart queryPart : queryParts) {

			Collection<ILogicalOperator> operatorsForFragment = this.determineOperatorsForFragment(queryPart.getOperators(), helper, bundle);
			if (!operatorsForFragment.isEmpty()) {

				final ILogicalQueryPart fragment = new LogicalQueryPart(operatorsForFragment, queryPart.getAvoidingQueryParts());
				fragments.add(fragment);

			}

			if (operatorsForFragment.size() < queryPart.getOperators().size()) {

				Collection<ILogicalOperator> operatorsForNonfragment = Lists.newArrayList(queryPart.getOperators());
				operatorsForNonfragment.removeAll(operatorsForFragment);

				if (!operatorsForNonfragment.isEmpty()) {

					ILogicalQueryPart nonfragment = new LogicalQueryPart(operatorsForNonfragment, queryPart.getAvoidingQueryParts());
					nonfragments.add(nonfragment);

				}

			}

		}

		return new Pair<Collection<ILogicalQueryPart>, Collection<ILogicalQueryPart>>(fragments, nonfragments);

	}

	/**
	 * Makes preparations for fragmentation. <br />
	 * The degree of fragmentation, the operators marking start and end of
	 * fragmentation and the fragments are stored within<code>bundle</code>.
	 * 
	 * @param queryParts
	 *            The given query parts.
	 * @param helper
	 *            The {@link AbstractFragmentationParameterHelper} instance.
	 * @param bundle
	 *            The {@link FragmentationInfoBundle} instance.
	 * @return True, if <code>queryParts</code> and the informations given by
	 *         Odysseus-Script are valid for fragmentation.
	 * @throws QueryPartModificationException
	 *             If the parameters for fragmentation do not contain two
	 *             parameters, if the second parameter is no integer or if the
	 *             degree of fragmentation if lower than
	 *             {@link AbstractFragmentationParameterHelper#MIN_DEGREE_OF_FRAGMENTATION}
	 *             . <br />
	 *             If the parameters for fragmentation is empty or if the first
	 *             parameter does not match any patterns.<br />
	 *             If no query parts remain to build fragments.
	 */
	private void prepare(Collection<ILogicalQueryPart> queryParts, AbstractFragmentationParameterHelper helper, FragmentationInfoBundle bundle) throws QueryPartModificationException {

		// Preconditions
		Preconditions.checkNotNull(helper, "Fragmentation helper must be not null!");
		Preconditions.checkNotNull(bundle, "Fragmentation info bundle must be not null!");

		// Determine degree of fragmentation
		bundle.setDegreeOfFragmentation(helper.determineDegreeOfModification());

		// Determine identifiers for start point and end point for fragmentation
		final IPair<Optional<ILogicalOperator>, Optional<ILogicalOperator>> startAndendPointOfFragmentation = helper.determineStartAndEndPoints(queryParts);
		if (!startAndendPointOfFragmentation.getE1().isPresent()) {

			throw new QueryPartModificationException("No start of fragmentation given!");

		}
		bundle.setOriginStartOperator(startAndendPointOfFragmentation.getE1().get());
		bundle.setOriginEndOperator(startAndendPointOfFragmentation.getE2());

		// Determine query parts to be fragments and those to be outside of
		// fragments
		final IPair<Collection<ILogicalQueryPart>, Collection<ILogicalQueryPart>> fragmentsAndNonfragments = this.determineFragments(queryParts, helper, bundle);
		if (fragmentsAndNonfragments.getE1().isEmpty()) {

			throw new QueryPartModificationException("No query parts given to build fragments!");

		}
		bundle.setOriginalRelevantParts(fragmentsAndNonfragments.getE1());
		bundle.setOriginalIrrelevantParts(fragmentsAndNonfragments.getE2());

	}

	/**
	 * Processes a single subscription between query parts.
	 * 
	 * @param subscription
	 *            The subscription.
	 * @param originalSource
	 *            The relative source connected by <code>subscription</code>.
	 * @param originalPart
	 *            The query part containing <code>originalSource</code>.
	 * @param helper
	 *            The {@link AbstractFragmentationParameterHelper} instance.
	 * @param bundle
	 *            The {@link FragmentationInfoBundle} instance.
	 * @throws QueryPartModificationException
	 *             if the query part of the target of <code>subscription</code>
	 *             is unknown. <br />
	 *             May also be thrown by
	 *             {@link #needSpecialHandlingForQueryPart(ILogicalQueryPart, FragmentationInfoBundle)}
	 *             or
	 *             {@link #createFragmentOperator(int, FragmentationInfoBundle)}
	 *             .
	 */
	private void processSubscriptionFromRelativeSource(LogicalSubscription subscription, ILogicalOperator originalSource, ILogicalQueryPart originalPart, AbstractFragmentationParameterHelper helper, FragmentationInfoBundle bundle) throws QueryPartModificationException {

		Preconditions.checkNotNull(subscription, "Logical subscription must be not null!");
		Preconditions.checkNotNull(originalSource, "Logical source must be not null!");
		Preconditions.checkNotNull(originalPart, "Query part must be not null!");
		Preconditions.checkNotNull(helper, "Fragmentation helper must be not null!");
		Preconditions.checkNotNull(bundle, "Fragmentation info bundle must be not null!");
		Preconditions.checkNotNull(bundle.getCopyMap(), "Copy map must be not null!");
		Preconditions.checkNotNull(bundle.getOriginStartOperator(), "The origin start operator must be not null!");

		ILogicalOperator originalTarget = subscription.getTarget();
		Collection<ILogicalOperator> copiedTargets = Lists.newArrayList();
		Collection<ILogicalOperator> copiedSources = ModificationHelper.findCopies(originalSource, bundle.getCopyMap());
		Optional<ILogicalQueryPart> optPartOfOriginalTarget = LogicalQueryHelper.determineQueryPart(bundle.getCopyMap().keySet(), originalTarget);
		if (!optPartOfOriginalTarget.isPresent()) {
			throw new QueryPartModificationException("Unknown query part of operator " + originalTarget);
		} 
		copiedTargets.addAll(ModificationHelper.findCopies(originalTarget, bundle.getCopyMap()));

		if (originalPart.getOperators().contains(originalTarget)) {

			// Target and source are within the same query part
			return;

		} else if (!ModificationHelper.isOperatorAboveOrEqual(originalTarget, bundle.getOriginStartOperator())) {

			// Target is not connected (via other operators) to source for
			// fragmentation
			// Copies will be connected directly
			ModificationHelper.connectOperators(copiedSources, copiedTargets, subscription);
			AbstractFragmentationQueryPartModificator.LOG.debug("Connected {} and {}", originalSource, originalTarget);

		} else {

			// Target is connected (via other operators) to source for
			// fragmentation

			Collection<ILogicalQueryPart> partsOfCopiedSource = bundle.getCopyMap().get(originalPart);
			ILogicalQueryPart partOfOriginalTarget = optPartOfOriginalTarget.get();
			Collection<ILogicalQueryPart> partsOfCopiedTarget = bundle.getCopyMap().get(partOfOriginalTarget);

			if (partsOfCopiedSource.size() == 1 && partsOfCopiedTarget.size() == 1) {

				// 1:1 relationship
				// No need to fragment
				ModificationHelper.connectOperators(copiedSources, copiedTargets, subscription);
				AbstractFragmentationQueryPartModificator.LOG.debug("Connected {} and {}", originalSource, originalTarget);

			} else if (partsOfCopiedSource.size() == 1 && partsOfCopiedTarget.size() > 1) {

				// N:1 relationship
				if (FragmentationRuleHelper.needSpecialHandlingForQueryPart(partOfOriginalTarget, this, helper)) {

					// Need special handling
					this.processSpecialHandling(originalTarget, copiedSources, copiedTargets, Optional.of(subscription), Optional.of(originalPart), partsOfCopiedSource, partOfOriginalTarget, partsOfCopiedTarget, helper, bundle);

				} else {

					// Need to reunion
					ILogicalOperator reunionOperator = this.insertReunionOperator(originalTarget, Optional.of(copiedSources.iterator().next()), copiedTargets, Optional.of(subscription), Optional.of(originalPart), Optional.of(partsOfCopiedSource.iterator().next()), bundle);
					bundle.addReunionOperator(reunionOperator);
					AbstractFragmentationQueryPartModificator.LOG.debug("Inserted a reunion operator after {}", originalTarget);

				}

			} else if (partsOfCopiedSource.size() > 1 && partsOfCopiedTarget.size() == 1) {

				// 1:N relationship
				// Need to fragment
				ILogicalOperator fragmentOperator = this.insertFragmentOperator(copiedSources, copiedTargets.iterator().next(), partOfOriginalTarget, partsOfCopiedTarget.iterator().next(), subscription, bundle);
				bundle.addFragmentOperator(fragmentOperator);
				AbstractFragmentationQueryPartModificator.LOG.debug("Inserted a fragment operator below {}", originalSource);

			} else {

				// N:N relationship
				if (FragmentationRuleHelper.needSpecialHandlingForQueryPart(partOfOriginalTarget, this, helper)) {

					// Need special handling
					this.processSpecialHandling(originalTarget, copiedSources, copiedTargets, Optional.of(subscription), Optional.of(originalPart), partsOfCopiedSource, partOfOriginalTarget, partsOfCopiedTarget, helper, bundle);

				} else {

					// No need to fragment
					ModificationHelper.connectOperators(copiedSources, copiedTargets, subscription);
					AbstractFragmentationQueryPartModificator.LOG.debug("Connected {} and {}", originalSource, originalTarget);

				}

			}

		}

	}

	/**
	 * Processes a single relative source of a query part.
	 * 
	 * @param originalSource
	 *            The relative source.
	 * @param originalPart
	 *            The query part containing <code>originalSource</code>.
	 * @param helper
	 *            The {@link AbstractFragmentationParameterHelper} instance.
	 * @param bundle
	 *            The {@link FragmentationInfoBundle} instance.
	 * @throws QueryPartModificationException
	 *             if the query part of the target of an incoming subscription
	 *             to <code>originalSource</code> is unknown. <br />
	 *             May also be thrown by
	 *             {@link #needSpecialHandlingForQueryPart(ILogicalQueryPart, FragmentationInfoBundle)}
	 *             .
	 */
	private void processRelativeSource(ILogicalOperator originalSource, ILogicalQueryPart originalPart, AbstractFragmentationParameterHelper helper, FragmentationInfoBundle bundle) throws QueryPartModificationException {

		Preconditions.checkNotNull(originalSource, "Logical source must be not null!");
		Preconditions.checkNotNull(originalPart, "Query part must be not null!");
		Preconditions.checkNotNull(helper, "Fragmentation helper must be not null!");
		Preconditions.checkNotNull(bundle, "Fragmentation info bundle must be not null!");

		if (!originalSource.getSubscribedToSource().isEmpty()) {

			for (LogicalSubscription subToSource : originalSource.getSubscribedToSource()) {

				this.processSubscriptionFromRelativeSource(subToSource, originalSource, originalPart, helper, bundle);

			}

		}

	}

	/**
	 * Processes a single real sink of a query part.
	 * 
	 * @param originalSink
	 *            The real sink.
	 * @param originalPart
	 *            The query part containing <code>originalSink</code>.
	 * @param helper
	 *            The {@link AbstractFragmentationParameterHelper} instance.
	 * @param bundle
	 *            The {@link FragmentationInfoBundle} instance.
	 * @throws QueryPartModificationException
	 *             may be thrown by
	 *             {@link #createReunionOperator(int, FragmentationInfoBundle)}.
	 */
	private void processRealSink(ILogicalOperator originalSink, ILogicalQueryPart originalPart, AbstractFragmentationParameterHelper helper, FragmentationInfoBundle bundle) throws QueryPartModificationException {

		Preconditions.checkNotNull(originalSink, "Logical sink must be not null!");
		Preconditions.checkNotNull(originalPart, "Query part must be not null!");
		Preconditions.checkNotNull(helper, "Fragmentation helper must be not null!");
		Preconditions.checkNotNull(bundle, "Fragmentation info bundle must be not null!");
		Preconditions.checkNotNull(bundle.getCopyMap(), "Copy map must be not null!");

		Optional<ILogicalOperator> source = Optional.absent();
		Collection<ILogicalOperator> copiedSources = Lists.newArrayList();
		Collection<ILogicalOperator> copiedSinks = ModificationHelper.findCopies(originalSink, bundle.getCopyMap());
		Optional<ILogicalQueryPart> partOfSource = Optional.absent();
		Collection<ILogicalQueryPart> partsOfCopiedSources = Lists.newArrayList();
		Collection<ILogicalQueryPart> partsOfCopiedSink = bundle.getCopyMap().get(originalPart);
		Optional<LogicalSubscription> subscription = Optional.absent();

		if (copiedSinks.size() == 1) {

			// Nothing to do
			return;

		}

		if (FragmentationRuleHelper.needSpecialHandlingForQueryPart(originalPart, this, helper)) {

			this.processSpecialHandling(originalSink, copiedSources, copiedSinks, subscription, partOfSource, partsOfCopiedSources, originalPart, partsOfCopiedSink, helper, bundle);

		} else {

			// Need to reunion
			ILogicalOperator reunionOperator = this.insertReunionOperator(originalSink, source, ModificationHelper.findCopies(originalSink, bundle.getCopyMap()), subscription, partOfSource, Optional.of(originalPart), bundle);
			bundle.addReunionOperator(reunionOperator);
			AbstractFragmentationQueryPartModificator.LOG.debug("Inserted a reunion operator after {}", originalSink);

		}

	}

	/**
	 * Modifies a single query part (e.g. insertion of fragment and/or reunion
	 * operators, if needed.
	 * 
	 * @param part
	 *            The given query part.
	 * @param helper
	 *            The {@link AbstractFragmentationParameterHelper} instance.
	 * @param bundle
	 *            The {@link FragmentationInfoBundle} instance.
	 * @throws QueryPartModificationException
	 *             if the query part of the target of an incoming subscription
	 *             to a relative source of <code>part</code> is unknown. <br />
	 *             May also be thrown by
	 *             {@link #needSpecialHandlingForQueryPart(ILogicalQueryPart, FragmentationInfoBundle)}
	 *             .
	 */
	private void modify(ILogicalQueryPart part, AbstractFragmentationParameterHelper helper, FragmentationInfoBundle bundle) throws QueryPartModificationException {

		Preconditions.checkNotNull(part, "Query part must be not null!");
		Preconditions.checkNotNull(helper, "Fragmentation helper must be not null!");
		Preconditions.checkNotNull(bundle, "Fragmentation info bundle must be not null!");

		// 2. Insert fragment and reunion operators
		// 3. Attach all query parts not to build fragments to the modified
		// fragments.

		// Process each relative source, which is relevant for fragmentation
		for (ILogicalOperator originalSource : LogicalQueryHelper.getRelativeSourcesOfLogicalQueryPart(part)) {

			this.processRelativeSource(originalSource, part, helper, bundle);

		}

		// Process each real sink, which is relevant for fragmentation
		for (ILogicalOperator originalSink : LogicalQueryHelper.getRelativeSinksOfLogicalQueryPart(part)) {

			if (originalSink.getSubscriptions().isEmpty()) {

				this.processRealSink(originalSink, part, helper, bundle);

			}

		}

	}

	/**
	 * Inserts a new fragment operator between a single target and multiple
	 * copies of a relative source.
	 * 
	 * @param copiedSources
	 *            The given copies of a relative source.
	 * @param copiedTarget
	 *            The given target.
	 * @param partOfOriginalTarget
	 *            The query part of the original target.
	 * @param partOfCopiedTarget
	 *            The query part of the given target
	 * @param subscription
	 *            The original subscription between the original target and the
	 *            original relative source.
	 * @param bundle
	 *            The {@link FragmentationInfoBundle} instance.
	 * @return The inserted fragment operator.
	 * @throws QueryPartModificationException
	 *             may be thrown by
	 *             {@link #createFragmentOperator(int, FragmentationInfoBundle)}
	 *             .
	 */
	protected ILogicalOperator insertFragmentOperator(Collection<ILogicalOperator> copiedSources, ILogicalOperator copiedTarget, ILogicalQueryPart partOfOriginalTarget, ILogicalQueryPart partOfCopiedTarget, LogicalSubscription subscription, FragmentationInfoBundle bundle) throws QueryPartModificationException {

		Preconditions.checkNotNull(subscription, "Logical subscription must be not null!");
		Preconditions.checkNotNull(copiedSources, "Copied logical sources must be not null!");
		Preconditions.checkNotNull(copiedTarget, "Copied logical target must be not null!");
		Preconditions.checkNotNull(partOfOriginalTarget, "Part of original logical target must be not null!");
		Preconditions.checkNotNull(partOfCopiedTarget, "Part of copied logical target must be not null!");
		Preconditions.checkNotNull(bundle, "Fragmentation info bundle must be not null!");
		Preconditions.checkNotNull(bundle.getCopyMap(), "Copy map must be not null!");

		// Create fragment operator
		ILogicalOperator fragmentOperator = this.createFragmentOperator(copiedSources.size(), bundle);

		// Subscribe the fragment operator for fragmentation to the sources
		for (int sourceNo = 0; sourceNo < copiedSources.size(); sourceNo++) {

			ILogicalOperator copiedSource = ((List<ILogicalOperator>) copiedSources).get(sourceNo);
			fragmentOperator.subscribeSink(copiedSource, subscription.getSinkInPort(), sourceNo, subscription.getSchema());

		}

		// Subscribe the fragment operator for fragmentation to the target
		fragmentOperator.subscribeToSource(copiedTarget, 0, subscription.getSourceOutPort(), subscription.getSchema());

		// Change the query part for the operator for fragmentation
		Collection<ILogicalOperator> operatorsWithFragment = Lists.newArrayList(partOfCopiedTarget.getOperators());
		operatorsWithFragment.add(fragmentOperator);
		Collection<ILogicalQueryPart> modifiedQueryParts = Lists.newArrayList();
		ILogicalQueryPart fragmentPart = new LogicalQueryPart(operatorsWithFragment);
		modifiedQueryParts.add(fragmentPart);
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copyMap = bundle.getCopyMap();
		copyMap.put(partOfOriginalTarget, modifiedQueryParts);
		bundle.setCopyMap(copyMap);

		return fragmentOperator;

	}

	/**
	 * Inserts a new reunion operator between multiple copies of a target and a
	 * single relative source or as a real sink after multiple copies of a
	 * target.
	 * 
	 * @param originalTarget
	 *            the given original target.
	 * @param copiedSource
	 *            The given relative source, if present.
	 * @param copiedTargets
	 *            The given copies of a target.
	 * @param subscription
	 *            The original subscription between the original target and the
	 *            original relative source, if present.
	 * @param partOfOriginalSource
	 *            The query part containing the original source, if present.
	 * @param partOfOCopiedSource
	 *            The query part containing the copied source, if present.
	 * @param bundle
	 *            The {@link FragmentationInfoBundle} instance.
	 * @return The inserted reunion operator.
	 * @throws QueryPartModificationException
	 *             may be thrown by
	 *             {@link #createReunionOperator(int, FragmentationInfoBundle)}.
	 */
	protected ILogicalOperator insertReunionOperator(ILogicalOperator originalTarget, Optional<ILogicalOperator> copiedSource, Collection<ILogicalOperator> copiedTargets, Optional<LogicalSubscription> subscription, Optional<ILogicalQueryPart> partOfOriginalSource, Optional<ILogicalQueryPart> partOfCopiedSource, FragmentationInfoBundle bundle) throws QueryPartModificationException {

		Preconditions.checkNotNull(originalTarget, "Original logical target must be not null!");
		Preconditions.checkNotNull(copiedTargets, "Copied logical targets must be not null!");
		Preconditions.checkNotNull(bundle, "Fragmentation info bundle must be not null!");
		Preconditions.checkNotNull(bundle.getCopyMap(), "Copy map must be not null!");

		// Create reunion operator
		ILogicalOperator reunionOperator = this.createReunionOperator(copiedTargets.size(), bundle);

		int sinkInPort = 0;
		int sourceOutPort = 0;
		SDFSchema schema = originalTarget.getOutputSchema();
		if (subscription.isPresent()) {

			sourceOutPort = subscription.get().getSourceOutPort();
			sinkInPort = subscription.get().getSinkInPort();
			schema = subscription.get().getSchema();

		}

		// Subscribe the reunion operator to the copied targets
		for (int sinkNo = 0; sinkNo < copiedTargets.size(); sinkNo++) {

			ILogicalOperator copiedTarget = ((List<ILogicalOperator>) copiedTargets).get(sinkNo);
			copiedTarget.subscribeSink(reunionOperator, sinkNo, sourceOutPort, schema);

		}

		// Subscribe the copied relative source to the operator for reunion
		if (copiedSource.isPresent()) {

			reunionOperator.subscribeSink(copiedSource.get(), sinkInPort, 0, schema);

		}

		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copyMap = bundle.getCopyMap();

		if (copiedSource.isPresent() && partOfOriginalSource.isPresent() && partOfCopiedSource.isPresent()) {

			// Create modified query part
			Collection<ILogicalOperator> operatorsWithReunion = Lists.newArrayList(partOfCopiedSource.get().getOperators());
			operatorsWithReunion.add(reunionOperator);
			Collection<ILogicalQueryPart> modifiedQueryParts = Lists.newArrayList();
			ILogicalQueryPart reunionPart = new LogicalQueryPart(operatorsWithReunion);
			modifiedQueryParts.add(reunionPart);
			copyMap.put(partOfOriginalSource.get(), modifiedQueryParts);

		} else {

			// Create the query part for the operator for reunion
			ILogicalQueryPart reunionPart = new LogicalQueryPart(reunionOperator);
			Collection<ILogicalQueryPart> copiesOfReunionPart = Lists.newArrayList(reunionPart);
			copyMap.put(reunionPart, copiesOfReunionPart);

		}

		bundle.setCopyMap(copyMap);
		return reunionOperator;

	}

	/**
	 * Creates the helper for the fragmentation.
	 * 
	 * @param fragmentationParameters
	 *            The parameters for fragmentation.
	 * @return A new instance of {@link AbstractFragmentationParameterHelper}.
	 */
	protected abstract AbstractFragmentationParameterHelper createFragmentationHelper(List<String> fragmentationParameters);

	/**
	 * Creates the information bundle for the fragmentation.
	 * 
	 * @param helper
	 *            The {@link AbstractFragmentationParameterHelper} instance.
	 * @return A new instance of {@link FragmentationInfoBundle}.
	 */
	protected abstract FragmentationInfoBundle createFragmentationInfoBundle(AbstractFragmentationParameterHelper helper);

	/**
	 * Special handling for a query part (e.g., the usage of partial
	 * aggregates).
	 * 
	 * @param originalTarget
	 *            the given original target.
	 * @param copiedSources
	 *            The given copies of the relative source, if present.
	 * @param copiedTargets
	 *            The given copies of a target.
	 * @param subscription
	 *            The original subscription between the original target and the
	 *            original relative source, if present.
	 * @param partOfOriginalSource
	 *            The query part containing the original source, if present.
	 * @param partsOfOCopiedSource
	 *            The query parts containing the copied sources, if present.
	 * @param partOfOriginalTarget
	 *            The query part containing the original target.
	 * @param partsOfOCopiedTarget
	 *            The query parts containing the copied targets.
	 * @param helper
	 *            The {@link AbstractFragmentationParameterHelper} instance.
	 * @param bundle
	 *            The {@link FragmentationInfoBundle} instance.
	 * @throws QueryPartModificationException
	 *             may be thrown by
	 *             {@link #createReunionOperator(int, FragmentationInfoBundle)}.
	 */
	protected abstract void processSpecialHandling(ILogicalOperator originalTarget, Collection<ILogicalOperator> copiedSources, Collection<ILogicalOperator> copiedTargets, Optional<LogicalSubscription> subscription, Optional<ILogicalQueryPart> partOfOriginalSource, Collection<ILogicalQueryPart> partsOfCopiedSource, ILogicalQueryPart partOfOriginalTarget,
			Collection<ILogicalQueryPart> partsOfCopiedTargets, AbstractFragmentationParameterHelper helper, FragmentationInfoBundle bundle) throws QueryPartModificationException;

	/**
	 * Creates a new fragment operator.
	 * 
	 * @param numFragments
	 *            The number of fragments.
	 * @param bundle
	 *            The {@link FragmentationInfoBundle} instance.
	 * @return A new instance of a fragment operator.
	 * @throws QueryPartModificationException
	 *             if any error occurs.
	 */
	protected abstract ILogicalOperator createFragmentOperator(int numFragments, FragmentationInfoBundle bundle) throws QueryPartModificationException;

	/**
	 * Creates a new reunion operator.
	 * 
	 * @param numFragments
	 *            The number of fragments.
	 * @param bundle
	 *            The {@link FragmentationInfoBundle} instance.
	 * @return A new instance of a reunion operator.
	 * @throws QueryPartModificationException
	 *             if any error occurs.
	 */
	protected abstract ILogicalOperator createReunionOperator(int numFragments, FragmentationInfoBundle bundle) throws QueryPartModificationException;

	@Override
	public Collection<ILogicalQueryPart> modify(Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, QueryBuildConfiguration config, List<String> modificatorParameters) throws QueryPartModificationException {

		// The helper instance
		AbstractFragmentationParameterHelper helper = this.createFragmentationHelper(modificatorParameters);

		// The bundle of informations for the fragmentation
		FragmentationInfoBundle bundle = this.createFragmentationInfoBundle(helper);

		// Preconditions
		if (queryParts == null || queryParts.isEmpty()) {

			AbstractFragmentationQueryPartModificator.LOG.warn("No query parts given for fragmentation");
			return queryParts;

		}

		// Preparation based on the query parts and parameters
		this.prepare(queryParts, helper, bundle);
		AbstractFragmentationQueryPartModificator.LOG.debug("State of fragmentation after preparation:\n{}", bundle);

		// 1. Make loose working copies of the query parts
		AbstractFragmentationQueryPartModificator.makeCopies(bundle);
		AbstractFragmentationQueryPartModificator.LOG.debug("State of fragmentation after making copies:\n{}", bundle);

		// Process each query part
		// Need of an array to iterate over due to possible changes of the copy
		// map
		for (ILogicalQueryPart part : bundle.getCopyMap().keySet().toArray(new ILogicalQueryPart[bundle.getCopyMap().keySet().size()])) {

			this.modify(part, helper, bundle);
			AbstractFragmentationQueryPartModificator.LOG.debug("State of fragmentation after processing {}:\n{}", part, bundle);

		}

		// Create the return value and avoid parts
		Collection<ILogicalQueryPart> resultingParts = AbstractFragmentationQueryPartModificator.setQueryPartsToAvoid(bundle);
		LogicalQueryHelper.initializeOperators(resultingParts);
		AbstractFragmentationQueryPartModificator.LOG.debug("Resulting query parts: {}", resultingParts);
		return resultingParts;

	}

}