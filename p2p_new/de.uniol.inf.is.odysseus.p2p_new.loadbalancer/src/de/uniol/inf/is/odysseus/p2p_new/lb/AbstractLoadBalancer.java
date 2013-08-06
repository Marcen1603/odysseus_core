package de.uniol.inf.is.odysseus.p2p_new.lb;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.IDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDistributionType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterFragmentationType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.SimplePlanPrinter;
import de.uniol.inf.is.odysseus.p2p_new.distribute.DistributionHelper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPart;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPartController;
import de.uniol.inf.is.odysseus.p2p_new.lb.fragmentation.Replication;
import de.uniol.inf.is.odysseus.p2p_new.lb.logicaloperator.ReplicationMergeAO;
import de.uniol.inf.is.odysseus.p2p_new.lb.service.P2PDictionaryService;

/**
 * The class for abstract load balancers. <br />
 * A load balancer distributes queries and/or sources on a network of peers.
 * @author Michael Brand
 */
public abstract class AbstractLoadBalancer implements ILogicalQueryDistributor {

	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(AbstractLoadBalancer.class);
	
	/**
	 * The counter for connections to other peers.
	 */
	private static int connectionCounter = 0;
	
	/**
	 * The index within a collection of {@link PeerID}s, which indicates the 
	 * remote peer, to which the next {@link QueryPart} shall be assigned.
	 */
	private static int peerCounter = 0;
	
	/**
	 * The parameter to determine that the minimum degree of parallelism should be used.
	 */
	public static final String MIN_DEGREE_PARAM = "min";
	
	/**
	 * The minimum value of parallelism.
	 */
	public static final int MIN_DEGREE_VALUE = 1;
	
	/**
	 * The parameter to determine that the maximum degree of parallelism should be used.
	 */
	public static final String MAX_DEGREE_PARAM = "max";
	
	/**
	 * The maximum value of parallelism. <br />
	 * This leads to the usage of the number of available remote Peers as the degree of parallelism.
	 */
	public static final int MAX_DEGREE_VALUE = Integer.MAX_VALUE;
	
	/**
	 * Returns the base name for acceptor operators.
	 */
	protected static String getAccessName() {
		
		return "JxtaReceiver_";
		
	}

	/**
	 * Returns the base name for sender operators.
	 */
	protected static String getSenderName() {
		
		return "JxtaSender_";
		
	}
	
	/**
	 * Returns the identifier of the local peer.
	 */
	protected static String getLocalDestinationName() {
		
		return "local";
		
	}
	
	/**
	 * Returns the identifier of the source manager peer.
	 */
	protected static String getSourceDestinationName() {
		
		return "source manager";
		
	}

	/**
	 * Returns the name of this distributor. It can be used as a parameter in a keyword to use this distributor.
	 */
	@Override
	public abstract String getName();
	
