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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

/**
 * A abstract modifier of {@link ILogicalQueryPart}s, which fragments data
 * streams from a given source into parallel query parts and inserts operators
 * to merge the result sets of the parallel fragments for each relative sink
 * within every single query part.
 * 
 * @author Michael Brand
 */
public abstract class AbstractFragmentationQueryPartModificator implements IQueryPartModificator {

	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractFragmentationQueryPartModificator.class);

	/**
	 * The minimum degree of fragmentation.
	 */
	private static final int min_degree = 2;

	@Override
	public Collection<ILogicalQueryPart> modify(Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, QueryBuildConfiguration config, List<String> modificatorParameters) throws QueryPartModificationException {

		// Preconditions
		if (queryParts == null) {

			QueryPartModificationException e = new QueryPartModificationException("Query parts to be modified must be not null!");
			AbstractFragmentationQueryPartModificator.log.error(e.getMessage(), e);
			throw e;

		} else if (queryParts.isEmpty()) {

			AbstractFragmentationQueryPartModificator.log.warn("No query parts given to fragment!");
			return queryParts;

		}

		// The return value
		Collection<ILogicalQueryPart> modifiedParts = Lists.newArrayList();
		
		// History for origin relative sources (key), inserted operators for
		// fragmentation (value.getE1())
		// and the subscription to the origin relative source
		// (value.getE2())
		Map<ILogicalOperator, Collection<IPair<ILogicalOperator, LogicalSubscription>>> historyOfFragmentationOperators = Maps.newHashMap();
		
		// Collection of all inserted operators for reunion
		Collection<ILogicalOperator> historyOfReunionOperators = Lists.newArrayList();

		try {

			// Determine degree of fragmentation and source to be fragmented
			final String sourceName = this.determineSourceName(modificatorParameters);
			final int degreeOfFragmentation = this.determineDegreeOfFragmentation(modificatorParameters);

			// Determine all parts to be fragmented (e1) and other (e2) query
			// parts
			// Note: Some of the origin query parts may be split.
			IPair<Collection<ILogicalQueryPart>, Collection<ILogicalQueryPart>> partsToBeFragmentedAndOtherParts = this.determinePartsToBeFragmented(queryParts, sourceName);
			Collection<ILogicalQueryPart> partsToBeFragmented = partsToBeFragmentedAndOtherParts.getE1();

			// Preconditions
			if (partsToBeFragmented.isEmpty()) {

				AbstractFragmentationQueryPartModificator.log.warn("No query parts given to fragment depending on the source to be fragmented!");
				return queryParts;

			}

			modifiedParts.addAll(partsToBeFragmented);
			Collection<ILogicalQueryPart> otherParts = partsToBeFragmentedAndOtherParts.getE2();
			modifiedParts.addAll(otherParts);

			// Copy the query parts
			Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin = LogicalQueryHelper.copyAndCutQueryParts(modifiedParts, degreeOfFragmentation);

			// Keep only one copy of parts not to be fragmented
			for (ILogicalQueryPart originPart : otherParts) {

				ILogicalQueryPart copyToKeep = copiesToOrigin.get(originPart).iterator().next();
				copiesToOrigin.get(originPart).clear();
				copiesToOrigin.get(originPart).add(copyToKeep);

			}

			if (AbstractFragmentationQueryPartModificator.log.isDebugEnabled()) {

				// Print working copies
				for (ILogicalQueryPart origin : copiesToOrigin.keySet())
					AbstractFragmentationQueryPartModificator.log.debug("Created query parts {} as copies of query part {}.", copiesToOrigin.get(origin), origin);

			}

			// Check, if the degree of fragmentation is suitable for the number
			// of available peers
			AbstractFragmentationQueryPartModificator.validateDegreeOfFragmentation(partsToBeFragmented.size(), degreeOfFragmentation);

			// Modify each query part to be fragmented for insertion of
			// operators for fragmentation
			for (ILogicalQueryPart originPart : partsToBeFragmented)
				copiesToOrigin = this.modifyPartForFragmentation(originPart, copiesToOrigin, sourceName, historyOfFragmentationOperators, modificatorParameters);

			// Modify each query part to be fragmented for insertion of
			// operators for reunion
			for (ILogicalQueryPart originPart : partsToBeFragmented) {

				copiesToOrigin = this.modifyPartForMerging(originPart, copiesToOrigin, partsToBeFragmented, historyOfFragmentationOperators, historyOfReunionOperators, modificatorParameters);

			}

			// Connect all other query parts, which are not connected yet
			for (ILogicalQueryPart originPart : otherParts)
				copiesToOrigin = this.attachOtherParts(originPart, copiesToOrigin, partsToBeFragmented, sourceName, historyOfFragmentationOperators);

			// Create the return value
			modifiedParts.clear();
			for (ILogicalQueryPart originPart : copiesToOrigin.keySet()) {

				for (ILogicalQueryPart modifiedPart : copiesToOrigin.get(originPart)) {

					Collection<ILogicalQueryPart> avoidedParts = Lists.newArrayList(copiesToOrigin.get(originPart));
					avoidedParts.remove(modifiedPart);

					modifiedPart.addAvoidingQueryParts(avoidedParts);

					modifiedParts.add(modifiedPart);
				}

			}

		} catch (Exception e) {

			String message = "Some error occured while modifying " + queryParts + "!";
			AbstractFragmentationQueryPartModificator.log.error(message, e);
			throw new QueryPartModificationException(message, e);

		}

		Collection<ILogicalOperator> operatorsForFragmentation = Lists.newArrayList();
		for(ILogicalOperator operator : historyOfFragmentationOperators.keySet()) {
			
			for(IPair<ILogicalOperator, LogicalSubscription> pair : historyOfFragmentationOperators.get(operator)) {
				
				if(!operatorsForFragmentation.contains(pair.getE1()))
					operatorsForFragmentation.add(pair.getE1());
				
			}
			
		}
		
		return AbstractFragmentationQueryPartModificator.setQueryPartsToAvoid(modifiedParts, operatorsForFragmentation, historyOfReunionOperators);

	}
	
	/**
	 * Sets for each query part the parts to be avoided. <br />
	 * A part being a fragment will set all other fragments, the fragmentation part and the merger part to be avoided. <br />
	 * A part containing a fragmentation operator will set all fragments to be avoided. <br />
	 * A part containing a merger will set all fragments to be avoided.
	 * @param parts A collection of query parts to process.
	 * @return The same collection as <code>parts</code> except the avoided parts being set.
	 * @throws QueryPartModificationException if a part of an operator could not be determined.
	 */
	private static Collection<ILogicalQueryPart> setQueryPartsToAvoid(Collection<ILogicalQueryPart> parts, 
			Collection<ILogicalOperator> operatorsForFragmentation, Collection<ILogicalOperator> operatorsForReunion) throws QueryPartModificationException {
		
		Preconditions.checkNotNull(parts, "Collection of query parts must be not null!");
		Preconditions.checkNotNull(operatorsForFragmentation, "Collection of operators for fragmentation must be not null!");
		Preconditions.checkNotNull(operatorsForReunion, "Collection of operators for reunion must be not null!");
		
		Collection<ILogicalQueryPart> modifiedParts = Lists.newArrayList(parts);
		
		for(ILogicalQueryPart part : modifiedParts) {
			
			for(ILogicalOperator operator : part.getOperators()) {
				
				if(operatorsForFragmentation.contains(operator)) {
				
					final ILogicalOperator fragmenter = operator;
					
					for(LogicalSubscription subToSink : fragmenter.getSubscriptions()) {
						
						final ILogicalOperator sink = subToSink.getTarget();					
						final Optional<ILogicalQueryPart> optPartOfSink = LogicalQueryHelper.determineQueryPart(modifiedParts, sink);
						
						if(!optPartOfSink.isPresent() || optPartOfSink.get().equals(part)) {
							
							final String errorMessage = "Query part of " + sink + " is either not present or the same part as of the antecedent fragmenter!";
							AbstractFragmentationQueryPartModificator.log.error(errorMessage);
							throw new QueryPartModificationException(errorMessage);
							
						}
						
						final ILogicalQueryPart partOfSink = optPartOfSink.get();
						if(!part.getAvoidingQueryParts().contains(partOfSink))
							part.addAvoidingQueryPart(partOfSink);
						if(!partOfSink.getAvoidingQueryParts().contains(part))
							partOfSink.addAvoidingQueryPart(part);
						
					}
					
				} else if(operatorsForReunion.contains(operator)) {
					
					final ILogicalOperator merger = operator;
					
					for(LogicalSubscription subToSource : merger.getSubscribedToSource()) {
						
						final ILogicalOperator source = subToSource.getTarget();					
						final Optional<ILogicalQueryPart> optPartOfSource = LogicalQueryHelper.determineQueryPart(modifiedParts, source);
						
						if(!optPartOfSource.isPresent() || optPartOfSource.get().equals(part)) {
							
							final String errorMessage = "Query part of " + source + " is either not present or the same part as of the antecedent fragmenter!";
							AbstractFragmentationQueryPartModificator.log.error(errorMessage);
							throw new QueryPartModificationException(errorMessage);
							
						}
						
						final ILogicalQueryPart partOfSource = optPartOfSource.get();
						if(!part.getAvoidingQueryParts().contains(partOfSource))
							part.addAvoidingQueryPart(partOfSource);
						if(!partOfSource.getAvoidingQueryParts().contains(part))
							partOfSource.addAvoidingQueryPart(part);
						
					}
					
				}
				
			}
			
		}
		
		return modifiedParts;
		
	}

	/**
	 * Subscribes a collection of targets to a collection of sources (m:n).
	 * 
	 * @param copiesToOrigin
	 *            A mapping of copies to origin query parts.
	 * @param sources
	 *            The sources.
	 * @param targets
	 *            The targets.
	 * @param subscription
	 *            The subscription to use.
	 * @return A modified mapping of copies to origin query parts.
	 * @throws NullPointerException
	 *             if <code>copiesToOrigin</code>, <code>sources</code>,
	 *             <code>targets</code> or <code>subscription</code> is null.
	 */
	protected static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> connect(Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, Collection<ILogicalOperator> sources, LogicalSubscription subscription,
			Collection<ILogicalOperator> targets) {

		// Preconditions
		if (copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if (sources == null)
			throw new NullPointerException("The sources must be not null!");
		else if (subscription == null)
			throw new NullPointerException("The subscription to modify must be not null!");
		else if (targets == null)
			throw new NullPointerException("The targets must be not null!");

		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);

		for (ILogicalOperator source : sources) {

			for (ILogicalOperator target : targets)
				source.subscribeSink(target, subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());

		}

		AbstractFragmentationQueryPartModificator.log.debug("Connected {} and {}", sources, targets);

		return modifiedCopiesToOrigin;

	}

	/**
	 * Searches an operator for fragmentation.
	 * 
	 * @param queryParts
	 *            A collection of query parts to search within.
	 * @param originSource
	 *            The origin, relative source.
	 * @param subscription
	 *            The subscription, which has been broken due to the insertion
	 *            of the operator for fragmentation.
	 * @param historyOfOperatorsForFragmentation
	 *            The history for origin relative sources (key), inserted
	 *            operators for fragmentation (value.getE1()) and the
	 *            subscription to the origin relative source (value.getE2()).
	 * @return The found operator for fragmentation (e1) and the query part
	 *         containing it (e2).
	 * @throws NullPointerException
	 *             if <code>queryParts</code>,<code>subscription</code> or
	 *             <code>historyOfOperatorsForFragmentation</code> is null.
	 * @throws IllegalArgumentException
	 *             if an operator for fragmentation could not be found.
	 */
	protected static IPair<ILogicalOperator, ILogicalQueryPart> searchOperatorForFragmentation(Collection<ILogicalQueryPart> queryParts,
			Map<ILogicalOperator, Collection<IPair<ILogicalOperator, LogicalSubscription>>> historyOfOperatorsForFragmentation, LogicalSubscription subscription, ILogicalOperator originSource) throws NullPointerException, IllegalArgumentException {

		// Preconditions
		if (queryParts == null)
			throw new NullPointerException("Collection query parts must be not null!");
		else if (subscription == null)
			throw new NullPointerException("The subscription must be not null!");
		else if (historyOfOperatorsForFragmentation == null)
			throw new NullPointerException("The history of inserted operator for fragmentation must be not null!");
		else if (originSource == null)
			throw new NullPointerException("The origin source must be not null!");

		ILogicalOperator operatorForFragmentation = null;

		for (IPair<ILogicalOperator, LogicalSubscription> entry : historyOfOperatorsForFragmentation.get(originSource)) {

			if (entry.getE2().equals(subscription)) {

				operatorForFragmentation = entry.getE1();
				break;

			}

		}

		if (operatorForFragmentation == null)
			throw new IllegalArgumentException("Could not find an operator for fragmentation for the given subscription " + subscription);

		return new Pair<ILogicalOperator, ILogicalQueryPart>(operatorForFragmentation, LogicalQueryHelper.determineQueryPart(queryParts, operatorForFragmentation).get());

	}

	/**
	 * Removes and obsolete operator for fragmentation.
	 * 
	 * @param copiesToOrigin
	 *            A mapping of copies to origin query parts.
	 * @param originSource
	 *            The origin source, whose copies are subscribed to the operator
	 *            for fragmentation.
	 * @param subscription
	 *            The subscription of <code>originSource</code>.
	 * @param historyOfOperatorsForFragmentation
	 *            The history for origin relative sources (key), inserted
	 *            operators for fragmentation (value.getE1()) and the
	 *            subscription to the origin relative source (value.getE2()).
	 * @return A modified mapping of copies to origin query parts.
	 * @throws NullPointerException
	 *             if <code>copiesToOrigin</code>, <code>originSource</code>,
	 *             <code>subscription</code> or
	 *             <code>historyOfOperatorsForFragmentation</code> is null.
	 * @throws IllegalArgumentException
	 *             if the query part containing the operator for fragmentation
	 *             could not be determined.
	 */
	private static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> removeOperatorForFragmentation(Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, ILogicalOperator originSource, LogicalSubscription subscription,
			Map<ILogicalOperator, Collection<IPair<ILogicalOperator, LogicalSubscription>>> historyOfOperatorsForFragmentation) throws NullPointerException, IllegalArgumentException {

		// Preconditions
		if (copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if (originSource == null)
			throw new NullPointerException("The origin source must be not null!");
		else if (subscription == null)
			throw new NullPointerException("The subscription must be not null!");
		else if (historyOfOperatorsForFragmentation == null)
			throw new NullPointerException("The history of inserted operator for fragmentation must be not null!");

		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);

		Optional<ILogicalQueryPart> optPartOfOriginSource = LogicalQueryHelper.determineQueryPart(copiesToOrigin.keySet(), originSource);
		if (!optPartOfOriginSource.isPresent())
			throw new IllegalArgumentException("Could not determine the query part of " + originSource);
		Collection<ILogicalOperator> copiesOfOriginSource = LogicalQueryHelper.collectCopies(optPartOfOriginSource.get(), copiesToOrigin.get(optPartOfOriginSource.get()), originSource);

		IPair<ILogicalOperator, ILogicalQueryPart> operatorForFragmentationAndItsPart = AbstractFragmentationQueryPartModificator.searchOperatorForFragmentation(copiesToOrigin.keySet(), historyOfOperatorsForFragmentation, subscription, originSource);

		for (ILogicalOperator copyOfOriginSource : copiesOfOriginSource) {

			Collection<LogicalSubscription> subsToRemove = copyOfOriginSource.getSubscribedToSource(operatorForFragmentationAndItsPart.getE1());
			for (LogicalSubscription sub : subsToRemove)
				copyOfOriginSource.unsubscribeFromSource(sub);

		}

		modifiedCopiesToOrigin.remove(operatorForFragmentationAndItsPart.getE2());
		historyOfOperatorsForFragmentation.remove(operatorForFragmentationAndItsPart.getE1());
		AbstractFragmentationQueryPartModificator.log.debug("Removed {} due to optimization", operatorForFragmentationAndItsPart.getE1());

		return modifiedCopiesToOrigin;

	}

	/**
	 * Modifies a part to be fragmented by inserting an operator for
	 * fragmentation for every subscription to source from relevant, relative
	 * sources.
	 * 
	 * @param originPart
	 *            The query part to modify.
	 * @param copiesToOrigin
	 *            A mapping of copies to origin query parts.
	 * @param sourceName
	 *            The name of the source to be fragmented.
	 * @param historyOfOperatorsForFragmentation
	 *            The history for origin relative sources (key), inserted
	 *            operators for fragmentation (value.getE1()) and the
	 *            subscription to the origin relative source (value.getE2()).
	 * @param modificatorParameters
	 *            The parameters for the modification given by the user without
	 *            the parameter <code>fragmentation-strategy-name</code>.
	 * @return A modified mapping of copies to origin query parts.
	 * @throws NullPointerException
	 *             if <code>originPart</code>, <code>copiesToOrigin</code>,
	 *             <code>sourceName</code> or
	 *             <code>historyOfOperatorsForFragmentation</code> is null.
	 * @throws IllegalArgumentException
	 *             if <code>copiesToOrigin</code> does not contain
	 *             <code>originPart</code> as a key.
	 * @throws QueryPartModificationException
	 *             if any error occurs in
	 *             {@link #insertOperatorForFragmentation(Map, Collection, LogicalSubscription, Collection)}
	 *             .
	 */
	private Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifyPartForFragmentation(ILogicalQueryPart originPart, Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, String sourceName,
			Map<ILogicalOperator, Collection<IPair<ILogicalOperator, LogicalSubscription>>> historyOfOperatorsForFragmentation, List<String> modificationParameters) throws NullPointerException, IllegalArgumentException,
			QueryPartModificationException {

		// Preconditions
		if (originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if (copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if (sourceName == null)
			throw new NullPointerException("Name of the source to be fragmented must be not null!");
		else if (historyOfOperatorsForFragmentation == null)
			throw new NullPointerException("The history of inserted operator for fragmentation must be not null!");
		else if (!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain all query parts to be fragmented!");

		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);

		// Collect all relative sources
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSources = LogicalQueryHelper.collectRelativeSources(originPart, modifiedCopiesToOrigin.get(originPart));

		// Process each relative source, which is relevant for fragmentation
		for (ILogicalOperator originSource : copiedToOriginSources.keySet()) {

			if (originSource.getSubscribedToSource().isEmpty()) {

				throw new QueryPartModificationException("The real source " + originSource.toString() + " of part " + originPart.toString() + " is a source to be fragmented! "
						+ "Therefore this real source must not be part of a query part to be fragmented!");

			}

			for (LogicalSubscription subToSource : originSource.getSubscribedToSource()) {

				ILogicalOperator target = subToSource.getTarget();
				Collection<ILogicalOperator> targets = Lists.newArrayList();
				Collection<ILogicalOperator> copiedSources = copiedToOriginSources.get(originSource);

				if (originPart.getOperators().contains(target))
					continue;
				else if (!AbstractFragmentationQueryPartModificator.isOperatorRelevant(target, sourceName)) {

					Optional<ILogicalQueryPart> optPartOfTarget = LogicalQueryHelper.determineQueryPart(modifiedCopiesToOrigin.keySet(), target);
					if (optPartOfTarget.isPresent()) {

						targets.addAll(LogicalQueryHelper.collectCopies(optPartOfTarget.get(), modifiedCopiesToOrigin.get(optPartOfTarget.get()), target));

					} else
						targets.add(target);

					modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.connect(modifiedCopiesToOrigin, targets, subToSource, copiedSources);

				} else {

					modifiedCopiesToOrigin = this.insertOperatorForFragmentation(modifiedCopiesToOrigin, originSource, copiedSources, subToSource, historyOfOperatorsForFragmentation, modificationParameters);

				}

			}

		}

		return modifiedCopiesToOrigin;

	}

	/**
	 * Inserts an operator for fragmentation and subscribes it by the copies of
	 * a relative source.
	 * 
	 * @param copiesToOrigin
	 *            A mapping of copies to origin query parts.
	 * @param originSource
	 *            The origin, relative source.
	 * @param copiesOfOriginSource
	 *            The copies of <code>originSource</code>.
	 * @param subscription
	 *            The origin subscription of <code>originSource</code>.
	 * @param historyOfOperatorsForFragmentation
	 *            The history for origin relative sources (key), inserted
	 *            operators for fragmentation (value.getE1()) and the
	 *            subscription to the origin relative source (value.getE2()).
	 * @param modificatorParameters
	 *            The parameters for the modification given by the user without
	 *            the parameter <code>fragmentation-strategy-name</code>.
	 * @return A modified mapping of copies to origin query parts.
	 * @throws NullPointerException
	 *             if <code>copiesToOrigin</code>, <code>originSource</code>,
	 *             <code>copiesOfOriginSource</code>, <code>subscription</code>
	 *             or <code>historyOfOperatorsForFragmentation</code> is null.
	 * @throws QueryPartModificationException
	 *             if the operator for fragmentation could not be instantiated.
	 */
	private Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> insertOperatorForFragmentation(Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, ILogicalOperator originSource, Collection<ILogicalOperator> copiesOfOriginSource,
			LogicalSubscription subscription, Map<ILogicalOperator, Collection<IPair<ILogicalOperator, LogicalSubscription>>> historyOfOperatorsForFragmentation, List<String> modificationParameters) throws NullPointerException,
			QueryPartModificationException {

		// Preconditions
		if (copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if (originSource == null)
			throw new NullPointerException("The origin source must be not null!");
		else if (copiesOfOriginSource == null)
			throw new NullPointerException("The copied sources must be not null!");
		else if (subscription == null)
			throw new NullPointerException("The subscription to modify must be not null!");
		else if (historyOfOperatorsForFragmentation == null)
			throw new NullPointerException("The history of inserted operator for fragmentation must be not null!");

		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);

		// Create operator for fragmentation
		ILogicalOperator operatorForFragmentation = this.createOperatorForFragmentation(copiesOfOriginSource.size(), modificationParameters);

		// Subscribe the operator for fragmentation to the sources
		for (int sourceNo = 0; sourceNo < copiesOfOriginSource.size(); sourceNo++) {

			ILogicalOperator copiedSource = ((List<ILogicalOperator>) copiesOfOriginSource).get(sourceNo);
			operatorForFragmentation.subscribeSink(copiedSource, subscription.getSinkInPort(), sourceNo, subscription.getSchema());

		}

		AbstractFragmentationQueryPartModificator.log.debug("Inserted an operator for fragmentation before {}", copiesOfOriginSource);

		// Create the query part for the operator for fragmentation
		ILogicalQueryPart fragmentationPart = new LogicalQueryPart(operatorForFragmentation);
		Collection<ILogicalQueryPart> copiesOfFragmentationPart = Lists.newArrayList(fragmentationPart);
		modifiedCopiesToOrigin.put(fragmentationPart, copiesOfFragmentationPart);
		IPair<ILogicalOperator, LogicalSubscription> pair = new Pair<ILogicalOperator, LogicalSubscription>(operatorForFragmentation, subscription);
		if (historyOfOperatorsForFragmentation.containsKey(originSource))
			historyOfOperatorsForFragmentation.get(originSource).add(pair);
		else {

			Collection<IPair<ILogicalOperator, LogicalSubscription>> collection = Lists.newArrayList();
			collection.add(pair);
			historyOfOperatorsForFragmentation.put(originSource, collection);
		}

		return modifiedCopiesToOrigin;

	}

	/**
	 * Creates an operator for fragmentation.
	 * 
	 * @param modificatorParameters
	 *            The parameters for the modification given by the user without
	 *            the parameter <code>fragmentation-strategy-name</code>.
	 * @param numFragments
	 *            The number of fragments.
	 * @return The created operator for fragmentation.
	 * @throws QueryPartModificationException
	 *             if any error occurs.
	 */
	protected abstract ILogicalOperator createOperatorForFragmentation(int numFragments, List<String> modificationParameters) throws QueryPartModificationException;

	/**
	 * Modifies a part to be fragmented by inserting an operator for reunion for
	 * every subscription from relevant, relative sinks.
	 * 
	 * @param originPart
	 *            The query part to modify.
	 * @param copiesToOrigin
	 *            A mapping of copies to origin query parts.
	 * @param partsToBeFragmented
	 *            A collection of all query parts to be fragmented.
	 * @param historyOfOperatorsForFragmentation
	 *            The history for origin relative sources (key), inserted
	 *            operators for fragmentation (value.getE1()) and the
	 *            subscription to the origin relative source (value.getE2()).
	 * @param historyOfOperatorsForReunion
	 *            A collection of all inserted operators for reunion
	 * @param modificatorParameters
	 *            The parameters for the modification given by the user without
	 *            the parameter <code>fragmentation-strategy-name</code>.
	 * @return A modified mapping of copies to origin query parts.
	 * @throws NullPointerException
	 *             if <code>originPart</code>, <code>copiesToOrigin</code> or
	 *             <code>partsToBeFragmented</code>,
	 *             <code>historyOfOperatorsForFragmentation</code> is null.
	 * @throws IllegalArgumentException
	 *             if <code>copiesToOrigin</code> does not contain
	 *             <code>partsToBeFragmented</code> or <code>otherParts</code>
	 *             as keys or if <code>partsToBeFragmented</code> does not
	 *             contain <code>originPart</code>.
	 * @throws QueryPartModificationException
	 *             if any error occurs in
	 *             {@link #insertOperatorForReunion(Map, ILogicalOperator, Collection, Optional, Collection)}
	 *             .
	 */
	private Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifyPartForMerging(ILogicalQueryPart originPart, Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, Collection<ILogicalQueryPart> partsToBeFragmented,
			Map<ILogicalOperator, Collection<IPair<ILogicalOperator, LogicalSubscription>>> historyOfOperatorsForFragmentation, 
			Collection<ILogicalOperator> historyOfOperatorsForReunion, List<String> modificationParameters) throws NullPointerException, IllegalArgumentException,
			QueryPartModificationException {

		// Preconditions
		if (originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if (copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if (!copiesToOrigin.keySet().contains(originPart))
			throw new IllegalArgumentException("Mapping of copies to origin query parts must contain the origin query part for modification!");
		else if (partsToBeFragmented == null)
			throw new NullPointerException("Collection of relevant parts for fragmentation must be not null!");
		else if (!partsToBeFragmented.contains(originPart))
			throw new IllegalArgumentException("Collection of relevant parts for fragmentation must contain the origin query part for modification!");
		else if (historyOfOperatorsForFragmentation == null)
			throw new NullPointerException("The history of inserted operator for fragmentation must be not null!");
		else if (historyOfOperatorsForReunion == null)
			throw new NullPointerException("The history of inserted operator for reunion must be not null!");
		else if (modificationParameters == null)
			throw new NullPointerException("Modification parameters must be not null!");

		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);

		// Collect all relative sinks
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSinks = LogicalQueryHelper.collectRelativeSinks(originPart, modifiedCopiesToOrigin.get(originPart));

		// Process each relative sink
		for (ILogicalOperator originSink : copiedToOriginSinks.keySet()) {

			Collection<ILogicalOperator> copiedSinks = copiedToOriginSinks.get(originSink);
			Collection<ILogicalOperator> targets = Lists.newArrayList();
			Optional<ILogicalQueryPart> queryPartOfOriginTarget = Optional.absent();
			List<ILogicalQueryPart> queryPartsOfCopiedTargets = Lists.newArrayList();

			if (originSink.getSubscriptions().isEmpty()) {

				Optional<LogicalSubscription> optSubscription = Optional.absent();

				modifiedCopiesToOrigin = this.insertOperatorForReunion(
						originPart, modifiedCopiesToOrigin, originSink, copiedSinks, optSubscription, targets, 
						historyOfOperatorsForFragmentation, historyOfOperatorsForReunion, 
						queryPartOfOriginTarget, queryPartsOfCopiedTargets, modificationParameters);

			} else {

				for (LogicalSubscription subToSink : originSink.getSubscriptions()) {

					ILogicalOperator target = subToSink.getTarget();

					if (originPart.getOperators().contains(target))
						continue;

					queryPartOfOriginTarget = LogicalQueryHelper.determineQueryPart(modifiedCopiesToOrigin.keySet(), target);
					
					if (queryPartOfOriginTarget.isPresent()) {

						queryPartsOfCopiedTargets = (List<ILogicalQueryPart>) modifiedCopiesToOrigin.get(queryPartOfOriginTarget.get());
						targets.addAll(LogicalQueryHelper.collectCopies(queryPartOfOriginTarget.get(), modifiedCopiesToOrigin.get(queryPartOfOriginTarget.get()), target));

						if (partsToBeFragmented.contains(queryPartOfOriginTarget.get())) {

							LogicalSubscription subscription = new LogicalSubscription(originSink, subToSink.getSinkInPort(), subToSink.getSourceOutPort(), subToSink.getSchema());

							if (this.canOptimizeSubscription(originPart, modifiedCopiesToOrigin, originSink, copiedSinks, subToSink, modificationParameters)) {

								modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.removeOperatorForFragmentation(modifiedCopiesToOrigin, subToSink.getTarget(), subscription, historyOfOperatorsForFragmentation);
								for (int copyNo = 0; copyNo < copiedSinks.size(); copyNo++) {

									Collection<ILogicalOperator> singleSource = Lists.newArrayList(((List<ILogicalOperator>) copiedSinks).get(copyNo));
									Collection<ILogicalOperator> singleTarget = Lists.newArrayList(((List<ILogicalOperator>) targets).get(copyNo));

									modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.connect(modifiedCopiesToOrigin, singleSource, subToSink, singleTarget);

								}

								continue;

							}

							targets.clear();
							targets.add(AbstractFragmentationQueryPartModificator.findOperatorForFragmentation(target, subscription, historyOfOperatorsForFragmentation).get());
							targets.iterator().next().initialize();

						}

					} else {
						targets.add(target);
					}

					modifiedCopiesToOrigin = this.insertOperatorForReunion(
							originPart, modifiedCopiesToOrigin, originSink, copiedSinks, Optional.of(subToSink), targets, 
							historyOfOperatorsForFragmentation, historyOfOperatorsForReunion, 
							queryPartOfOriginTarget, queryPartsOfCopiedTargets, modificationParameters);

				}

			}

		}

		return modifiedCopiesToOrigin;

	}

	/**
	 * Finds an inserted operator for fragmentation, is there is any for the
	 * given origin source and subscription.
	 * 
	 * @param originSource
	 *            The origin, relative source.
	 * @param subscription
	 *            The origin subscription of <code>originSource</code>.
	 * @param historyOfOperatorsForFragmentation
	 *            The history for origin relative sources (key), inserted
	 *            operators for fragmentation (value.getE1()) and the
	 *            subscription to the origin relative source (value.getE2()).
	 * @return The operator for fragmentation subscribed by
	 *         <code>originSource</code> for which <code>subscription</code> has
	 *         been broken, if there is any.
	 * @throws NullPointerException
	 *             if <code>originSource</code>, <code>subscription</code> or
	 *             <code>historyOfOperatorsForFragmentation</code> is null.
	 */
	private static Optional<ILogicalOperator> findOperatorForFragmentation(ILogicalOperator originSource, LogicalSubscription subscription,
			Map<ILogicalOperator, Collection<IPair<ILogicalOperator, LogicalSubscription>>> historyOfOperatorsForFragmentation) throws NullPointerException {

		// Preconditions
		if (originSource == null)
			throw new NullPointerException("The origin source must be not null!");
		else if (subscription == null)
			throw new NullPointerException("The subscription to modify must be not null!");
		else if (historyOfOperatorsForFragmentation == null)
			throw new NullPointerException("The history of inserted operator for fragmentation must be not null!");

		for (IPair<ILogicalOperator, LogicalSubscription> pair : historyOfOperatorsForFragmentation.get(originSource)) {

			if (pair.getE2().equals(subscription))
				return Optional.of(pair.getE1());

		}

		return Optional.absent();

	}

	/**
	 * Inserts an operator for reunion and subscribes it to the copies of a
	 * relative source and by a list of targets, if there are any.
	 * 
	 * @param originPart
	 *            The query part to modify.
	 * @param copiesToOrigin
	 *            A mapping of copies to origin query parts.
	 * @param originSink
	 *            The origin, relative sink.
	 * @param copiesOfOriginSink
	 *            The copies of <code>originSink</code>.
	 * @param optSubscription
	 *            The origin subscription of <code>originSink</code>, if
	 *            present.
	 * @param targets
	 *            A collection of all targets.
	 * @param historyOfOperatorsForFragmentation
	 *            The history for origin relative sources (key), inserted
	 *            operators for fragmentation (value.getE1()) and the
	 *            subscription to the origin relative source (value.getE2()).
	 * @param historyOfOperatorsForReunion
	 *            A collection of all inserted operators for reunion
	 * @param modificatorParameters
	 *            The parameters for the modification given by the user without
	 *            the parameter <code>fragmentation-strategy-name</code>.
	 * @return A modified mapping of copies to origin query parts.
	 * @throws NullPointerException
	 *             if <code>originPart</code>, <code>copiesToOrigin</code>,
	 *             <code>originSink</code>, <code>copiesOfOriginSink</code>,
	 *             <code>optSubscription</code> or <code>targets</code> is null.
	 * @throws QueryPartModificationException
	 *             if the operator for reunion could not be instantiated.
	 */
	protected Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> insertOperatorForReunion(ILogicalQueryPart originPart, Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, ILogicalOperator originSink,
			Collection<ILogicalOperator> copiesOfOriginSink, Optional<LogicalSubscription> optSubscription, Collection<ILogicalOperator> targets,
			Map<ILogicalOperator, Collection<IPair<ILogicalOperator, LogicalSubscription>>> historyOfOperatorsForFragmentation, 
			Collection<ILogicalOperator> historyOfOperatorsForReunion, Optional<ILogicalQueryPart> originPartOfTarget, 
			List<ILogicalQueryPart> queryPartsOfCopiedTargets, List<String> modificationParameters) throws NullPointerException, QueryPartModificationException {

		// Preconditions
		if (originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if (copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if (originSink == null)
			throw new NullPointerException("The origin sink must be not null!");
		else if (copiesOfOriginSink == null)
			throw new NullPointerException("The copied sinks must be not null!");
		else if (optSubscription == null)
			throw new NullPointerException("The optional of the subscription to modify must be not null!");
		else if (targets == null)
			throw new NullPointerException("The targets must be not null!");
		else if (historyOfOperatorsForFragmentation == null)
			throw new NullPointerException("The history of inserted operator for fragmentation must be not null!");
		else if (historyOfOperatorsForReunion == null)
			throw new NullPointerException("The history of inserted operator for reunion must be not null!");
		else if (modificationParameters == null)
			throw new NullPointerException("Modification parameters must be not null!");

		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);

		// Create operator for reunion
		ILogicalOperator operatorForReunion = this.createOperatorForReunion();

		int sinkInPort = 0;
		int sourceOutPort = 0;
		SDFSchema schema = originSink.getOutputSchema();
		if (optSubscription.isPresent()) {

			sourceOutPort = optSubscription.get().getSourceOutPort();
			sinkInPort = optSubscription.get().getSinkInPort();
			schema = optSubscription.get().getSchema();

		}

		// Subscribe the operator for reunion to the copied sinks
		for (int sinkNo = 0; sinkNo < copiesOfOriginSink.size(); sinkNo++) {

			ILogicalOperator copiedSink = ((List<ILogicalOperator>) copiesOfOriginSink).get(sinkNo);
			copiedSink.subscribeSink(operatorForReunion, sinkNo, sourceOutPort, schema);

		}

		// Subscribe the targets to the operator for reunion
		for (ILogicalOperator target : targets)
			operatorForReunion.subscribeSink(target, sinkInPort, 0, schema);

		AbstractFragmentationQueryPartModificator.log.debug("Inserted an operator for reunion between {} and {}", copiesOfOriginSink, targets);
		historyOfOperatorsForReunion.add(operatorForReunion);
		
		if(targets.size() == 1 && originPartOfTarget.isPresent() && !queryPartsOfCopiedTargets.isEmpty()) {
			
			final ILogicalQueryPart partOfCopiedTarget = queryPartsOfCopiedTargets.get(0);
		
			// Create modified query part
			Collection<ILogicalOperator> operatorsWithReunion = Lists.newArrayList(partOfCopiedTarget.getOperators());
			operatorsWithReunion.add(operatorForReunion);
			Collection<ILogicalQueryPart> modifiedQueryParts = Lists.newArrayList();
			for(ILogicalQueryPart part : modifiedCopiesToOrigin.get(originPartOfTarget.get())) {
				
				if(!part.equals(partOfCopiedTarget))
					modifiedQueryParts.add(part);
				
			}
				
			ILogicalQueryPart reunionPart = new LogicalQueryPart(operatorsWithReunion);
			modifiedQueryParts.add(reunionPart);
			modifiedCopiesToOrigin.put(originPartOfTarget.get(), modifiedQueryParts);
			modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.unionPartOfFragmentationWithGivenPart(reunionPart, modifiedCopiesToOrigin, operatorForReunion, historyOfOperatorsForFragmentation);
			
		} else {

			// Create the query part for the operator for reunion
			ILogicalQueryPart reunionPart = new LogicalQueryPart(operatorForReunion);
			Collection<ILogicalQueryPart> copiesOfReunionPart = Lists.newArrayList(reunionPart);
			modifiedCopiesToOrigin.put(reunionPart, copiesOfReunionPart);
			modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.unionPartOfFragmentationWithGivenPart(reunionPart, modifiedCopiesToOrigin, operatorForReunion, historyOfOperatorsForFragmentation);
			
		}
		
		return modifiedCopiesToOrigin;

	}

	/**
	 * Creates an operator for reunion.
	 * 
	 * @return The created operator for reunion.
	 */
	protected abstract ILogicalOperator createOperatorForReunion();

	/**
	 * Handles not yet processed subscriptions from or to a part no relevant for
	 * fragmentation.
	 * 
	 * @param originPart
	 *            The query part to modify.
	 * @param copiesToOrigin
	 *            A mapping of copies to origin query parts.
	 * @param partsToBeFragmented
	 *            A collection of all query parts to be fragmented.
	 * @param sourceName
	 *            The name of the source to be fragmented.
	 * @param historyOfOperatorsForFragmentation
	 *            The history for origin relative sources (key), inserted
	 *            operators for fragmentation (value.getE1()) and the
	 *            subscription to the origin relative source (value.getE2()).
	 * @return A modified mapping of copies to origin query parts.
	 * @throws NullPointerException
	 *             if <code>originPart</code>, <code>copiesToOrigin</code>,
	 *             <code>partsToBeFragmented</code> or
	 *             <code>historyOfOperatorsForFragmentation</code> is null.
	 */
	protected Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> attachOtherParts(ILogicalQueryPart originPart, Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, Collection<ILogicalQueryPart> partsToBeFragmented,
			String sourceName, Map<ILogicalOperator, Collection<IPair<ILogicalOperator, LogicalSubscription>>> historyOfOperatorsForFragmentation) throws NullPointerException {

		// Preconditions
		if (originPart == null)
			throw new NullPointerException("Origin query part for attachment must be not null!");
		else if (copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if (partsToBeFragmented == null)
			throw new NullPointerException("Collection of parts to be fragmented must be not null!");
		else if (historyOfOperatorsForFragmentation == null)
			throw new NullPointerException("The history of inserted operator for fragmentation must be not null!");
		else if (sourceName == null)
			throw new NullPointerException("Name of the source to be fragmented must be not null!");

		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);

		// Collect all relative sources
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSources = LogicalQueryHelper.collectRelativeSources(originPart, modifiedCopiesToOrigin.get(originPart));

		// Collect all relative sinks
		Map<ILogicalOperator, Collection<ILogicalOperator>> copiedToOriginSinks = LogicalQueryHelper.collectRelativeSinks(originPart, modifiedCopiesToOrigin.get(originPart));

		// Process each relative source
		for (ILogicalOperator originSource : copiedToOriginSources.keySet()) {

			if (originSource.getSubscribedToSource().isEmpty()) {
				continue;
			}

			for (LogicalSubscription subToSource : originSource.getSubscribedToSource()) {

				if (originPart.getOperators().contains(subToSource.getTarget()))
					continue;

				Collection<ILogicalOperator> copiedSources = copiedToOriginSources.get(originSource);

				ILogicalOperator target = subToSource.getTarget();
				Collection<ILogicalOperator> targets = Lists.newArrayList();
				Optional<ILogicalQueryPart> optPartOfOriginTarget = LogicalQueryHelper.determineQueryPart(modifiedCopiesToOrigin.keySet(), target);
				if (!optPartOfOriginTarget.isPresent())
					targets.add(target);
				else if (partsToBeFragmented.contains(optPartOfOriginTarget.get())) {

					// Done by the insertion of the operator for reunion
					continue;

				} else
					targets.addAll(LogicalQueryHelper.collectCopies(optPartOfOriginTarget.get(), modifiedCopiesToOrigin.get(optPartOfOriginTarget.get()), target));

				modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.connect(modifiedCopiesToOrigin, targets, subToSource, copiedSources);

			}

		}

		// Process each relative sink
		for (ILogicalOperator originSink : copiedToOriginSinks.keySet()) {

			if (originSink.getSubscriptions().isEmpty()) {
				continue;
			}

			for (LogicalSubscription subToSink : originSink.getSubscriptions()) {

				if (originPart.getOperators().contains(subToSink.getTarget()))
					continue;

				Collection<ILogicalOperator> copiedSinks = copiedToOriginSinks.get(originSink);

				ILogicalOperator target = subToSink.getTarget();
				Collection<ILogicalOperator> targets = Lists.newArrayList();
				Optional<ILogicalQueryPart> optPartOfOriginTarget = LogicalQueryHelper.determineQueryPart(modifiedCopiesToOrigin.keySet(), target);
				if (!optPartOfOriginTarget.isPresent())
					targets.add(target);
				else if (AbstractFragmentationQueryPartModificator.isSourceOperator(originSink, sourceName)) {

					LogicalSubscription subscription = new LogicalSubscription(originSink, subToSink.getSinkInPort(), subToSink.getSourceOutPort(), subToSink.getSchema());

					IPair<ILogicalOperator, ILogicalQueryPart> operatorForFragmentation = AbstractFragmentationQueryPartModificator.searchOperatorForFragmentation(modifiedCopiesToOrigin.keySet(), historyOfOperatorsForFragmentation, subscription,
							target);

					targets.add(operatorForFragmentation.getE1());
					modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.connect(modifiedCopiesToOrigin, copiedSinks, subToSink, targets);
					targets.iterator().next().initialize();

					modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.unionPartOfFragmentationWithGivenPart(originPart, modifiedCopiesToOrigin, originSink, historyOfOperatorsForFragmentation);

					continue;

				} else
					targets.addAll(LogicalQueryHelper.collectCopies(optPartOfOriginTarget.get(), modifiedCopiesToOrigin.get(optPartOfOriginTarget.get()), target));

				modifiedCopiesToOrigin = AbstractFragmentationQueryPartModificator.connect(modifiedCopiesToOrigin, copiedSinks, subToSink, targets);

			}

		}

		return modifiedCopiesToOrigin;

	}

	/**
	 * Checks, if the connection between two relevant query parts can be
	 * optimized (direct connections, neither operator for reunion nor operator
	 * for fragmentation).
	 * 
	 * @param originPart
	 *            The query part to modify.
	 * @param copiesToOrigin
	 *            A mapping of copies to origin query parts.
	 * @param originSink
	 *            The origin, relative sink.
	 * @param copiesOfOriginSink
	 *            The copies of <code>originSink</code>.
	 * @param subscription
	 *            The origin subscription of <code>originSink</code>.
	 * @param modificatorParameters
	 *            The parameters for the modification given by the user without
	 *            the parameter <code>fragmentation-strategy-name</code>.
	 * @throws QueryPartModificationException
	 *             if any error occurs.
	 */
	protected abstract boolean canOptimizeSubscription(ILogicalQueryPart originPart, Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, ILogicalOperator originSink, Collection<ILogicalOperator> copiesOfOriginSink,
			LogicalSubscription subscription, List<String> modificationParameters) throws QueryPartModificationException;

	/**
	 * Compares the degree of fragmentation given by the user with the number of
	 * available peers. <br />
	 * If there are not enough peers available a warning will be logged.
	 * 
	 * @param numQueryParts
	 *            The number of query parts, which shall be fragmented.
	 * @param degreeOfFragmentation
	 *            The degree of fragmentation given by the user.
	 * @throws NullPointerException
	 *             if no {@link IP2PDictionary} could be found.
	 * @throws IllegalArgumentException
	 *             if <code>numQueryParts</code> is less than one or if
	 *             <code>degreeOfFragmentation</code> is less
	 *             {@value #min_degree}.
	 */
	private static void validateDegreeOfFragmentation(int numQueryParts, int degreeOfFragmentation) throws IllegalArgumentException, NullPointerException {

		// Preconditions 1
		if (numQueryParts < 1)
			throw new IllegalArgumentException("At least one query part is needed for fragmentation!");
		else if (degreeOfFragmentation < AbstractFragmentationQueryPartModificator.min_degree)
			throw new IllegalArgumentException("Degree of fragmentation must be at least " + AbstractFragmentationQueryPartModificator.min_degree + "!");

		// The bound IP2PDictionary
		Optional<IP2PDictionary> optDict = Activator.getP2PDictionary();

		// Preconditions 2
		if (!optDict.isPresent())
			throw new NullPointerException("No P2PDictionary available!");

		// Check number of available peers (inclusive the local one)
		int numRemotePeers = optDict.get().getRemotePeerIDs().size();
		if (numRemotePeers + 1 < degreeOfFragmentation * numQueryParts) {

			AbstractFragmentationQueryPartModificator.log.warn("Fragmentation leads to at least {} query parts, " + "but there are only {} peers available. Consider to provide more peers. "
					+ "For the given configuration some query parts will be executed on the same peer.", degreeOfFragmentation * numQueryParts, numRemotePeers + 1);

		}

		AbstractFragmentationQueryPartModificator.log.debug("Degree of fragmentation set to {}.", degreeOfFragmentation);

	}

	/**
	 * Determines all query parts to be fragmented. <br />
	 * Origin parts within <code>queryParts</code> may be split.
	 * 
	 * @param queryParts
	 *            A collection of query parts to modify.
	 * @param sourceName
	 *            The name of the source to be fragmented.
	 * @return A pair of query parts to be fragmented (e1) and other parts (e2).
	 * @throws NullPointerException
	 *             if <code>queryParts</code> or <code>sourceName</code> is
	 *             null.
	 */
	private IPair<Collection<ILogicalQueryPart>, Collection<ILogicalQueryPart>> determinePartsToBeFragmented(Collection<ILogicalQueryPart> queryParts, String sourceName) throws NullPointerException {

		// Preconditions
		if (queryParts == null)
			throw new NullPointerException("Query parts for modification must be not null!");
		else if (sourceName == null)
			throw new NullPointerException("Name of the source to be fragmented must be not null!");

		// The return value
		Collection<ILogicalQueryPart> partsToBeFragmented = Lists.newArrayList();
		Collection<ILogicalQueryPart> otherParts = Lists.newArrayList();

		for (ILogicalQueryPart part : queryParts) {

			Collection<ILogicalOperator> relevantOperators = this.determineRelevantOperators(part, sourceName);
			if (relevantOperators.size() == part.getOperators().size()) {

				partsToBeFragmented.add(part);
				AbstractFragmentationQueryPartModificator.log.debug("Found {} as a part to be fragmented", part);

			} else if (!relevantOperators.isEmpty()) {
				AbstractFragmentationQueryPartModificator.log.debug("Split {} to form a fragmentable part as well as a non-fragmentable part", part);

				Collection<ILogicalOperator> allOperatorsOfPart = part.getOperators();
				part.removeAllOperators();
				part.addOperators(relevantOperators);
				partsToBeFragmented.add(part);

				Collection<ILogicalOperator> irrelevantOperators = Lists.newArrayList(allOperatorsOfPart);
				irrelevantOperators.removeAll(relevantOperators);

				ILogicalQueryPart otherPart = new LogicalQueryPart(irrelevantOperators);
				otherParts.add(otherPart);

				AbstractFragmentationQueryPartModificator.log.debug("Fragmentable part is {}", part);
				AbstractFragmentationQueryPartModificator.log.debug("Non-fragmentable part is {}", otherPart);

			} else {

				otherParts.add(part);
				AbstractFragmentationQueryPartModificator.log.debug("Found {} as an irrelevant part", part);

			}

		}

		return new Pair<Collection<ILogicalQueryPart>, Collection<ILogicalQueryPart>>(partsToBeFragmented, otherParts);

	}
	
	/**
	 * Checks if an operator delivers the data stream to be fragmented.
	 * @param operator The operator to check.
	 * @param sourceName The name of the source to be fragmented.
	 * @return True, if the operator is an {@link AbstractAccessAO} with sourceName as resource name or
	 * if the operator has sourceName as unique identifier or
	 * if the operator is a {@link RenameAO} with an operator as source to be one of those mentioned above.
	 */
	protected static boolean isSourceOperator(ILogicalOperator operator, String sourceName) {
		
		Preconditions.checkNotNull(operator, "Operator must be not null!");
		Preconditions.checkNotNull(sourceName, "Source name must be not null!");
		
		if(operator instanceof AbstractAccessAO && ((AbstractAccessAO) operator).getAccessAOName().getResourceName().equals(sourceName))
			return true;
		else if(operator.getUniqueIdentifier() != null && operator.getUniqueIdentifier().equals(sourceName))
			return true;
		else if(operator instanceof RenameAO) {
			
			Preconditions.checkArgument(operator.getSubscribedToSource().size() == 1, "RenameAO must have exact one subscription to source!");
			final ILogicalOperator target = operator.getSubscribedToSource().iterator().next().getTarget();
			
			return AbstractFragmentationQueryPartModificator.isSourceOperator(target, sourceName);
			
		}
		
		return false;
		
	}

	/**
	 * Determine all relevant operators for fragmentation of a given query part.
	 * 
	 * @param queryPart
	 *            The query part to modify.
	 * @param sourceName
	 *            The name of the source to be fragmented.
	 * @return A collection of all operators relevant for fragmentation. Those
	 *         operators have a data stream as an input, which depend on the
	 *         source to be fragmented.
	 * @throws NullPointerException
	 *             if <code>queryPart</code> or <code>sourceName</code> is null.
	 */
	protected Collection<ILogicalOperator> determineRelevantOperators(ILogicalQueryPart queryPart, String sourceName) throws NullPointerException {

		// Preconditions
		if (queryPart == null)
			throw new NullPointerException("Query part for modification must be not null!");
		else if (sourceName == null)
			throw new NullPointerException("Name of the source to be fragmented must be not null!");

		// The return value
		Collection<ILogicalOperator> relevantOperators = Lists.newArrayList();

		for (ILogicalOperator operator : queryPart.getOperators()) {
			
			if(AbstractFragmentationQueryPartModificator.isSourceOperator(operator, sourceName))
				continue;
			else if(operator.isSinkOperator() && !operator.isSourceOperator())
				continue;
			else if(operator instanceof RenameAO) {
				
				Preconditions.checkArgument(operator.getSubscribedToSource().size() == 1, "RenameAO must have exact one subscription to source!");
				final ILogicalOperator target = operator.getSubscribedToSource().iterator().next().getTarget();
				
				if(AbstractFragmentationQueryPartModificator.isOperatorRelevant(target, sourceName))
					relevantOperators.add(operator);
				
			} else if(AbstractFragmentationQueryPartModificator.isOperatorRelevant(operator, sourceName))
				relevantOperators.add(operator);

		}

		return relevantOperators;

	}

	/**
	 * Checks, if an operator is relevant for fragmentation.
	 * 
	 * @param operator
	 *            The operator to check.
	 * @param sourceName
	 *            The name of the source to be fragmented.
	 * @return true, if the operator is an access operator for the given source
	 *         or if any incoming data stream depends on the given source.
	 * @throws NullPointerException
	 *             if <code>operator</code> or <code>sourceName</code> is null.
	 */
	private static boolean isOperatorRelevant(ILogicalOperator operator, String sourceName) throws NullPointerException {

		// Preconditions
		if (operator == null)
			throw new NullPointerException("Operator to process must be not null!");
		else if (sourceName == null)
			throw new NullPointerException("Name of the source to be fragmented must be not null!");

		if(AbstractFragmentationQueryPartModificator.isSourceOperator(operator, sourceName))
			return true;

		for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {

			if(AbstractFragmentationQueryPartModificator.isOperatorRelevant(subToSource.getTarget(), sourceName))
				return true;

		}

		return false;

	}

	/**
	 * Determines the degree of fragmentation given by the user (Odysseus
	 * script). <br />
	 * #PEER_MODIFICATION <code>fragmentation-strategy-name</code>
	 * <code>source-name</code> <code>degree</code>
	 * 
	 * @param modificatorParameters
	 *            The parameters for the modification given by the user without
	 *            the parameter <code>fragmentation-strategy-name<code/>.
	 * @return The degree of fragmentation given by the user.
	 * @throws NullPointerException
	 *             if <code>modificatorParameters</code> is null.
	 */
	protected int determineDegreeOfFragmentation(List<String> modificatorParameters) throws NullPointerException {

		// Preconditions 1
		if (modificatorParameters == null)
			throw new NullPointerException("Parameters for query part fragmentation strategy must not be null!");

		// The return value
		int degreeOfFragmentation = -1;

		// Preconditions 2
		if (modificatorParameters.size() < 2) {

			AbstractFragmentationQueryPartModificator.log.warn("Parameters for query part fragmentation strategy does not contain the degree of fragmentation! " + "The degree of fragmentation is set to {}.",
					AbstractFragmentationQueryPartModificator.min_degree);
			degreeOfFragmentation = 2;

		} else
			try {

				degreeOfFragmentation = Integer.parseInt(modificatorParameters.get(1));

				// Preconditions 3
				if (degreeOfFragmentation < AbstractFragmentationQueryPartModificator.min_degree) {

					AbstractFragmentationQueryPartModificator.log.warn("Second parameter for query part fragmentation strategy, the degree of fragmentation, " + "should be at least {}. The degree of fragmentation is set to {}.",
							AbstractFragmentationQueryPartModificator.min_degree, AbstractFragmentationQueryPartModificator.min_degree);
					degreeOfFragmentation = 2;

				}

			} catch (NumberFormatException e) {

				AbstractFragmentationQueryPartModificator.log.error("Second parameter for query part fragmentation strategy must be an integer!", e);
				AbstractFragmentationQueryPartModificator.log.warn("The degree of fragmentation is set to {}.", AbstractFragmentationQueryPartModificator.min_degree);
				return AbstractFragmentationQueryPartModificator.min_degree;

			}

		return degreeOfFragmentation;

	}

	/**
	 * Determines the name of the source to be fragmented given by the user
	 * (Odysseus script). <br />
	 * #PEER_MODIFICATION <code>fragmentation-strategy-name</code>
	 * <code>source-name</code> <code>degree</code> <br />
	 * This name may be the name of an {@link AbstractAccessAO} or the unique ID of an operator.
	 * 
	 * @param modificatorParameters
	 *            The parameters for the modification given by the user without
	 *            the parameter <code>fragmentation-strategy-name</code>.
	 * @return The name of the source given by the user.
	 * @throws NullPointerException
	 *             if <code>modificatorParameters</code> is null.
	 * @throws IllegalArgumentException
	 *             if <code>modificatorParameters</code> is empty.
	 */
	protected String determineSourceName(List<String> modificatorParameters) throws NullPointerException, IllegalArgumentException {

		// Preconditions
		if (modificatorParameters == null)
			throw new NullPointerException("Parameters for query part modificator must not be null!");
		else if (modificatorParameters.isEmpty())
			throw new IllegalArgumentException("Parameters for query part fragmentation stratgey does neither contain " + "the name of the source to be fragmented nor the degree of fragmentation!");

		return modificatorParameters.get(0);

	}

	/**
	 * Determines the attributes given by the user (Odysseus script), if there
	 * are any.
	 * 
	 * @param modificatorParameters
	 *            The parameters for the modification given by the user without
	 *            the name of the fragmentation strategy.
	 */
	protected abstract Optional<List<String>> determineKeyAttributes(List<String> modificationParameters);

	/**
	 * Unions a query part, which contains only an operator for fragmentation,
	 * with a given part.
	 * 
	 * @param originPart
	 *            The query part to modify.
	 * @param copiesToOrigin
	 *            A mapping of copies to origin query parts.
	 * @param relativeSink
	 *            The relative sink, after which the operator for fragmentation
	 *            has been inserted.
	 * @param historyOfOperatorsForFragmentation
	 *            The history for origin relative sources (key), inserted
	 *            operators for fragmentation (value.getE1()) and the
	 *            subscription to the origin relative source (value.getE2()).
	 * @return A modified mapping of copies to origin query parts.
	 * @throws NullPointerException
	 *             if <code>originPart</code>, <code>copiesToOrigin</code>,
	 *             <code>relativeSink</code> or
	 *             <code>historyOfOperatorsForFragmentation</code> is null.
	 * @throws IllegalArgumentException
	 *             if there is more than one copy of <code>originPart</code>.
	 */
	protected static Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> unionPartOfFragmentationWithGivenPart(ILogicalQueryPart originPart, Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copiesToOrigin, ILogicalOperator relativeSink,
			Map<ILogicalOperator, Collection<IPair<ILogicalOperator, LogicalSubscription>>> historyOfOperatorsForFragmentation) throws NullPointerException, IllegalArgumentException {

		// Preconditions
		if (originPart == null)
			throw new NullPointerException("Origin query part for modification must be not null!");
		else if (copiesToOrigin == null)
			throw new NullPointerException("Mapping of copies to origin query parts must be not null!");
		else if (copiesToOrigin.get(originPart).size() != 1)
			throw new IllegalArgumentException("There must be exactly one copy of the origin query part!");
		else if (relativeSink == null)
			throw new NullPointerException("The relative sink must be not null!");
		else if (historyOfOperatorsForFragmentation == null)
			throw new NullPointerException("The history of inserted operator for fragmentation must be not null!");

		// The return value
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> modifiedCopiesToOrigin = Maps.newHashMap(copiesToOrigin);

		for (ILogicalOperator relSource : historyOfOperatorsForFragmentation.keySet()) {

			for (IPair<ILogicalOperator, LogicalSubscription> pair : historyOfOperatorsForFragmentation.get(relSource)) {

				if (pair.getE2().getTarget().equals(relativeSink)) {

					ILogicalQueryPart partOfFragmentation = LogicalQueryHelper.determineQueryPart(modifiedCopiesToOrigin.keySet(), pair.getE1()).get();
					modifiedCopiesToOrigin.remove(partOfFragmentation);

					Collection<ILogicalOperator> modifiedOperatorList = Lists.newArrayList(modifiedCopiesToOrigin.get(originPart).iterator().next().getOperators());
					modifiedOperatorList.add(pair.getE1());
					Collection<ILogicalQueryPart> modifiedParts = Lists.newArrayList((ILogicalQueryPart) new LogicalQueryPart(modifiedOperatorList));
					modifiedCopiesToOrigin.put(originPart, modifiedParts);

				}

			}

		}

		return modifiedCopiesToOrigin;

	}

}