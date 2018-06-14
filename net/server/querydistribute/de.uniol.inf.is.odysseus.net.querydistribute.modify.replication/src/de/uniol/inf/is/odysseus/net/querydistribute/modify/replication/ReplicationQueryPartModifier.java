package de.uniol.inf.is.odysseus.net.querydistribute.modify.replication;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.net.querydistribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.net.querydistribute.modify.ModificationHelper;
import de.uniol.inf.is.odysseus.net.querydistribute.modify.replication.rule.ReplicationRuleHelper;
import de.uniol.inf.is.odysseus.net.querydistribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.server.replication.logicaloperator.ReplicationMergeAO;

/**
 * A modifier of {@link ILogicalQueryPart}s, which replicates query parts and
 * inserts operators to merge the result sets of the replicates for each
 * relative sink within every single query part. <br />
 * Usage in Odysseus Script: <br />
 * #NODE_MODIFICATION replication &lt;number of replicates&gt;
 * 
 * @author Michael Brand
 */
public class ReplicationQueryPartModifier implements IQueryPartModificator {
	
	private static final long serialVersionUID = 2375679860615607859L;
	
	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ReplicationQueryPartModifier.class);

	@Override
	public String getName() {

		return "replication";

	}

	@Override
	public Collection<ILogicalQueryPart> modify(Collection<ILogicalQueryPart> queryParts, ILogicalQuery query,
			QueryBuildConfiguration config, List<String> modificatorParameters) throws QueryPartModificationException {

		// The helper instance
		ReplicationParameterHelper helper = new ReplicationParameterHelper(modificatorParameters);

		// The bundle of informations for the fragmentation
		ReplicationInfoBundle bundle = new ReplicationInfoBundle();

		// Preconditions
		if (queryParts == null || queryParts.isEmpty()) {

			ReplicationQueryPartModifier.LOG.warn("No query parts given for fragmentation");
			return queryParts;

		}

		// Preparation based on the query parts and parameters
		this.prepare(queryParts, helper, bundle);
		ReplicationQueryPartModifier.LOG.debug("State of replication after preparation:\n{}", bundle);

		// 1. Make loose working copies of the query parts
		ReplicationQueryPartModifier.makeCopies(bundle);
		ReplicationQueryPartModifier.LOG.debug("State of replication after making copies:\n{}", bundle);

		// Process each query part
		// Need of an array to iterate over due to possible changes of the copy
		// map
		for (ILogicalQueryPart part : bundle.getCopyMap().keySet()
				.toArray(new ILogicalQueryPart[bundle.getCopyMap().keySet().size()])) {

			ReplicationQueryPartModifier.modify(part, helper, bundle);
			ReplicationQueryPartModifier.LOG.debug("State of replication after processing {}:\n{}", part, bundle);

		}

		// Create the return value and avoid parts
		Collection<ILogicalQueryPart> resultingParts = ReplicationQueryPartModifier.setQueryPartsToAvoid(bundle);
		LogicalQueryHelper.initializeOperators(resultingParts);
		ReplicationQueryPartModifier.LOG.debug("Resulting query parts: {}", resultingParts);
		return resultingParts;

	}

	/**
	 * Sets for each query part the parts to be avoided. <br />
	 * A part being replicated will set all other replicates and the merge part
	 * to be avoided. <br />
	 * A part containing a merge operator will set all replicates to be avoided.
	 * 
	 * @param bundle
	 *            The {@link ReplicationInfoBundle} instance.
	 * @return The same collection as <code>parts</code> except the avoided
	 *         parts being set.
	 */
	private static Collection<ILogicalQueryPart> setQueryPartsToAvoid(ReplicationInfoBundle bundle) {

		Preconditions.checkNotNull(bundle, "Replication info bundle must be not null!");
		Preconditions.checkNotNull(bundle.getCopyMap(), "Copy map must be not null!");

		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> avoidedMap = Maps.newHashMap();

		for (ILogicalQueryPart originalPart : bundle.getCopyMap().keySet()
				.toArray(new ILogicalQueryPart[bundle.getCopyMap().keySet().size()])) {

			for (ILogicalQueryPart copiedPart : bundle.getCopyMap().get(originalPart)) {

				Collection<ILogicalQueryPart> avoidedParts = Lists.newArrayList();
				avoidedParts.addAll(bundle.getCopyMap().get(originalPart));
				avoidedParts.remove(copiedPart);

				for (ILogicalOperator mergeOperator : bundle.getMergeOperators()) {

					if (copiedPart.contains(mergeOperator)) {

						Collection<ILogicalQueryPart> parts = ReplicationQueryPartModifier
								.setReplicatesToAvoid(originalPart, mergeOperator, bundle);

						for (ILogicalQueryPart part : parts) {

							if (!avoidedParts.contains(part)) {

								avoidedParts.add(part);

							}

						}

					}

				}

				avoidedMap.put(copiedPart, avoidedParts);
				ReplicationQueryPartModifier.LOG.debug("Set avoided parts for {}: {}", copiedPart, avoidedParts);

			}

		}

		for (ILogicalQueryPart part : avoidedMap.keySet().toArray(new ILogicalQueryPart[avoidedMap.keySet().size()])) {

			part.addAvoidingQueryParts(avoidedMap.get(part));

		}
		return avoidedMap.keySet();

	}

	/**
	 * Sets for a query part the parts to be avoided, which are replicated.
	 * 
	 * @param originalPart
	 *            The given query part.
	 * @param mergeOperator
	 *            The merge operator within <code>originalPart</code>.
	 * @param bundle
	 *            The {@link ReplicationInfoBundle} instance.
	 * @return The same collection as <code>parts</code> except the avoided
	 *         parts being set.
	 */
	private static Collection<ILogicalQueryPart> setReplicatesToAvoid(ILogicalQueryPart originalPart,
			ILogicalOperator mergeOperator, ReplicationInfoBundle bundle) {

		Preconditions.checkNotNull(originalPart, "Querypart must be not null!");
		Preconditions.checkNotNull(mergeOperator, "Merge operator must be not null!");
		Preconditions.checkNotNull(bundle, "Replication info bundle must be not null!");
		Preconditions.checkNotNull(bundle.getCopyMap(), "Copy map must be not null!");

		Collection<ILogicalQueryPart> avoidedParts = Lists.newArrayList();
		for (ILogicalQueryPart part : bundle.getCopyMap().keySet()) {

			if (part.equals(originalPart) || bundle.getCopyMap().get(part).size() == 1) {

				continue;

			}

			ILogicalQueryPart copiedPart = bundle.getCopyMap().get(part).iterator().next();
			for (ILogicalOperator operator : copiedPart.getOperators()) {

				if (ModificationHelper.isOperatorAbove(mergeOperator, operator)) {

					avoidedParts.addAll(bundle.getCopyMap().get(part));
					break;

				}

			}

		}

		return avoidedParts;

	}

	/**
	 * Modifies a single query part (e.g. insertion of merge operators), if
	 * needed.
	 * 
	 * @param part
	 *            The given query part.
	 * @param helper
	 *            The {@link ReplicationParameterHelper} instance.
	 * @param bundle
	 *            The {@link ReplicationInfoBundle} instance.
	 * @throws QueryPartModificationException
	 *             if the query part of the target of an incoming subscription
	 *             to a relative source of <code>part</code> is unknown.
	 */
	private static void modify(ILogicalQueryPart part, ReplicationParameterHelper helper, ReplicationInfoBundle bundle)
			throws QueryPartModificationException {

		Preconditions.checkNotNull(part, "Query part must be not null!");
		Preconditions.checkNotNull(helper, "Replication helper must be not null!");
		Preconditions.checkNotNull(bundle, "Replication info bundle must be not null!");

		// 2. Insert merge operators
		// 3. Attach all query parts not to replicate to the replicated ones.

		// Process each relative source, which is relevant for replication
		for (ILogicalOperator originalSource : LogicalQueryHelper.getRelativeSourcesOfLogicalQueryPart(part)) {

			ReplicationQueryPartModifier.processRelativeSource(originalSource, part, helper, bundle);

		}

		// Process each real sink, which is relevant for replication
		for (ILogicalOperator originalSink : LogicalQueryHelper.getRelativeSinksOfLogicalQueryPart(part)) {

			if (originalSink.getSubscriptions().isEmpty()) {

				ReplicationQueryPartModifier.processRealSink(originalSink, part, helper, bundle);

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
	 *            The {@link ReplicationParameterHelper} instance.
	 * @param bundle
	 *            The {@link ReplicationInfoBundle} instance.
	 */
	private static void processRealSink(ILogicalOperator originalSink, ILogicalQueryPart originalPart,
			ReplicationParameterHelper helper, ReplicationInfoBundle bundle) {

		Preconditions.checkNotNull(originalSink, "Logical sink must be not null!");
		Preconditions.checkNotNull(originalPart, "Query part must be not null!");
		Preconditions.checkNotNull(helper, "Replication helper must be not null!");
		Preconditions.checkNotNull(bundle, "Replication info bundle must be not null!");
		Preconditions.checkNotNull(bundle.getCopyMap(), "Copy map must be not null!");

		Optional<ILogicalOperator> source = Optional.absent();
		Collection<ILogicalOperator> copiedSinks = ModificationHelper.findCopies(originalSink, bundle.getCopyMap());
		Optional<ILogicalQueryPart> partOfSource = Optional.absent();
		Optional<LogicalSubscription> subscription = Optional.absent();

		if (copiedSinks.size() == 1) {

			// Nothing to do
			return;

		}

		// Need to merge
		ILogicalOperator mergeOperator = ReplicationQueryPartModifier.insertMergeOperator(originalSink, source,
				ModificationHelper.findCopies(originalSink, bundle.getCopyMap()), subscription, partOfSource,
				Optional.of(originalPart), bundle);
		bundle.addMergeOperator(mergeOperator);
		ReplicationQueryPartModifier.LOG.debug("Inserted a merge operator after {}", originalSink);

	}

	/**
	 * Processes a single relative source of a query part.
	 * 
	 * @param originalSource
	 *            The relative source.
	 * @param originalPart
	 *            The query part containing <code>originalSource</code>.
	 * @param helper
	 *            The {@link ReplicationParameterHelper} instance.
	 * @param bundle
	 *            The {@link ReplicationInfoBundle} instance.
	 * @throws QueryPartModificationException
	 *             if the query part of the target of an incoming subscription
	 *             to <code>originalSource</code> is unknown.
	 */
	private static void processRelativeSource(ILogicalOperator originalSource, ILogicalQueryPart originalPart,
			ReplicationParameterHelper helper, ReplicationInfoBundle bundle) throws QueryPartModificationException {

		Preconditions.checkNotNull(originalSource, "Logical source must be not null!");
		Preconditions.checkNotNull(originalPart, "Query part must be not null!");
		Preconditions.checkNotNull(helper, "Replication helper must be not null!");
		Preconditions.checkNotNull(bundle, "Replication info bundle must be not null!");

		if (!originalSource.getSubscribedToSource().isEmpty()) {

			for (LogicalSubscription subToSource : originalSource.getSubscribedToSource()) {

				ReplicationQueryPartModifier.processSubscriptionFromRelativeSource(subToSource, originalSource,
						originalPart, helper, bundle);

			}

		}

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
	 *            The {@link ReplicationParameterHelper} instance.
	 * @param bundle
	 *            The {@link ReplicationInfoBundle} instance.
	 * @throws QueryPartModificationException
	 *             if the query part of the target of <code>subscription</code>
	 *             is unknown.
	 */
	private static void processSubscriptionFromRelativeSource(LogicalSubscription subscription,
			ILogicalOperator originalSource, ILogicalQueryPart originalPart, ReplicationParameterHelper helper,
			ReplicationInfoBundle bundle) throws QueryPartModificationException {

		Preconditions.checkNotNull(subscription, "Logical subscription must be not null!");
		Preconditions.checkNotNull(originalSource, "Logical source must be not null!");
		Preconditions.checkNotNull(originalPart, "Query part must be not null!");
		Preconditions.checkNotNull(helper, "Replication helper must be not null!");
		Preconditions.checkNotNull(bundle, "Replication info bundle must be not null!");
		Preconditions.checkNotNull(bundle.getCopyMap(), "Copy map must be not null!");
		Preconditions.checkNotNull(bundle.getOriginStartOperator(), "The origin start operator must be not null!");

		ILogicalOperator originalTarget = subscription.getSource();
		Collection<ILogicalOperator> copiedTargets = Lists.newArrayList();
		ImmutableList<ILogicalOperator> copiedSources = ImmutableList
				.copyOf(ModificationHelper.findCopies(originalSource, bundle.getCopyMap()));
		Optional<ILogicalQueryPart> optPartOfOriginalTarget = LogicalQueryHelper
				.determineQueryPart(bundle.getCopyMap().keySet(), originalTarget);
		if (!optPartOfOriginalTarget.isPresent()) {
			throw new QueryPartModificationException("Unknown query part of operator " + originalTarget);
		}
		copiedTargets.addAll(ModificationHelper.findCopies(originalTarget, bundle.getCopyMap()));

		if (originalPart.getOperators().contains(originalTarget)) {

			// Target and source are within the same query part
			return;

		} else if (bundle.getOriginStartOperator().isPresent()
				&& !ModificationHelper.isOperatorAboveOrEqual(originalTarget, bundle.getOriginStartOperator().get())) {

			// Target is not connected (via other operators) to source for
			// replication
			// Copies will be connected directly
			ModificationHelper.connectOperators(copiedSources, copiedTargets, subscription);
			ReplicationQueryPartModifier.LOG.debug("Connected {} and {}", originalSource, originalTarget);

		} else {

			// Target is connected (via other operators) to source for
			// replication

			ImmutableList<ILogicalQueryPart> partsOfCopiedSource = ImmutableList
					.copyOf(bundle.getCopyMap().get(originalPart));
			ILogicalQueryPart partOfOriginalTarget = optPartOfOriginalTarget.get();
			Collection<ILogicalQueryPart> partsOfCopiedTarget = bundle.getCopyMap().get(partOfOriginalTarget);

			if (partsOfCopiedSource.size() == 1 && partsOfCopiedTarget.size() == 1) {

				// 1:1 relationship
				ModificationHelper.connectOperators(copiedSources, copiedTargets, subscription);
				ReplicationQueryPartModifier.LOG.debug("Connected {} and {}", originalSource, originalTarget);

			} else if (partsOfCopiedSource.size() == 1 && partsOfCopiedTarget.size() > 1) {

				// N:1 relationship
				// Need to merge
				ILogicalOperator mergeOperator = ReplicationQueryPartModifier.insertMergeOperator(originalTarget,
						Optional.of(copiedSources.iterator().next()), copiedTargets, Optional.of(subscription),
						Optional.of(originalPart), Optional.of(partsOfCopiedSource.iterator().next()), bundle);
				bundle.addMergeOperator(mergeOperator);
				ReplicationQueryPartModifier.LOG.debug("Inserted a merge operator after {}", originalTarget);

			} else if (partsOfCopiedSource.size() > 1 && partsOfCopiedTarget.size() == 1) {

				// 1:N relationship
				// Copies will be connected directly
				for (ILogicalOperator copiedSource : copiedSources) {

					Collection<ILogicalOperator> sources = Lists.newArrayList(copiedSource);

					ModificationHelper.connectOperators(sources, copiedTargets, subscription);
					ReplicationQueryPartModifier.LOG.debug("Connected {} and {}", originalSource, originalTarget);

				}

			} else {

				// N:N relationship
				// Need to merge
				for (int copyNo = 0; copyNo < copiedSources.size(); copyNo++) {

					ILogicalOperator mergeOperator = ReplicationQueryPartModifier.insertMergeOperator(originalTarget,
							Optional.of(copiedSources.get(copyNo)), copiedTargets, Optional.of(subscription),
							Optional.of(originalPart), Optional.of(partsOfCopiedSource.get(copyNo)), bundle);
					bundle.addMergeOperator(mergeOperator);

				}

				ReplicationQueryPartModifier.LOG.debug("Inserted a merge operator after {}", originalTarget);

			}

		}

	}

	/**
	 * Inserts a new merge operator between multiple copies of a target and a
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
	 *            The {@link ReplicationInfoBundle} instance.
	 * @return The inserted merge operator.
	 */
	private static ILogicalOperator insertMergeOperator(ILogicalOperator originalTarget,
			Optional<ILogicalOperator> copiedSource, Collection<ILogicalOperator> copiedTargets,
			Optional<LogicalSubscription> subscription, Optional<ILogicalQueryPart> partOfOriginalSource,
			Optional<ILogicalQueryPart> partOfCopiedSource, ReplicationInfoBundle bundle) {

		Preconditions.checkNotNull(originalTarget, "Original logical target must be not null!");
		Preconditions.checkNotNull(copiedTargets, "Copied logical targets must be not null!");
		Preconditions.checkNotNull(bundle, "Replication info bundle must be not null!");
		Preconditions.checkNotNull(bundle.getCopyMap(), "Copy map must be not null!");

		// Create merge operator
		ILogicalOperator mergeOperator = new ReplicationMergeAO();
		mergeOperator.addParameterInfo("NAME", "'ReplicationMerge'");

		int sinkInPort = 0;
		int sourceOutPort = 0;
		SDFSchema schema = originalTarget.getOutputSchema();
		if (subscription.isPresent()) {

			sourceOutPort = subscription.get().getSourceOutPort();
			sinkInPort = subscription.get().getSinkInPort();
			schema = subscription.get().getSchema();

		}

		// Subscribe the merge operator to the copied targets
		for (int sinkNo = 0; sinkNo < copiedTargets.size(); sinkNo++) {

			ILogicalOperator copiedTarget = ((List<ILogicalOperator>) copiedTargets).get(sinkNo);
			copiedTarget.subscribeSink(mergeOperator, sinkNo, sourceOutPort, schema);

		}

		// Subscribe the copied relative source to the merge operator
		if (copiedSource.isPresent()) {

			mergeOperator.subscribeSink(copiedSource.get(), sinkInPort, 0, schema);

		}

		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copyMap = bundle.getCopyMap();

		if (copiedSource.isPresent() && partOfOriginalSource.isPresent() && partOfCopiedSource.isPresent()) {

			// Create modified query part
			Collection<ILogicalOperator> operatorsWithMerge = Lists
					.newArrayList(partOfCopiedSource.get().getOperators());
			operatorsWithMerge.add(mergeOperator);
			Collection<ILogicalQueryPart> copiedParts = copyMap.get(partOfOriginalSource.get());
			copiedParts.remove(partOfCopiedSource.get());
			ILogicalQueryPart mergePart = new LogicalQueryPart(operatorsWithMerge);
			copiedParts.add(mergePart);
			copyMap.put(partOfOriginalSource.get(), copiedParts);

		} else {

			// Create the query part for the merge operator
			ILogicalQueryPart mergePart = new LogicalQueryPart(mergeOperator);
			Collection<ILogicalQueryPart> copiesOfMergePart = Lists.newArrayList(mergePart);
			copyMap.put(mergePart, copiesOfMergePart);

		}

		bundle.setCopyMap(copyMap);
		return mergeOperator;

	}

	/**
	 * Makes copies of the origin query parts as follows: <br />
	 * For every part to replicate, there will be as many copies made, as
	 * {@link ReplicationInfoBundle#getDegreeOfReplication()}. <br />
	 * For every other query part, there will be one copy made. <br />
	 * Each copy will be a cut off query part having o incoming or outgoing
	 * subscriptions. The result is available as
	 * {@link ReplicationInfoBundle#getCopyMap()}.
	 * 
	 * @param bundle
	 *            The {@link ReplicationInfoBundle} instance.
	 */
	private static void makeCopies(ReplicationInfoBundle bundle) {

		Preconditions.checkNotNull(bundle, "Replication info bundle must be not null!");
		Preconditions.checkNotNull(bundle.getOriginalRelevantParts(), "Relevant parts must be not null!");
		Preconditions.checkNotNull(bundle.getOriginalIrrelevantParts(), "Irrelevant parts must be not null!");

		// Copy the query parts
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiedPartsToReplicateToOriginals = LogicalQueryHelper
				.copyAndCutQueryParts(bundle.getOriginalRelevantParts(), bundle.getDegreeOfReplication());
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiedPartsNotToReplicateToOriginals = LogicalQueryHelper
				.copyAndCutQueryParts(bundle.getOriginalIrrelevantParts(), 1);

		// Put the maps together
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOriginals = Maps.newHashMap();
		copiesToOriginals.putAll(copiedPartsToReplicateToOriginals);
		copiesToOriginals.putAll(copiedPartsNotToReplicateToOriginals);
		bundle.setCopyMap(copiesToOriginals);

		// Check, if the degree of fragmentation is suitable for the number of
		// available peers
		if (!ModificationHelper
				.validateDegreeOfModification(ModificationHelper.calcSize(bundle.getCopyMap().values()))) {

			ReplicationQueryPartModifier.LOG.warn("Got not enough peers for a suitable replication!");

		}

	}

	/**
	 * Makes preparations for replication. <br />
	 * The degree of replication, the operators marking start and end of
	 * replication and the query parts to be replicated are stored within
	 * <code>bundle</code>.
	 * 
	 * @param queryParts
	 *            The given query parts.
	 * @param helper
	 *            The {@link ReplicationParameterHelper} instance.
	 * @param bundle
	 *            The {@link ReplicationInfoBundle} instance.
	 * @return True, if <code>queryParts</code> and the informations given by
	 *         Odysseus-Script are valid for replication.
	 * @throws QueryPartModificationException
	 *             If the parameters for replication do not contain two
	 *             parameters, if the second parameter is no integer or if the
	 *             degree of replication if lower than
	 *             {@link ReplicationParameterHelper#MIN_DEGREE_OF_REPLICATION}
	 *             . <br />
	 *             If the parameters for replication is empty or if the first
	 *             parameter does not match any patterns.<br />
	 *             If no query parts remain to replicate.
	 */
	private void prepare(Collection<ILogicalQueryPart> queryParts, ReplicationParameterHelper helper,
			ReplicationInfoBundle bundle) throws QueryPartModificationException {

		// Preconditions
		Preconditions.checkNotNull(helper, "Replication helper must be not null!");
		Preconditions.checkNotNull(bundle, "Replication info bundle must be not null!");

		// Determine degree of fragmentation
		bundle.setDegreeOfReplication(helper.determineDegreeOfModification());

		// Determine identifiers for start point and end point for replication
		final IPair<Optional<ILogicalOperator>, Optional<ILogicalOperator>> startAndendPointForReplication = helper
				.determineStartAndEndPoints(queryParts);
		bundle.setOriginStartOperator(startAndendPointForReplication.getE1());
		bundle.setOriginEndOperator(startAndendPointForReplication.getE2());

		// Determine query parts to be replicated and those to be not
		final IPair<Collection<ILogicalQueryPart>, Collection<ILogicalQueryPart>> partsToReplicatesAndPartsNot = ReplicationQueryPartModifier
				.determinePartsToReplicate(queryParts, helper, bundle);
		if (partsToReplicatesAndPartsNot.getE1().isEmpty()) {

			throw new QueryPartModificationException("No query parts given to replicate!");

		}
		bundle.setOriginalRelevantParts(partsToReplicatesAndPartsNot.getE1());
		bundle.setOriginalIrrelevantParts(partsToReplicatesAndPartsNot.getE2());

	}

	/**
	 * Determines all query parts to replicate.
	 * 
	 * @param queryParts
	 *            The query parts to check.
	 * @param helper
	 *            The {@link ReplicationParameterHelper} instance.
	 * @param bundle
	 *            The {@link ReplicationInfoBundle} instance.
	 * @return All operators within <code>operators</code> to replicate.
	 */
	private static IPair<Collection<ILogicalQueryPart>, Collection<ILogicalQueryPart>> determinePartsToReplicate(
			Collection<ILogicalQueryPart> queryParts, ReplicationParameterHelper helper, ReplicationInfoBundle bundle) {

		// Preconditions
		Preconditions.checkNotNull(queryParts, "List of query parts must be not null!");
		Preconditions.checkNotNull(!queryParts.isEmpty(), "List of query parts must be not empty!");

		Collection<ILogicalQueryPart> partsToReplicate = Lists.newArrayList();
		Collection<ILogicalQueryPart> partsNotToReplicate = Lists.newArrayList();

		for (ILogicalQueryPart queryPart : queryParts) {

			Collection<ILogicalOperator> operatorsToReplicate = ReplicationQueryPartModifier
					.determineOperatorsToReplicate(queryPart.getOperators(), helper, bundle);
			if (!operatorsToReplicate.isEmpty()) {

				final ILogicalQueryPart partToReplicate = new LogicalQueryPart(operatorsToReplicate,
						queryPart.getAvoidingQueryParts());
				partsToReplicate.add(partToReplicate);

			}

			if (operatorsToReplicate.size() < queryPart.getOperators().size()) {

				Collection<ILogicalOperator> operatorsNotToReplicate = Lists.newArrayList(queryPart.getOperators());
				operatorsNotToReplicate.removeAll(operatorsToReplicate);

				if (!operatorsNotToReplicate.isEmpty()) {

					ILogicalQueryPart partNotToReplicate = new LogicalQueryPart(operatorsNotToReplicate,
							queryPart.getAvoidingQueryParts());
					partsNotToReplicate.add(partNotToReplicate);

				}

			}

		}

		return new Pair<Collection<ILogicalQueryPart>, Collection<ILogicalQueryPart>>(partsToReplicate,
				partsNotToReplicate);

	}

	/**
	 * Determines all operators to replicate.
	 * 
	 * @param operators
	 *            The operators to check.
	 * @param helper
	 *            The {@link ReplicationParameterHelper} instance.
	 * @param bundle
	 *            The {@link ReplicationInfoBundle} instance.
	 * @return All operators within <code>operators</code> to replicate.
	 */
	private static Collection<ILogicalOperator> determineOperatorsToReplicate(Collection<ILogicalOperator> operators,
			ReplicationParameterHelper helper, ReplicationInfoBundle bundle) {

		// Preconditions
		Preconditions.checkNotNull(operators, "List of operators must be not null!");
		Preconditions.checkNotNull(helper, "Replication helper must be not null!");
		Preconditions.checkNotNull(bundle, "Replication info bundle must be not null!");
		Preconditions.checkNotNull(bundle.getOriginStartOperator(),
				"The operator marking the start of replication must be not null!");
		Preconditions.checkNotNull(bundle.getOriginEndOperator(),
				"The operator marking the end of replication must be not null!");

		Collection<ILogicalOperator> operatorsToReplicate = Lists.newArrayList();

		for (ILogicalOperator operator : operators) {

			if (!ReplicationRuleHelper.canOperatorBeReplicated(operator, helper)) {

				continue;

			} else if (!bundle.getOriginStartOperator().isPresent()) {

				operatorsToReplicate.add(operator);

			} else if (bundle.getOriginStartOperator().isPresent()
					&& ModificationHelper.isOperatorAbove(operator, bundle.getOriginStartOperator().get())
					&& (!bundle.getOriginEndOperator().isPresent()
							|| ModificationHelper.isOperatorAbove(bundle.getOriginEndOperator().get(), operator))) {

				operatorsToReplicate.add(operator);

			}

		}

		return operatorsToReplicate;

	}

}