	/**
	 * Returns all {@link ILogicalQuery}s to be executed locally. This will be all {@link IlogicalQuery}s within <code>queries</code> modified due 
	 * to the distribution plus copies of the {@link ILogicalQuery}s as the case may be. The returned {@link ILogicalQuerie}s will be semantically 
	 * equivalent to those in <code>queries</code>.
	 */
	// TODO Probleme mit Aggregationen bschreiben
	// TODO Aggregationen rausnehmen und in den Ausblick
	// TODO Problem mit mehreren Fragmentierungen beschreiben
	@Override
	public List<ILogicalQuery> distributeLogicalQueries(IExecutor sender,
			List<ILogicalQuery> queries, QueryBuildConfiguration cfg) {
		
		// Preconditions
		Preconditions.checkNotNull(cfg, "cfg must be not null!");
		if(queries == null || queries.isEmpty()) {
			
			// Nothing to distribute
			return queries;
			
		}
		
		// Variables
		Collection<String> sourceNames;							// List of all source names
		Map<String, IDataFragmentation> sourceToFragStrat;		// Map of source names and fragmentation strategies
		Map<ILogicalOperator, Integer> fragOPToNumUsedOutputs =
				Maps.newHashMap();								// Map of operators for fragmentation and the number of used outputs of them
		boolean replicationOnly = true;							// true, if no fragmentation is used
		int degreeOfParallelism;								// The degree of parallelization is also the number of fragments, 
																// if fragmentation is used
																// All queryparts over all queries:
		final Map<ILogicalQuery, 								// Outer map keys: the origin queries
				Map<ILogicalQuery, 								// Inner map keys: the copies of one origin query
						List<QueryPart>>> queryPartsMap;		// Inner map values: a list of queryparts belonging to the copy
		
		// Collect source names
		sourceNames = this.collectSourceNames(queries);
		
		// Check, if any fragmentation strategy is defined	
		if(cfg.get(ParameterDoDataFragmentation.class).getValue()) {
			
			sourceToFragStrat = this.getFragmentationStrategies((IServerExecutor) sender, cfg, sourceNames);
			
			// check for replication only
			for(IDataFragmentation strat : sourceToFragStrat.values()) {
				
				if(!(strat instanceof Replication)) {
					
					replicationOnly = false;
					break;
					
				}
				
			}
		
		} else {
			
			// Replication only
			sourceToFragStrat = Maps.newHashMap();
			for(String sourceName : sourceNames)
				sourceToFragStrat.put(sourceName, new Replication());
			
		}
		
		// Get all available peers
		final Collection<PeerID> remotePeerIDs = P2PDictionaryService.get().getRemotePeerIDs();
		if(remotePeerIDs.isEmpty()) {
			
			LOG.debug("Could not find any remote peers to distribute logical query. Executing all locally.");
			return queries;
			
		} 		
		DistributionHelper.logPeerStatus(remotePeerIDs);
		
		// Read out the wanted degree of parallelism from the given distribution type parameter
		degreeOfParallelism = this.getDegreeOfParallelismn(this.determineWantedDegreeOfParallelism(cfg), remotePeerIDs.size());
		
		// There must be at least 2 fragments
		if(!replicationOnly && degreeOfParallelism < 2) {
			
			LOG.error("Degree of parallelism must be at least 2 to use data fragmentation. Turned off data fragmentation.");
			sourceToFragStrat.clear();
			for(String sourceName : sourceNames)
				sourceToFragStrat.put(sourceName, new Replication());
			replicationOnly = true;
			
		}
		
		queryPartsMap = Maps.newHashMap();
		for(final ILogicalQuery originQuery : queries) {
			
			// Variables
			QueryPart localPart = null;									// the query part to be executed locally
			Collection<QueryPart> sourceParts = Lists.newArrayList();	// the query parts for all StreamAOs, WindowAOs and operators for fragmentation,
																		// if fragmentation is used
			List<ILogicalQuery> queryCopies = Lists.newArrayList();		// A list of all copies of the originquery
			
			// Add an entry for the origin query to the queryPartsMap
			queryPartsMap.put(originQuery, new HashMap<ILogicalQuery, List<QueryPart>>());			
			
			// Make copies of the query
			for(int copyNo = 0; copyNo < degreeOfParallelism; copyNo++)
				queryCopies.add(DistributionHelper.copyLogicalQuery(originQuery));

			for(ILogicalQuery queryCopy: queryCopies) {
				
				// Variables
				List<QueryPart> parts;									// A list of all query parts of the copy, except the source part and the local part
				List<ILogicalOperator> operators = Lists.newArrayList();// A list of all operators of the query
				
				// Get all logical operators of the query and remove the TopAOs
				RestructHelper.collectOperators(queryCopy.getLogicalPlan(), operators);
				RestructHelper.removeTopAOs(operators);
				
				if(!replicationOnly) {
					
					if(sourceParts.isEmpty()) {
						
						// Create the source parts
						sourceParts = this.createSourceParts(operators);
						
						// Insert the operators for fragmentation
						Collection<QueryPart> enhancedSourceParts = Lists.newArrayList();
						for(QueryPart sourcePart : sourceParts)
							enhancedSourceParts.add(this.insertOperatorsForFragmentation(
									sourcePart, sourceToFragStrat, fragOPToNumUsedOutputs, degreeOfParallelism, cfg));
						sourceParts.clear();
						sourceParts.addAll(enhancedSourceParts);
						
					} else {
					
						// Subscribe copy to source parts
						this.subscribeCopyToSourceParts(operators, sourceParts, sourceToFragStrat, fragOPToNumUsedOutputs);
						
					}
				
				}
				
				if(localPart == null) {
					
					// Create the local part
					localPart = this.createLocalPart(operators, degreeOfParallelism, sourceToFragStrat);
					
				} else {

					// Subscribe copy to local part
					this.subscribeCopyToLocalPart(operators, degreeOfParallelism, localPart, sourceToFragStrat);
					
				}
				
				// Create the queryparts of this query
				parts = this.determineQueryParts(operators, localPart);
				LOG.debug("Got {} parts of logical query {}", parts.size(), originQuery);
				queryPartsMap.get(originQuery).put(queryCopy, parts);
				
			}
			
			// Add all operators of the source and the local part ONCE
			if(!replicationOnly && !sourceParts.isEmpty()) {
			
				for(QueryPart sourcePart : sourceParts) {
					
					this.initPart(sourcePart);
					queryPartsMap.get(originQuery).values().iterator().next().add(sourcePart);
					
				}
				
			}
			this.initPart(localPart);
			queryPartsMap.get(originQuery).values().iterator().next().add(localPart);
			
		}
		
		// Assign query parts to peers
		final Map<QueryPart, PeerID> queryPartDistributionMap = 
				this.assignQueryParts(remotePeerIDs, P2PDictionaryService.get().getLocalPeerID(), this.arrangeQueryParts(queryPartsMap));
		int connectionNo =  connectionCounter++;
		DistributionHelper.generatePeerConnections(queryPartDistributionMap, getAccessName() + connectionNo + "_", 
				getSenderName() + connectionNo + "_");
		
		// The queries to be executed locally
		final List<ILogicalQuery> localQueries = Lists.newArrayList();
		
		// Publish all remote parts and get the local ones
		for(ILogicalQuery originQuery : queryPartsMap.keySet()) {
			
			List<QueryPart> allQueryParts = Lists.newArrayList();
			for(List<QueryPart> queryPartsOfCopy : queryPartsMap.get(originQuery).values())
				allQueryParts.addAll(queryPartsOfCopy);
			localQueries.add(this.shareParts(allQueryParts, queryPartDistributionMap, cfg, originQuery.toString(), 
					sourceToFragStrat, degreeOfParallelism));
			
		}
		
		return localQueries;
		
	}
	
	/**
	 * Returns a collection of the names of all used sources.
	 */
	private Collection<String> collectSourceNames(List<ILogicalQuery> queries) {
		
		Collection<String> allSourceNames = Lists.newArrayList();
		
		for(ILogicalQuery query : queries) {
			
			Collection<String> sourceNames = DistributionHelper.getSourceNames(query.getLogicalPlan());
			
			for(String sourceName : sourceNames) {
				
				if(!allSourceNames.contains(sourceName))
					allSourceNames.add(sourceName);
				
			}
			
		}
		
		return allSourceNames;
		
	}

