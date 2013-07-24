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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDistributionType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterFragmentationType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.SimplePlanPrinter;
import de.uniol.inf.is.odysseus.p2p_new.distribute.DistributionHelper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPart;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPartController;
import de.uniol.inf.is.odysseus.p2p_new.distribute.logicaloperator.ReplicationMergeAO;
import de.uniol.inf.is.odysseus.p2p_new.lb.fragmentation.Replication;
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
	@Override
	public List<ILogicalQuery> distributeLogicalQueries(IExecutor sender,
			List<ILogicalQuery> queries, QueryBuildConfiguration cfg) {
		
		Preconditions.checkNotNull(cfg, "cfg must be not null!");

		if(queries == null || queries.isEmpty()) {
			
			// Nothing to distribute
			return queries;
			
		}
		
		// Check, if a fragmentation strategy is defined
		IDataFragmentation fragmentationStrategy = null;
		if(cfg.get(ParameterDoDataFragmentation.class).getValue()) {
			
			fragmentationStrategy = this.getFragmentationStrategy((IServerExecutor) sender, cfg);
			if(fragmentationStrategy != null)
				LOG.debug("Data fragmentation strategy {} used", fragmentationStrategy.getName());
			
		} else fragmentationStrategy = new Replication();
		
		// Get all available peers
		final Collection<PeerID> remotePeerIDs = P2PDictionaryService.get().getRemotePeerIDs();
		if(remotePeerIDs.isEmpty()) {
			
			LOG.debug("Could not find any remote peers to distribute logical query. Executing all locally.");
			return queries;
			
		} 		
		DistributionHelper.logPeerStatus(remotePeerIDs);
		
		// Read out the wanted degree of parallelism from the given distribution type parameter
		int wantedDegreeOfParallelism = this.determineWantedDegreeOfParallelism(cfg);
		int maxDegree = remotePeerIDs.size();
		int degreeOfParallelism = this.getDegreeOfParallelismn(wantedDegreeOfParallelism, maxDegree);;
		if(!fragmentationStrategy.getName().equals(Replication.NAME) && degreeOfParallelism < 2) {
			
			LOG.error("Degree of parallelism must be at least 2 to use data fragmentation. Turned off data fragmentation.");
			fragmentationStrategy = new Replication();
			
		}
		
		// All queryparts over all queries
		// Outer map keys: the origin queries
		// Inner map keys: the copies of one origin query
		// Inner map values: a list of queryparts belonging to the copy
		final Map<ILogicalQuery, Map<ILogicalQuery, List<QueryPart>>> queryPartsMap = Maps.newHashMap();

		for(final ILogicalQuery originQuery : queries) {
			
			queryPartsMap.put(originQuery, new HashMap<ILogicalQuery, List<QueryPart>>());
			QueryPart localPart = null;
			Collection<QueryPart> sourceParts = Lists.newArrayList();
			
			// Make copies of the query
			List<ILogicalQuery> queryCopies = Lists.newArrayList();	
			for(int copyNo = 0; copyNo < degreeOfParallelism; copyNo++)
				queryCopies.add(DistributionHelper.copyLogicalQuery(originQuery));

			for(ILogicalQuery queryCopy: queryCopies) {
			
				// Get all logical operators of the query and remove the TopAOs
				List<ILogicalOperator> operators = Lists.newArrayList();
				RestructHelper.collectOperators(queryCopy.getLogicalPlan(), operators);
				RestructHelper.removeTopAOs(operators);
				
				if(!fragmentationStrategy.getName().equals(Replication.NAME)) {
					
					if(sourceParts.isEmpty()) {
						
						// Create the source parts
						sourceParts = this.createSourceParts(operators);
						
					} else {
					
						// Subscribe copy to source parts
						this.subscribeCopyToSourceParts(operators, sourceParts);
						
					}
				
				}
				
				// Create the local part
				if(localPart == null)
					localPart = this.createLocalPart(operators, degreeOfParallelism, fragmentationStrategy);

				// Subscribe copy to local part
				this.subscribeCopyToLocalPart(operators, degreeOfParallelism, localPart, fragmentationStrategy);
				
				// Create the queryparts of this query
				List<QueryPart> parts = this.determineQueryParts(operators, localPart);
				LOG.debug("Got {} parts of logical query {}", parts.size(), originQuery);
				queryPartsMap.get(originQuery).put(queryCopy, parts);
				
			}
			
			// Initialize all operators of the source and the local part and add it ONCE
			if(!fragmentationStrategy.getName().equals(Replication.NAME) && !sourceParts.isEmpty()) {
			
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
					fragmentationStrategy, degreeOfParallelism));
			
		}
		
		return localQueries;
		
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
	 * @param fragmentationStrategy  The {@link IDataFragmentation} to determine the needed {@link ILogicalOperator}s.
	 * @return The returned collection will contain for each sink: <br />
	 * the sink, if <code>degreeOfParallelism</code> is <code>1</code> and {@link ILogicalOperator#needsLocalResources()} is true for the sink. <br />
	 * a {@link RenameAO} with no operation subscribed to the sink, if <code>degreeOfParallelism</code> is <code>1</code> and 
	 * {@link ILogicalOperator#needsLocalResources()} is false for the sink. <br />
	 * a {@link ReplicationMergeAO} subscribed to the sink, if <code>degreeOfParallelism</code> is greater than <code>1</code> and 
	 * {@link ILogicalOperator#needsLocalResources()} is false for the sink. <br />
	 * a {@link ReplicationMergeAO} subscribed to all sources of the sink and subscribed as a source to the sink, 
	 * if <code>degreeOfParallelism</code> is greater than <code>1</code> and {@link ILogicalOperator#needsLocalResources()} is true for the sink.
	 */
	protected QueryPart createLocalPart(Collection<ILogicalOperator> operators, int degreeOfParallelism, 
			IDataFragmentation fragmentationStrategy) {
		
		Preconditions.checkNotNull(operators, "operators must be not null!");
		Preconditions.checkArgument(degreeOfParallelism > 0, "degreeOfParallelism must be greater than zero!");
		Preconditions.checkNotNull(fragmentationStrategy, "fragmentationStrategy must be not null!");
		
		final List<ILogicalOperator> localOperators = Lists.newArrayList();
		
		for(ILogicalOperator operator : operators) {
			
			if(!operator.getSubscriptions().isEmpty()) {
				
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
				final ILogicalOperator mergeAO = fragmentationStrategy.createOperatorForJunction();
				localOperators.add(mergeAO);
				mergeAO.subscribeToSource(operator, 0, 0, operator.getOutputSchema());
				LOG.debug("Subscribed {} to {}", operator, mergeAO);
				
			} else {	// operator.needsLocalResources() && degreeOfParallelism > 1
				
				// Map operator to local part; Merging needed before
				final ILogicalOperator mergeAO = fragmentationStrategy.createOperatorForJunction();
				localOperators.add(mergeAO);
				localOperators.add(operator);
				
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
	 */
	protected void subscribeCopyToSourceParts(Collection<ILogicalOperator> operators, Collection<QueryPart> sourceParts) {
		
		Preconditions.checkNotNull(operators, "operators must be not null!");
		Preconditions.checkNotNull(sourceParts, "sourceParts must be not null!");
		Preconditions.checkArgument(!sourceParts.isEmpty(), "sourceParts must be not empty!");
		
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
						for(LogicalSubscription sub : operator.getSubscribedToSource()) {
							
							if(!(sub.getTarget() instanceof StreamAO) && !(sub.getTarget() instanceof WindowAO)) {
								
								onlySubscribedToSourceAccesses = false;
								break;
								
							}
							
						}
						if(!onlySubscribedToSourceAccesses)
							continue;
						
					}
					
					// StreamAO or WindowAO with subscriptions to StreamAOs
					while(!sourceIter.hasNext()) {
						
						sourcePart = sourcePartsIter.next();
						sourceIter = sourcePart.getOperators().iterator();
						
					}					
					ILogicalOperator source = sourceIter.next();
					
					subsToRemove.add(subToSource);
					subsToAdd.add(new LogicalSubscription(source, subToSource.getSinkInPort(), subToSource.getSourceOutPort(), subToSource.getSchema()));
					
				}
				
				for(LogicalSubscription sub : subsToRemove)
					operator.unsubscribeFromSource(sub);
				for(LogicalSubscription sub : subsToAdd)
					operator.subscribeToSource(sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
				
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
	 * @param fragmentationStrategy The {@link IDataFragmentation} to determine the needed {@link ILogicalOperator}s.
	 */
	protected void subscribeCopyToLocalPart(Collection<ILogicalOperator> operators, int degreeOfParallelism, QueryPart localPart, 
			IDataFragmentation fragmentationStrategy) {
		
		Preconditions.checkNotNull(operators, "operators must be not null!");
		Preconditions.checkArgument(degreeOfParallelism > 0, "degreeOfParallelism must be greater than zero!");
		Preconditions.checkNotNull(localPart, "localPart must be not null!");
		Preconditions.checkNotNull(fragmentationStrategy, "fragmentationStrategy must be not null!");
		
		final List<ILogicalOperator> localOperators = Lists.newArrayList();
		
		// Iterator for the operators of the local Part
		Iterator<ILogicalOperator> localIter = localPart.getOperators().iterator();
		
		for(ILogicalOperator operator : operators) {
			
			if(!operator.getSubscriptions().isEmpty()) {
				
				// Not a sink of the query
				continue;
				
			} else if(!operator.needsLocalResources() && degreeOfParallelism > 1) {
				
				// Subscribe operator to existing merger
				ILogicalOperator mergeAO = null;
				do mergeAO = localIter.next();
				while(mergeAO.getClass() != fragmentationStrategy.getOperatorForJunctionClass() && localIter.hasNext());
				
				mergeAO.subscribeToSource(operator, mergeAO.getNumberOfInputs(), 0, operator.getOutputSchema());
				LOG.debug("Subscribed {} to {}", operator, mergeAO);
				
			} else {	// operator.needsLocalResources() && degreeOfParallelism > 1
				
				// Subscribe sources of operator to existing merger
				localOperators.add(operator);
				ILogicalOperator mergeAO = null;
				do mergeAO = localIter.next();
				while(mergeAO.getClass() != fragmentationStrategy.getOperatorForJunctionClass() && localIter.hasNext());
				
				for(LogicalSubscription subToSource : operator.getSubscribedToSource()) {
					
					operator.unsubscribeFromSource(subToSource);
					LOG.debug("Unsubscribed {} from {}", subToSource.getTarget(), operator);
					mergeAO.subscribeToSource(subToSource.getTarget(), mergeAO.getNumberOfInputs(), subToSource.getSourceOutPort(), 
							subToSource.getTarget().getOutputSchema());
					LOG.debug("Subscribed {} to {}", subToSource.getTarget(), mergeAO);
					
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
	 * @param fragmentationStrategy The {@link IDataFragmentation} to determine the needed {@link ILogicalOperator}s.
	 * @param degreeOfParallelism The degree of parallelism.
	 * @return The new {@link ILogicalQuery} created from all {@link QueryPart}s, which shall be executed locally.
	 */
	protected ILogicalQuery shareParts(List<QueryPart> queryParts, Map<QueryPart, PeerID> queryPartDistributionMap, QueryBuildConfiguration cfg, 
			String queryName, IDataFragmentation fragmentationStrategy, int degreeOfParallelism) {
		
		Preconditions.checkNotNull(queryPartDistributionMap, "queryPartDistributionMap must be not null!");
		Preconditions.checkNotNull(queryParts, "queryParts must be not null!");
		Preconditions.checkNotNull(cfg, "transCfg must be not null!");
		Preconditions.checkNotNull(fragmentationStrategy, "fragmentationStrategy must be not null!");
		
		// Generate an ID for the shared query
		final ID sharedQueryID = DistributionHelper.generateSharedQueryID();
		
		// Get all queryParts of this query mapped with the executing peer
		final Map<QueryPart, PeerID> queryPartPeerMap = Maps.newHashMap();
		for(QueryPart part : queryParts) {
			
			QueryPart preparedPart = DistributionHelper.replaceStreamAOs(this.insertOperatorsForFragmentation(
						part, fragmentationStrategy, degreeOfParallelism, cfg));			
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
	 * @return The wanted fragmentation strategy.
	 */
	protected IDataFragmentation getFragmentationStrategy(IServerExecutor executor, QueryBuildConfiguration parameters) {
		
		// Preconditions
		Preconditions.checkNotNull(executor, "executor must not be null!");
		Preconditions.checkNotNull(parameters, "parameters must not be null!");
		
		if(!parameters.contains(ParameterFragmentationType.class))
			throw new QueryParseException("Could not fragment sources. No fragmentation strategy specified.");
		
		String[] strParameters = parameters.get(ParameterFragmentationType.class).getValue().split(" ");
		String strategyName = strParameters[0];
		
		if(ParameterFragmentationType.UNDEFINED.equals(strategyName))
			throw new QueryParseException("Could not fragment sources. No fragmentation strategy specified.");
			
		Optional<IDataFragmentation> optStrategy = executor.getDataFragmentation(strategyName);
		if(!optStrategy.isPresent())
			throw new QueryParseException("Could not fragment sources. Fragmentation strategy '" + strategyName + "' was not found.");
		else return optStrategy.get();
		
	}
	
	/**
	 * Inserts all {@link ILogicalOperator}s needed for a data fragmentation into a {@link QueryPart}.
	 * @param part The {@link QueryPart}.
	 * @param fragmentationStrategy The {@link IDataFragmentation} to determine the needed {@link ILogicalOperator}s.
	 * @param degreeOfParallelism The degree of parallelism.
	 * @param parameters The transfer configuration.
	 * @return <code>part</code> enhanced with an {@link ILogicalOperator} for data junction, 
	 * if <code>part</code> is the local one; <br />
	 * <code>part</code> enhanced with an {@link ILogicalOperator} for data distribution, 
	 * if <code>part</code> is the one with the sources manager one; <br />
	 * <code>part</code>, else.
	 * @see IDataFragmentation#insertOperatorForDistribution(Collection, int, QueryBuildConfiguration)
	 * @see IDataFragmentation#insertOperatorForJunction(Collection, QueryBuildConfiguration)
	 */
	protected QueryPart insertOperatorsForFragmentation(QueryPart part, IDataFragmentation fragmentationStrategy, int degreeOfParallelism, 
			QueryBuildConfiguration parameters) {
		
		// Preconditions
		Preconditions.checkNotNull(part, "part must not be null!");
		Preconditions.checkNotNull(fragmentationStrategy, "fragmentationStrategy must not be null!");
		Preconditions.checkArgument(degreeOfParallelism > 1, "degreeOfParallelism must be greater than one!");
		Preconditions.checkNotNull(parameters, "parameters must not be null!");
		
		if(part.getDestinationName().isPresent() && part.getDestinationName().get().equals(AbstractLoadBalancer.getSourceDestinationName()))
			return new QueryPart(fragmentationStrategy.insertOperatorForDistribution(part.getOperators(), degreeOfParallelism, parameters), 
					part.getDestinationName().get());
		else return part;
		
	}

}