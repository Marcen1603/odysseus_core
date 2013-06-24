package de.uniol.inf.is.odysseus.p2p_new.lb;

import java.util.ArrayList;
import java.util.Collection;
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
import de.uniol.inf.is.odysseus.core.server.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDistributionType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.SimplePlanPrinter;
import de.uniol.inf.is.odysseus.p2p_new.distribute.DistributionHelper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPart;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPartController;
import de.uniol.inf.is.odysseus.p2p_new.distribute.logicaloperator.DistributionMergeAO;
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
	 * The {@link SimplePlanPrinter} instance for this class.
	 */
	protected SimplePlanPrinter<ILogicalOperator> printer = new SimplePlanPrinter<ILogicalOperator>();
	
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
	public static final String MIN_PARAM = "min";
	
	/**
	 * The minimum value of parallelism.
	 */
	public static final int MIN_VALUE = 1;
	
	/**
	 * The parameter to determine that the maximum degree of parallelism should be used.
	 */
	public static final String MAX_PARAM = "max";
	
	/**
	 * The maximum value of parallelism. <br />
	 * This leads to the usage of the number of available remote Peers as the degree of parallelism.
	 */
	public static final int MAX_VALUE = Integer.MAX_VALUE;
	
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
		
		// Get all available peers
		final Collection<PeerID> remotePeerIDs = P2PDictionaryService.get().getRemotePeerIDs();
		if(remotePeerIDs.isEmpty()) {
			
			LOG.debug("Could not find any remote peers to distribute logical query. Executing all locally.");
			return queries;
			
		} 		
		DistributionHelper.logPeerStatus(remotePeerIDs);
		
		// Read out the wanted degree of parallelism from the given distribution type parameter		
		int degreeOfParallelism = this.getDegreeOfParallelismn(this.determineWantedDegreeOfParallelism(cfg), remotePeerIDs.size());
		
		// All queryparts over all queries
		final Map<ILogicalQuery, List<QueryPart>> queryPartsMap = Maps.newHashMap();

		for(final ILogicalQuery originQuery : queries) {
			
			// Make a copy of the query to avoid changes of the originQuery
			ILogicalQuery query = DistributionHelper.copyLogicalQuery(originQuery);
			queryPartsMap.put(originQuery, new ArrayList<QueryPart>());
			
			// Get all logical operators of the query and remove the TopAOs
			List<ILogicalOperator> operators = Lists.newArrayList();
			RestructHelper.collectOperators(query.getLogicalPlan(), operators);
			RestructHelper.removeTopAOs(operators);
			
			// Create the local part
			QueryPart localPart = this.createLocalPart(operators, degreeOfParallelism);
			
			// Determine query parts
			List<QueryPart> parts = this.determineQueryParts(operators);
			LOG.debug("Got {} parts of logical query {}", parts.size(), originQuery);
			queryPartsMap.get(originQuery).addAll(parts);
			
			// Make copies of the query
			List<ILogicalQuery> queryCopies = Lists.newArrayList();	
			for(int copyNo = 0; copyNo < degreeOfParallelism - 1; copyNo++)
				queryCopies.add(DistributionHelper.copyLogicalQuery(originQuery));

			for(ILogicalQuery queryCopy: queryCopies) {
			
				// Get all logical operators of the query and remove the TopAOs
				operators.clear();;
				RestructHelper.collectOperators(queryCopy.getLogicalPlan(), operators);
				RestructHelper.removeTopAOs(operators);

				// Subscribe copy to local part
				this.subscribeCopyToLocalPart(operators, degreeOfParallelism, localPart);
				
				// Create the queryparts of this query
				parts = this.determineQueryParts(operators);
				queryPartsMap.get(originQuery).addAll(parts);
				
			}
			
			// Initialize all operators of the local part
			this.initLocalPart(localPart);
			queryPartsMap.get(originQuery).add(localPart);
			
		}
		
		// Assign query parts to peers
		final Map<QueryPart, PeerID> queryPartDistributionMap = 
				this.assignQueryParts(remotePeerIDs, P2PDictionaryService.get().getLocalPeerID(), queryPartsMap.values());
		int connectionNo =  connectionCounter++;
		DistributionHelper.generatePeerConnections(queryPartDistributionMap, getAccessName() + connectionNo + "_", 
				getSenderName() + connectionNo + "_");
		
		// The queries to be executed locally
		final List<ILogicalQuery> localQueries = Lists.newArrayList();
		
		// Publish all remote parts and get the local ones
		for(ILogicalQuery query : queryPartsMap.keySet())
			localQueries.add(this.shareParts(queryPartsMap.get(query), queryPartDistributionMap, cfg, query.toString()));
		
		return localQueries;
		
	}
	
	protected int determineWantedDegreeOfParallelism(QueryBuildConfiguration cfg) {
		
		// Preconditions
		Preconditions.checkNotNull(cfg, "cfg must be not null");
		Preconditions.checkNotNull(cfg.get(ParameterDistributionType.class).getValue(), "parameters for distributiontype must not be null");
		
		String[] strParameters = cfg.get(ParameterDistributionType.class).getValue().split(" ");	
		int wantedDegreeOfParallelism = MIN_VALUE;
		
		if(strParameters.length > 1) {
			
			if(strParameters[1].toLowerCase().equals(MIN_PARAM))
				wantedDegreeOfParallelism = MIN_VALUE;
			else if(strParameters[1].toLowerCase().equals(MAX_PARAM))
				wantedDegreeOfParallelism = MAX_VALUE;
			else try {
			
				wantedDegreeOfParallelism = Integer.parseInt(strParameters[1]);
				if(wantedDegreeOfParallelism < MIN_VALUE) {
					
					LOG.error("{} is an invalid degree of parallelism. Degree settet to {}", strParameters[1], MIN_VALUE);
					wantedDegreeOfParallelism = MIN_VALUE;
					
				}
				
			} catch(NumberFormatException e) {
				
				e.printStackTrace();
				LOG.error("Could not parse {} to an integer. Degree settet to {}", strParameters[1], MIN_VALUE);
				
			}
			
		}
		
		return wantedDegreeOfParallelism;
		
	}
	
	/**
	 * Splits an {@link ILogicalQuery} into a list of {@link QueryPart}s.
	 * @param operators The list of {@link ILogicalOperator}s representing the{@link ILogicalQuery}. <br />
	 * <code>operators</code> must not be null and not empty.
	 * @return The list of {@link QueryPart}s.
	 */
	protected abstract List<QueryPart> determineQueryParts(List<ILogicalOperator> operators);
	
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
	protected Map<QueryPart, PeerID> assignQueryParts(Collection<PeerID> remotePeerIDs, 
			PeerID localPeerID, Collection<List<QueryPart>> queryParts) {
		
		Preconditions.checkNotNull(remotePeerIDs, "remotePeerIDs must be not null!");
		Preconditions.checkArgument(remotePeerIDs.size() > 0, "remotePeerIDs must be not empty!");
		Preconditions.checkNotNull(localPeerID, "localPeerID must be not null!");
		Preconditions.checkNotNull(queryParts, "queryParts must be not null!");
		
		final Map<QueryPart, PeerID> distributed = Maps.newHashMap();
		final Iterator<List<QueryPart>> partsIter = queryParts.iterator();
		
		while(partsIter.hasNext()) {
		
			for(final QueryPart part : partsIter.next()) {
			
				PeerID peerID = ((List<PeerID>) remotePeerIDs).get(peerCounter);
				Optional<String> peerName = P2PDictionaryService.get().getRemotePeerName(peerID);
				
				if(part.getDestinationName().isPresent() && part.getDestinationName().get().equals(AbstractLoadBalancer.getLocalDestinationName())) {
					
					// Local part
					distributed.put(part, localPeerID);
					peerName = P2PDictionaryService.get().getRemotePeerName(localPeerID);
					
				} else {
					
					// Round-Robin
					distributed.put(part, peerID);
					peerCounter = (++peerCounter) % remotePeerIDs.size();					
					
				}			
				
				if(peerName.isPresent())
					LOG.debug("Assign query part {} to peer {}", part, peerName.get());
				else LOG.debug("Assign query part {} to peer {}", part, peerID);
				
			}
			
		}

		return distributed;
		
	}
	
	/**
	 * Creates a new {@link QueryPart} to be executed locally and makes the subscriptions for a single {@link ILogicalQuery}, 
	 * which {@link ILogicalOperator}s are <code>operators</code>.
	 * @param operators A collection of all {@link ILogicalOperator}s within an {@link ILogicalQuery} without any {@link TopAO}s.
	 * @param degreeOfParallelism The degree of parallelism. <br />
	 * <code>degreeOfParallelism</code> must greater than zero.
	 * @return The returned collection will contain for each sink: <br />
	 * the sink, if <code>degreeOfParallelism</code> is <code>1</code> and {@link ILogicalOperator#needsLocalResources()} is true for the sink. <br />
	 * a {@link RenameAO} with no operation subscribed to the sink, if <code>degreeOfParallelism</code> is <code>1</code> and 
	 * {@link ILogicalOperator#needsLocalResources()} is false for the sink. <br />
	 * a {@link DistributionMergeAO} subscribed to the sink, if <code>degreeOfParallelism</code> is greater than <code>1</code> and 
	 * {@link ILogicalOperator#needsLocalResources()} is false for the sink. <br />
	 * a {@link DistributionMergeAO} subscribed to all sources of the sink and subscribed as a source to the sink, 
	 * if <code>degreeOfParallelism</code> is greater than <code>1</code> and {@link ILogicalOperator#needsLocalResources()} is true for the sink.
	 */
	protected QueryPart createLocalPart(Collection<ILogicalOperator> operators, int degreeOfParallelism) {
		
		Preconditions.checkNotNull(operators, "operators must be not null!");
		Preconditions.checkArgument(degreeOfParallelism > 0, "degreeOfParallelism must be greater than zero!");
		
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
				final DistributionMergeAO mergeAO = new DistributionMergeAO();
				localOperators.add(mergeAO);
				mergeAO.subscribeToSource(operator, 0, 0, operator.getOutputSchema());
				LOG.debug("Subscribed {} to {}", operator, mergeAO);
				
			} else {	// operator.needsLocalResources() && degreeOfParallelism > 1
				
				// Map operator to local part; Merging needed before
				final DistributionMergeAO mergeAO = new DistributionMergeAO();
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
	 * Makes the subscriptions for a single {@link ILogicalQuery}, which {@link ILogicalOperator}s are <code>operators</code>, to an existing local part.
	 * @param operators A collection of all {@link ILogicalOperator}s within an {@link ILogicalQuery} without any {@link TopAO}s.
	 * @param degreeOfParallelism The degree of parallelism. <br />
	 * <code>degreeOfParallelism</code> must greater than zero.
	 * @param localPart The local part to which the {@link ILogicalQuery} shall be subscribed.
	 */
	protected void subscribeCopyToLocalPart(Collection<ILogicalOperator> operators, int degreeOfParallelism, QueryPart localPart) {
		
		Preconditions.checkNotNull(operators, "operators must be not null!");
		Preconditions.checkArgument(degreeOfParallelism > 0, "degreeOfParallelism must be greater than zero!");
		Preconditions.checkNotNull(localPart, "localPart must be not null!");
		
		final List<ILogicalOperator> localOperators = Lists.newArrayList();
		
		// Iterator for the operators of the local Part
		Iterator<ILogicalOperator> localIter = localPart.getOperators().iterator();
		
		for(ILogicalOperator operator : operators) {
			
			if(!operator.getSubscriptions().isEmpty()) {
				
				// Not a sink of the query
				continue;
				
			} else if(!operator.needsLocalResources() && degreeOfParallelism > 1) {
				
				// Subscribe operator to existing merger
				ILogicalOperator mergeAO = localIter.next();
				mergeAO.subscribeToSource(operator, mergeAO.getNumberOfInputs(), 0, operator.getOutputSchema());
				LOG.debug("Subscribed {} to {}", operator, mergeAO);
				
			} else {	// operator.needsLocalResources() && degreeOfParallelism > 1
				
				// Subscribe sources of operator to existing merger
				localOperators.add(operator);
				ILogicalOperator mergeAO = localIter.next();
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
	 * Calls {@link ILogicalOperator#initialize()} for all {@link ILogicalOperator}s within the local part.
	 * @param localPart The local Part.
	 */
	protected void initLocalPart(QueryPart localPart) {
		
		Preconditions.checkNotNull(localPart, "localPart must be not null!");
		
		for(ILogicalOperator operator : localPart.getOperators())
			operator.initialize();
		
	}
	
	/**
	 * Determines which {@link QueryPart}s shall be executed locally and publishes the rest on the mapped peers.
	 * @param queryPartDistributionMap The mapping of the {@link QueryPart}s and the IDs of the the peers, where each {@link QueryPart} 
	 * shall be stored. <br />
	 * <code>queryPartDistributionMap</code> must not be null.
	 * @param sharedQueryID The {@link ID} for query sharing. <br />
	 * <code>sharedQueryID</code> must not be null.
	 * @param transCfg The name of the transport configuration. <br />
	 * <code>transCfg</code> must not be null.
	 */
	protected List<QueryPart> shareParts(Map<QueryPart, PeerID> queryPartDistributionMap, ID sharedQueryID, QueryBuildConfiguration transCfg) {
		
		Preconditions.checkNotNull(queryPartDistributionMap, "queryPartDistributionMap must be not null!");
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must be not null!");
		Preconditions.checkNotNull(transCfg, "transCfg must be not null!");
		
		final List<QueryPart> localParts = Lists.newArrayList();
		final PeerID ownPeerID = P2PDictionaryService.get().getLocalPeerID();

		for(final QueryPart part : queryPartDistributionMap.keySet()) {
			
			final PeerID assignedPeerID = queryPartDistributionMap.get(part);
			if(assignedPeerID.equals(ownPeerID)) {
				
				localParts.add(part);
				LOG.debug("QueryPart {} locally stored", part);
				
			} else {
				
				LOG.debug("Plan of the querypart to publish:\n{}", this.printer.createString(part.getLogicalPlan()));
				DistributionHelper.publish(part, assignedPeerID, sharedQueryID, transCfg);
				
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
	 * @param transCfg The transfer configuration. <br />
	 * <code>transCfg</code> must not be null.
	 * @param queryName The name of the {@link ILogicalQuery}.
	 * @return The new {@link ILogicalQuery} created from all {@link QueryPart}s, which shall be executed locally.
	 */
	protected ILogicalQuery shareParts(List<QueryPart> queryParts, Map<QueryPart, PeerID> queryPartDistributionMap, QueryBuildConfiguration transCfg, 
			String queryName) {
		
		Preconditions.checkNotNull(queryPartDistributionMap, "queryPartDistributionMap must be not null!");
		Preconditions.checkNotNull(queryParts, "queryParts must be not null!");
		Preconditions.checkNotNull(transCfg, "transCfg must be not null!");
		
		// Generate an ID for the shared query
		final ID sharedQueryID = DistributionHelper.generateSharedQueryID();
		
		// Get all queryParts of this query mapped with the executing peer
		final Map<QueryPart, PeerID> queryPartPeerMap = Maps.newHashMap();
		for(QueryPart part : queryParts)
			queryPartPeerMap.put(part, queryPartDistributionMap.get(part));
		
		// publish the queryparts and transform the parts which shall be executed locally into a query
		ILogicalQuery localQuery = DistributionHelper.transformToQuery(this.shareParts(queryPartPeerMap, sharedQueryID, transCfg), queryName);
		
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

}