	/**
	 * Reads out the wanted degree of parallelism from the {@link QueryBuildConfiguration}.
	 * @param cfg The {@link QueryBuildConfiguration}.
	 * @return The wanted degree of parallelism (> 0).
	 */
	protected int determineWantedDegreeOfParallelism(QueryBuildConfiguration cfg) {
		
		// Preconditions
		Preconditions.checkNotNull(cfg, "cfg must be not null");
		Preconditions.checkNotNull(cfg.get(ParameterDistributionType.class).getValue(), "parameters for distributiontype must not be null");
		
		String[] strParameters = cfg.get(ParameterDistributionType.class).getValue().split(" ");	
		int wantedDegreeOfParallelism = MIN_DEGREE_VALUE;
		
		if(strParameters.length > 1) {
			
			if(strParameters[1].toLowerCase().equals(MIN_DEGREE_PARAM))
				wantedDegreeOfParallelism = MIN_DEGREE_VALUE;
			else if(strParameters[1].toLowerCase().equals(MAX_DEGREE_PARAM))
				wantedDegreeOfParallelism = MAX_DEGREE_VALUE;
			else try {
			
				wantedDegreeOfParallelism = Integer.parseInt(strParameters[1]);
				if(wantedDegreeOfParallelism < MIN_DEGREE_VALUE) {
					
					LOG.error("{} is an invalid degree of parallelism. Degree settet to {}", strParameters[1], MIN_DEGREE_VALUE);
					wantedDegreeOfParallelism = MIN_DEGREE_VALUE;
					
				}
				
			} catch(NumberFormatException e) {
				
				e.printStackTrace();
				LOG.error("Could not parse {} to an integer. Degree settet to {}", strParameters[1], MIN_DEGREE_VALUE);
				
			}
			
		}
		
		return wantedDegreeOfParallelism;
		
	}
	
	/**
	 * Splits an {@link ILogicalQuery} into a list of {@link QueryPart}s.
	 * @param operators The list of {@link ILogicalOperator}s representing the{@link ILogicalQuery}. <br />
	 * <code>operators</code> must not be null and not empty.
	 * @param localPart The local part of the {@link ILogicalQuery}.
	 * @return The list of {@link QueryPart}s.
	 */
	protected abstract List<QueryPart> determineQueryParts(List<ILogicalOperator> operators, QueryPart localPart);
	
	/**
	 * Arranges the {@link QueryPart}s, so that same {@link QueryPart}s of different {@link ILogicalQuery} copies are back-to-back.
	 * @param queryPartsMap All {@link QueryPart}s over all queries. <br />
	 * Outer map keys: the origin queries <br />
	 * Inner map keys: the copies of one origin query <br />
	 * Inner map values: a list of queryparts belonging to the copy
	 * @return A collection of all {@link QueryPart}s <code>queryPartsMap</code> contains arranged as same {@link QueryPart}s of 
	 * different {@link ILogicalQuery} copies are back-to-back.
	 */
	protected List<QueryPart> arrangeQueryParts(Map<ILogicalQuery, Map<ILogicalQuery, List<QueryPart>>> queryPartsMap) {
		
		Preconditions.checkNotNull(queryPartsMap, "queryPartsMap must be not null!");
		
		final List<QueryPart> arrangedParts = Lists.newArrayList();
		final List<Iterator<QueryPart>> partsIter = Lists.newArrayList();
		
		for(Map<ILogicalQuery, List<QueryPart>> partialQueryPartsMap : queryPartsMap.values()) {
			
			for(List<QueryPart> parts : partialQueryPartsMap.values())
				partsIter.add(parts.iterator());
			
		}
		
		boolean elementsLeft;
		do {
			
			elementsLeft = false;
			for(Iterator<QueryPart> iter : partsIter) {
				
				if(iter.hasNext()) {
					
					arrangedParts.add(iter.next());
					elementsLeft = true;
					
				}
				
			}
			
		} while(elementsLeft);
		
		return arrangedParts;
		
	}
	
	/**
	 * Maps each {@link QueryPart} to a peer except the local peer via round robin.
	 * @param remotePeerIDs The collection of all peer IDs. <br />
	 * <code>remotePeerIDs</code> must not be null and not empty.
	 * @param localPeerID The ID of the local peer. <br />
	 * <code>localPeerID</code> must not be null.
	 * @param queryParts The list of all {@link QueryPart}s. <br />
	 * <code>queryParts</code> must not be null.
	 * @return The mapping of the {@link QueryPart}s and the IDs of the the peers, where each {@link QueryPart} shall be stored.
	 */
	protected Map<QueryPart, PeerID> assignQueryParts(Collection<PeerID> remotePeerIDs, PeerID localPeerID, Collection<QueryPart> queryParts) { 
		
		Preconditions.checkNotNull(remotePeerIDs, "remotePeerIDs must be not null!");
		Preconditions.checkArgument(remotePeerIDs.size() > 0, "remotePeerIDs must be not empty!");
		Preconditions.checkNotNull(localPeerID, "localPeerID must be not null!");
		Preconditions.checkNotNull(queryParts, "queryParts must be not null!");
		
		final Map<QueryPart, PeerID> distributed = Maps.newHashMap();
		final Iterator<QueryPart> partsIter = queryParts.iterator();
		
		while(partsIter.hasNext()) {
			
			QueryPart part = partsIter.next();
			Optional<String> peerName;
			PeerID peerID = null;
				
			if(part.getDestinationName().isPresent() && part.getDestinationName().get().equals(AbstractLoadBalancer.getLocalDestinationName())) {
					
				// Local part
				distributed.put(part, localPeerID);
				peerName = P2PDictionaryService.get().getRemotePeerName(localPeerID);
				
			} else {
				
				// Round-Robin
				peerID = ((List<PeerID>) remotePeerIDs).get(peerCounter);
				peerCounter = (++peerCounter) % remotePeerIDs.size();
				
				peerName = P2PDictionaryService.get().getRemotePeerName(peerID);
				distributed.put(part, peerID);					
				
			}			
				
			if(peerName.isPresent())
				LOG.debug("Assign query part {} to peer {}", part, peerName.get());
			else LOG.debug("Assign query part {} to peer {}", part, peerID);
			
		}

		return distributed;
		
	}
	
	/**
	 * Creates a new {@link QueryPart} to be executed locally and makes the subscriptions for a single {@link ILogicalQuery}, 
	 * which {@link ILogicalOperator}s are <code>operators</code>.
	 * @param operators A collection of all {@link ILogicalOperator}s within an {@link ILogicalQuery} without any {@link TopAO}s.
	 * @param degreeOfParallelism The degree of parallelism. <br />
	 * <code>degreeOfParallelism</code> must greater than zero.
	 * @param sourceToFragStrat A mapping of source name and fragmentation strategy for that source.
	 * @return The returned collection will contain for each sink: <br />
	 * the sink, if <code>degreeOfParallelism</code> is <code>1</code> and {@link ILogicalOperator#needsLocalResources()} is true for the sink. <br />
	 * a {@link RenameAO} with no operation subscribed to the sink, if <code>degreeOfParallelism</code> is <code>1</code> and 
	 * {@link ILogicalOperator#needsLocalResources()} is false for the sink. <br />
	 * a {@link ReplicationMergeAO} subscribed to the sink, if <code>degreeOfParallelism</code> is greater than <code>1</code> and 
	 * {@link ILogicalOperator#needsLocalResources()} is false for the sink. <br />
	 * a {@link ReplicationMergeAO} subscribed to all sources of the sink and subscribed as a source to the sink, 
	 * if <code>degreeOfParallelism</code> is greater than <code>1</code> and {@link ILogicalOperator#needsLocalResources()} is true for the sink.
	 */
	protected QueryPart createLocalPart(Collection<ILogicalOperator> operators, int degreeOfParallelism, Map<String, 
			IDataFragmentation> sourceToFragStrat) {
		
		Preconditions.checkNotNull(operators, "operators must be not null!");
		Preconditions.checkArgument(degreeOfParallelism > 0, "degreeOfParallelism must be greater than zero!");
		Preconditions.checkNotNull(sourceToFragStrat, "sourceToFragStrat must be not null!");
		// Variables
		final List<ILogicalOperator> localOperators = Lists.newArrayList();	// the list of all operators which shall be in the local part
		ILogicalOperator mergeAO = sourceToFragStrat.values().iterator().next().createOperatorForJunction();
																			// the operator to merge the different streams
		
		// Get the operator to merge the different streams.
		// As there are only horizontal fragmentation strategies, which return an UnionAO, it is that UnionAO except for the situation 
		// that at least one source is going to be replicated
		for(IDataFragmentation fragStrat : sourceToFragStrat.values()) {
			
			if(fragStrat instanceof Replication) {
				
				mergeAO = fragStrat.createOperatorForJunction();
				break;
				
			}
			
		}
		
		for(ILogicalOperator operator : operators) {
			
			if(!operator.needsLocalResources() && !operator.getSubscriptions().isEmpty()) {
				
				// Not a sink of the query
				continue;
				
			} else if(operator.needsLocalResources() && degreeOfParallelism == 1) {	
				
				// Map operator to local part; neither merging nor renaming needed
				localOperators.add(operator);
				
			} else if(degreeOfParallelism == 1) {
				
				// Don't map operator to local part; no merging needed
				final RenameAO renameAO = new RenameAO();
				renameAO.setNoOp(true);
				renameAO.addParameterInfo("isNoOp", "'true'");
				localOperators.add(renameAO);
				renameAO.subscribeToSource(operator, 0, 0, operator.getOutputSchema());
				LOG.debug("Subscribed {} to {}", operator, renameAO);
				
			} else if(!operator.needsLocalResources() && degreeOfParallelism > 1) {
				
				// Don't map operator to local part; merging needed before
				localOperators.add(mergeAO);
				mergeAO.subscribeToSource(operator, 0, 0, operator.getOutputSchema());
				LOG.debug("Subscribed {} to {}", operator, mergeAO);
				
			} else if(operator.needsLocalResources()) {
				
				localOperators.add(operator);
				
				boolean firstOpWhichNeedsLocalRessources = false;
				for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {
					
					if(!subToSource.getTarget().needsLocalResources())
						firstOpWhichNeedsLocalRessources = true;
					
				}
				
				if(firstOpWhichNeedsLocalRessources) {
					
					// Map operator to local part; Merging needed before
					localOperators.add(mergeAO);			
					
					// Delete subscriptions from all sources of the operator towards it; subscribe them to mergeAO
					for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {
						
						operator.unsubscribeFromSource(subToSource);
						LOG.debug("Unsubscribed {} from {}", subToSource.getTarget(), operator);
						mergeAO.subscribeToSource(subToSource.getTarget(), mergeAO.getNumberOfInputs(), subToSource.getSourceOutPort(), 
								subToSource.getTarget().getOutputSchema());
						LOG.debug("Subscribed {} to {}", subToSource.getTarget(), mergeAO);
						
					}
					
					// Subscribe mergeAO to the operator
					mergeAO.subscribeSink(operator, 0, 0, mergeAO.getOutputSchema());
					LOG.debug("Subscribed {} to {}", mergeAO, operator);
					
				}
				
			}
			
		}
		
		operators.removeAll(localOperators);		
		QueryPart localPart = new QueryPart(localOperators, AbstractLoadBalancer.getLocalDestinationName());
		LOG.debug("Created local part {}", localPart);
		return localPart;
	
	}
	
	/**
	 * Creates a new {@link QueryPart}, which contains only accesses to sources and makes the subscriptions for a single {@link ILogicalQuery}, 
	 * which {@link ILogicalOperator}s are <code>operators</code>.
	 * @param operators A collection of all {@link ILogicalOperator}s within an {@link ILogicalQuery} without any {@link TopAO}s.
	 * @return The returned collection will contain every {@link StreamAO} and every {@link WindowAO} which is subscribed to 
	 * a {@link StreamAO}.
	 */
	protected Collection<QueryPart> createSourceParts(Collection<ILogicalOperator> operators) {
		
		Preconditions.checkNotNull(operators, "operators must be not null!");
		
		final List<QueryPart> sourceParts = Lists.newArrayList();
		final List<ILogicalOperator> sourceOperators = Lists.newArrayList();
		final List<ILogicalOperator> currentSourceOperators = Lists.newArrayList();
		
		for(ILogicalOperator operator : operators) {
			
			if(operator instanceof WindowAO) {
				
				boolean onlySubscribedToSourceAccesses = true;
				for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {
					
					if(!(subToSource.getTarget() instanceof StreamAO) && !(subToSource.getTarget() instanceof WindowAO)) {
						
						onlySubscribedToSourceAccesses = false;
						break;
						
					}
					
				}
				if(onlySubscribedToSourceAccesses)
					currentSourceOperators.add(operator);
				
			} else if(operator instanceof StreamAO) {
				
				currentSourceOperators.add(operator);
				
				QueryPart sourcePart = new QueryPart(currentSourceOperators, AbstractLoadBalancer.getSourceDestinationName());
				LOG.debug("Created source part {}", sourcePart);
				sourceParts.add(sourcePart);
				sourceOperators.addAll(currentSourceOperators);
				currentSourceOperators.clear();
				
			}
			
		}
		
		if(!currentSourceOperators.isEmpty()) {
		
			QueryPart sourcePart = new QueryPart(sourceOperators, AbstractLoadBalancer.getSourceDestinationName());
			LOG.debug("Created source part {}", sourcePart);
			sourceParts.add(sourcePart);
			sourceOperators.addAll(currentSourceOperators);
			
		}
		
		operators.removeAll(sourceOperators);
		return sourceParts;
	
	}
	
	/**
	 * Makes the subscriptions for a single {@link ILogicalQuery}, which {@link ILogicalOperator}s are <code>operators</code>, 
	 * to an existing source part and removes all {@link StreamAO}s and {@link WindowAO}s from <code>operators</code>.
	 * @param operators A collection of all {@link ILogicalOperator}s within an {@link ILogicalQuery} without any {@link TopAO}s.
	 * @param sourcePart The source part to which the {@link ILogicalQuery} shall be subscribed.
	 * @param sourceToFragStrat A mapping of source name and fragmentation strategy for that source.
	 * @param fragOpToNumUsedOutputs A mapping of fragmentation operators and the number of used output ports.
	 */
	protected void subscribeCopyToSourceParts(Collection<ILogicalOperator> operators, Collection<QueryPart> sourceParts, 
			Map<String, IDataFragmentation> sourceToFragStrat, Map<ILogicalOperator, Integer> fragOpToNumUsedOutputs) {
		
		Preconditions.checkNotNull(operators, "operators must be not null!");
		Preconditions.checkNotNull(sourceParts, "sourceParts must be not null!");
		Preconditions.checkArgument(!sourceParts.isEmpty(), "sourceParts must be not empty!");
		Preconditions.checkNotNull(sourceToFragStrat, "sourceToFragStrat must not be null!");
		Preconditions.checkArgument(sourceToFragStrat.size() > 0, "sourceToFragStrat must be not empty!");
		
		final List<ILogicalOperator> sourceOperators = Lists.newArrayList();
		
		// Iterator for the source parts
		Iterator<QueryPart> sourcePartsIter = sourceParts.iterator();
		QueryPart sourcePart = sourcePartsIter.next();
		Iterator<ILogicalOperator> sourceIter = sourcePart.getOperators().iterator();
		
		for(ILogicalOperator operator : operators) {
			
			if(operator instanceof WindowAO) {
				
				boolean onlySubscribedToSourceAccesses = true;
				for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {
					
					if(!(subToSource.getTarget() instanceof StreamAO) && !(subToSource.getTarget() instanceof WindowAO)) {
						
						onlySubscribedToSourceAccesses = false;
						break;
						
					}
					
				}
				if(onlySubscribedToSourceAccesses)
					sourceOperators.add(operator);
				
			} else if(operator instanceof StreamAO)
				sourceOperators.add(operator);				
			else {
				
				Collection<LogicalSubscription> subsToRemove = Lists.newArrayList();
				Collection<LogicalSubscription> subsToAdd = Lists.newArrayList();
				
				for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {
					
					ILogicalOperator target = subToSource.getTarget();
					
					if(!(target instanceof StreamAO) && !(target instanceof WindowAO))
						continue;
					else if(target instanceof WindowAO) {
						
						boolean onlySubscribedToSourceAccesses = true;
						for (LogicalSubscription sub : target.getSubscribedToSource()) {
							
							if(!(sub.getTarget() instanceof StreamAO) && !(sub.getTarget() instanceof WindowAO)) {
								
								onlySubscribedToSourceAccesses = false;
								break;
								
							}
							
						}
						if(!onlySubscribedToSourceAccesses)
							continue;
						
					}
					
					// StreamAO or WindowAO with subscriptions to StreamAOs
					Optional<String> optSourceName = DistributionHelper.getSourceName(target);
					if(!optSourceName.isPresent())
						continue;
					IDataFragmentation fragStrat = sourceToFragStrat.get(optSourceName.get());
					Class<? extends ILogicalOperator> classForDistribution;
					classForDistribution = fragStrat.getOperatorForDistributionClass();	// null for replication
					ILogicalOperator source = null;
					
					while(source == null || (classForDistribution != null && source.getClass() != classForDistribution)) {
						
						while(!sourceIter.hasNext()) {
							
							sourcePart = sourcePartsIter.next();
							sourceIter = sourcePart.getOperators().iterator();
							
						}
						source = sourceIter.next();
						
					}
					
					subsToRemove.add(subToSource);
					int sourceOutPort;
					if(classForDistribution != null) {
						
						sourceOutPort = fragOpToNumUsedOutputs.get(source);
						fragOpToNumUsedOutputs.put(source, sourceOutPort + 1);
						
					} else sourceOutPort = subToSource.getSourceOutPort(); // Replication
					
					subsToAdd.add(new LogicalSubscription(source, subToSource.getSinkInPort(), sourceOutPort, subToSource.getSchema()));
					
				}
				
				for(LogicalSubscription sub : subsToRemove) {
					
					operator.unsubscribeFromSource(sub);
					LOG.debug("Unsubscribed {} from {}", sub.getTarget(), operator);
					
				}
				for(LogicalSubscription sub : subsToAdd) {
					
					operator.subscribeToSource(sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
					LOG.debug("Subscribed {} to {}", sub.getTarget(), operator);
					
				}
				
			}
			
		}
		
		operators.removeAll(sourceOperators);
		
	}
	
	/**
	 * Makes the subscriptions for a single {@link ILogicalQuery}, which {@link ILogicalOperator}s are <code>operators</code>, to an existing local part.
	 * @param operators A collection of all {@link ILogicalOperator}s within an {@link ILogicalQuery} without any {@link TopAO}s.
	 * @param degreeOfParallelism The degree of parallelism. <br />
	 * <code>degreeOfParallelism</code> must greater than zero.
	 * @param localPart The local part to which the {@link ILogicalQuery} shall be subscribed.
	 * @param sourceToFragStrat A mapping of source name and fragmentation strategy for that source.
	 */
	protected void subscribeCopyToLocalPart(Collection<ILogicalOperator> operators, int degreeOfParallelism, QueryPart localPart, 
			Map<String,IDataFragmentation> sourceToFragStrat) {
		
		Preconditions.checkNotNull(operators, "operators must be not null!");
		Preconditions.checkArgument(degreeOfParallelism > 0, "degreeOfParallelism must be greater than zero!");
		Preconditions.checkNotNull(localPart, "localPart must be not null!");
		Preconditions.checkNotNull(sourceToFragStrat, "sourceToFragStrat must be not null!");
		
		// Variables
		final List<ILogicalOperator> localOperators = Lists.newArrayList();	// the list of all operators which shall be in the local part
		Class<? extends ILogicalOperator> mergeAOClass = null;				// the class of the operator to merge the different streams
		Iterator<ILogicalOperator> localIter = 
				localPart.getOperators().iterator();						// Iterator for the operators of the local Part
		
		// Get class of the the operator to merge the different streams.
		// As there are only horizontal fragmentation strategies, which return an UnionAO, it is that UnionAO except for the situation 
		// that at least one source is going to be replicated
		for(IDataFragmentation fragStrat : sourceToFragStrat.values()) {
			
			if(fragStrat instanceof Replication) {
				
				mergeAOClass = fragStrat.getOperatorForJunctionClass();
				break;
				
			}
			
		}
		if(mergeAOClass == null)
			mergeAOClass = sourceToFragStrat.values().iterator().next().getOperatorForJunctionClass();
		
		for(ILogicalOperator operator : operators) {
			
			if(!operator.needsLocalResources() && !operator.getSubscriptions().isEmpty()) {
				
				// Not a sink of the query
				continue;
				
			} else if(!operator.needsLocalResources() && degreeOfParallelism > 1) {
				
				// Subscribe operator to existing merger
				ILogicalOperator mergeAO = null;
				do mergeAO = localIter.next();
				while(mergeAO.getClass() != mergeAOClass && localIter.hasNext());
				
				mergeAO.subscribeToSource(operator, mergeAO.getNumberOfInputs(), 0, operator.getOutputSchema());
				LOG.debug("Subscribed {} to {}", operator, mergeAO);
				
			} else if(operator.needsLocalResources()) {
				
				localOperators.add(operator);
				
				boolean firstOpWhichNeedsLocalRessources = false;
				for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {
					
					if(!subToSource.getTarget().needsLocalResources())
						firstOpWhichNeedsLocalRessources = true;
					
				}
				
				if(firstOpWhichNeedsLocalRessources) {
					
					// Subscribe sources of operator to existing merger
					ILogicalOperator mergeAO = null;
					do mergeAO = localIter.next();
					while(mergeAO.getClass() != mergeAOClass && localIter.hasNext());
					
					for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {
						
						operator.unsubscribeFromSource(subToSource);
						LOG.debug("Unsubscribed {} from {}", subToSource.getTarget(), operator);
						mergeAO.subscribeToSource(subToSource.getTarget(), mergeAO.getNumberOfInputs(), subToSource.getSourceOutPort(), 
								subToSource.getTarget().getOutputSchema());
						LOG.debug("Subscribed {} to {}", subToSource.getTarget(), mergeAO);
						
					}
					
				}
				
			}
			
		}
		
		operators.removeAll(localOperators);
		
	}
	
	/**
	 * Calls {@link ILogicalOperator#initialize()} for all {@link ILogicalOperator}s within the {@link QueryPart}.
	 * @param part The {@link QueryPart}.
	 */
	protected void initPart(QueryPart part) {
		
		Preconditions.checkNotNull(part, "part must be not null!");
		
		for(ILogicalOperator operator : part.getOperators())
			operator.initialize();
		
	}
	
	/**
	 * Determines which {@link QueryPart}s shall be executed locally and publishes the rest on the mapped peers.
	 * @param queryPartDistributionMap The mapping of the {@link QueryPart}s and the IDs of the the peers, where each {@link QueryPart} 
	 * shall be stored. <br />
	 * <code>queryPartDistributionMap</code> must not be null.
	 * @param sharedQueryID The {@link ID} for query sharing. <br />
	 * <code>sharedQueryID</code> must not be null.
	 * @param cfg The name of the transport configuration. <br />
	 * <code>transCfg</code> must not be null.
	 */
	protected List<QueryPart> shareParts(Map<QueryPart, PeerID> queryPartDistributionMap, ID sharedQueryID, QueryBuildConfiguration cfg) {
		
		Preconditions.checkNotNull(queryPartDistributionMap, "queryPartDistributionMap must be not null!");
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must be not null!");
		Preconditions.checkNotNull(cfg, "transCfg must be not null!");
		
		final List<QueryPart> localParts = Lists.newArrayList();
		final PeerID ownPeerID = P2PDictionaryService.get().getLocalPeerID();

		for(final QueryPart part : queryPartDistributionMap.keySet()) {
			
			final PeerID assignedPeerID = queryPartDistributionMap.get(part);
			if(assignedPeerID.equals(ownPeerID)) {
				
				localParts.add(part);
				LOG.debug("QueryPart {} locally stored", part);
				
			} else {
				
				SimplePlanPrinter<ILogicalOperator> printer = new SimplePlanPrinter<ILogicalOperator>();
				LOG.debug("Plan of the querypart to publish:\n{}", printer.createString(part.getLogicalPlan()));
				DistributionHelper.publish(part, assignedPeerID, sharedQueryID, cfg);
				
			}

		}

		return localParts;
		
	}
	
	/**
	 * Shares all {@link QueryPart} of an {@link ILogicalQuery}, which shall be published and transforms all locally {@link QueryPart}s 
	 * into an {@link ILogicalQuery}.
	 * @param queryParts All {@link QueryPart}s of an {@link ILogicalQuery}. <br />
	 * <code>queryParts</code> must not be null.
	 * @param queryPartDistributionMap The mapping of the {@link QueryPart}s and the {@link PeerID}s, where they shall be executed. <br />
	 * <code>queryPartDistributionMap</code> must not be null.
	 * @param cfg The transfer configuration. <br />
	 * <code>transCfg</code> must not be null.
	 * @param queryName The name of the {@link ILogicalQuery}.
	 * @param sourceToFragStrat A mapping of source name and fragmentation strategy for that source.
	 * @param degreeOfParallelism The degree of parallelism.
	 * @return The new {@link ILogicalQuery} created from all {@link QueryPart}s, which shall be executed locally.
	 */
	protected ILogicalQuery shareParts(List<QueryPart> queryParts, Map<QueryPart, PeerID> queryPartDistributionMap, QueryBuildConfiguration cfg, 
			String queryName, Map<String, IDataFragmentation> sourceToFragStrat, int degreeOfParallelism) {
		
		Preconditions.checkNotNull(queryPartDistributionMap, "queryPartDistributionMap must be not null!");
		Preconditions.checkNotNull(queryParts, "queryParts must be not null!");
		Preconditions.checkNotNull(cfg, "transCfg must be not null!");
		Preconditions.checkNotNull(sourceToFragStrat, "sourceToFragStrat must not be null!");
		Preconditions.checkArgument(sourceToFragStrat.size() > 0, "sourceToFragStrat must be not empty!");
		
		// Generate an ID for the shared query
		final ID sharedQueryID = DistributionHelper.generateSharedQueryID();
		
		// Get all queryParts of this query mapped with the executing peer
		final Map<QueryPart, PeerID> queryPartPeerMap = Maps.newHashMap();
		for(QueryPart part : queryParts) {
			
			QueryPart preparedPart = DistributionHelper.replaceStreamAOs(part);
			queryPartPeerMap.put(preparedPart, queryPartDistributionMap.get(part));
			
		}
		
		// publish the queryparts and transform the parts which shall be executed locally into a query
		ILogicalQuery localQuery = DistributionHelper.transformToQuery(this.shareParts(queryPartPeerMap, sharedQueryID, cfg), queryName);
		
		// Registers an sharedQueryID as a master to resolve removed query parts
		QueryPartController.getInstance().registerAsMaster(localQuery, sharedQueryID);
		
		return localQuery;
		
	}
	
	/**
	 * Sets the degree of parallelism for the {@link ILogicalQuery}, e.g. <code>1</code> for no parallelization of the {@link ILogicalQuery}.
	 * @param wantedDegree The wanted degree by the user. <br />
	 * <code>wantedDegree</code> must greater than zero.
	 * @param maxDegree The maximum possibleDegree. <br />
	 * <code>maxDegree</code> must greater than zero.
	 * @return The minimum of the two parameters.
	 */
	protected int getDegreeOfParallelismn(int wantedDegree, int maxDegree) {
		
		Preconditions.checkArgument(wantedDegree > 0, "wantedDegree must be greater than zero!");
		Preconditions.checkArgument(maxDegree > 0, "maxDegree must be greater than zero!");
		
		return Math.min(wantedDegree, maxDegree);
		
	}
	
	/**
	 * Reads out the fragmentation stratgey from the {@link QueryBuildConfiguration}.
	 * @param executor The {@link IServerExecutor}.
	 * @param parameters The transfer configuration. <br />
	 * <code>transCfg</code> must not be null.
	 * @param sourceNames A collection of all used source names.
	 * @return A mapping of source name and fragmentation strategy for that source.
	 */
	protected Map<String, IDataFragmentation> getFragmentationStrategies(IServerExecutor executor, QueryBuildConfiguration parameters, 
			Collection<String> sourceNames) {
		
		// Preconditions
		Preconditions.checkNotNull(executor, "executor must not be null!");
		Preconditions.checkNotNull(parameters, "parameters must not be null!");
		Preconditions.checkNotNull(sourceNames, "sourceNames must not be null!");
		Preconditions.checkArgument(!sourceNames.isEmpty(), "sourceNames must not be empty!");
		
		Map<String, IDataFragmentation> sourceToFragStrat = Maps.newHashMap();
		Class<? extends ILogicalOperator> classForDataJunction = null;
		
		if(!parameters.contains(ParameterFragmentationType.class)) {
			
			LOG.error("No fragmentation strategy defined. Using replication.");
			for(String sourceName : sourceNames)
				sourceToFragStrat.put(sourceName, new Replication());
			return sourceToFragStrat;
			
		}
		
		String[] strFragStrats = parameters.get(ParameterFragmentationType.class).getValue().split(ParameterFragmentationType.OUTER_SEP);
		
		// XXX At the moment there is only one strategy for one source allowed!
		if(strFragStrats.length > 1)
			throw new IllegalArgumentException("More than one fragmentation startegy or source specification is not allowed yet!");
		
		for(String strFragStrat : strFragStrats) {
			
			String[] strParameters = strFragStrat.split(ParameterFragmentationType.INNER_SEP);
			String strategyName = strParameters[0];
			IDataFragmentation fragStrat;
			
			if(ParameterFragmentationType.UNDEFINED.equals(strategyName)) {
				
				LOG.error("Fragmentation strategy '{}' was not found. Using replication.", strategyName);
				fragStrat = new Replication();
				
			} else {
				
				Optional<IDataFragmentation> optFragStrat = executor.getDataFragmentation(strategyName);
				if(!optFragStrat.isPresent()) {
					
					LOG.error("Fragmentation strategy '{}' was not found. Using replication.", strategyName);
					fragStrat = new Replication();
					
				} else fragStrat = optFragStrat.get();
				
			}
			
			// Check if different strategies (e.g. horizontal and vertical) are mixed
			if(classForDataJunction == null)
				classForDataJunction = fragStrat.getOperatorForJunctionClass();
			else if(classForDataJunction != fragStrat.getOperatorForJunctionClass())
				throw new IllegalArgumentException("Illegal mixture of fragmentation strategies");
			
			if(strParameters.length < 2) {
				
				// No source name given
				throw new IllegalArgumentException("No source specified for fragmentation strategy 'strategyName'.");
				
			} else {
							
				String sourceName = strParameters[1];
				
				if(!sourceNames.contains(sourceName))
					throw new IllegalArgumentException("Unknown source 'sourceName'.");
				else sourceToFragStrat.put(sourceName, fragStrat);
					
			}
			
		}
		
		for(String sourceName : sourceNames) {
			
			if(!sourceToFragStrat.containsKey(sourceName))
				sourceToFragStrat.put(sourceName, new Replication());
				
		}
		
		return sourceToFragStrat;
		
	}
	
	/**
	 * Inserts all {@link ILogicalOperator}s needed for a data fragmentation into a {@link QueryPart}.
	 * @param part The {@link QueryPart}.
	 * @param sourceToFragStrat A mapping of source name and fragmentation strategy for that source.
	 * @param fragOpToNumUsedOutputs A mapping of fragmentation operators and the number of used output ports.
	 * @param degreeOfParallelism The degree of parallelism.
	 * @param parameters The transfer configuration.
	 * @return <code>part</code> enhanced with an {@link ILogicalOperator} for data distribution, 
	 * if <code>part</code> is one belonging to the source manager; <br />
	 * <code>part</code>, else.
	 * @see IDataFragmentation#insertOperatorForDistribution(Collection, int, QueryBuildConfiguration)
	 * @see IDataFragmentation#insertOperatorForJunction(Collection, QueryBuildConfiguration)
	 */
	protected QueryPart insertOperatorsForFragmentation(QueryPart part, Map<String, IDataFragmentation> sourceToFragStrat, 
			Map<ILogicalOperator, Integer> fragOpToNumUsedOutputs, int degreeOfParallelism, QueryBuildConfiguration parameters) {
		
		// Preconditions
		Preconditions.checkNotNull(part, "part must not be null!");
		Preconditions.checkArgument(part.getDestinationName().isPresent() && 
				part.getDestinationName().get().equals(AbstractLoadBalancer.getSourceDestinationName()), "part must be a source part");
		Preconditions.checkNotNull(sourceToFragStrat, "sourceToFragStrat must not be null!");
		Preconditions.checkArgument(sourceToFragStrat.size() > 0, "sourceToFragStrat must be not empty!");
		Preconditions.checkArgument(degreeOfParallelism > 1, "degreeOfParallelism must be greater than one!");
		Preconditions.checkNotNull(parameters, "parameters must not be null!");
		
		if(!part.getDestinationName().isPresent() || !part.getDestinationName().get().equals(AbstractLoadBalancer.getSourceDestinationName()))
			return part;
		
		Collection<ILogicalOperator> operators = part.getOperators();
		Collection<ILogicalOperator> enhancedOperators = Lists.newArrayList(operators);
		Collection<String> processedSourceNames = Lists.newArrayList();
		
		for(ILogicalOperator operator : operators) {
			
			if(!(operator instanceof StreamAO) && !(operator instanceof WindowAO))
				continue;
			
			Optional<String> optSourceName = DistributionHelper.getSourceName(operator);
			
			if(!optSourceName.isPresent() ||processedSourceNames.contains(optSourceName.get()))
				continue;
			
			IDataFragmentation fragStrat = sourceToFragStrat.get(optSourceName.get());
			
			enhancedOperators = fragStrat.insertOperatorForDistribution(enhancedOperators, optSourceName.get(), degreeOfParallelism, parameters);
			
			// Search the new Operator
			if(fragStrat.getOperatorForDistributionClass() != null) {
				
				for(ILogicalOperator enhancedOperator : enhancedOperators) {
					
					if(enhancedOperator.getClass() == fragStrat.getOperatorForDistributionClass())
						fragOpToNumUsedOutputs.put(enhancedOperator, 1);
					
				}
				
			}
			
			processedSourceNames.add(optSourceName.get());
			
		}
		
		return new QueryPart(enhancedOperators, AbstractLoadBalancer.getSourceDestinationName());
		
	}

}