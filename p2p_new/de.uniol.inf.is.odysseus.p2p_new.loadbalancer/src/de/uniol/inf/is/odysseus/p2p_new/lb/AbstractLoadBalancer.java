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
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
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
		
		int degreeOfParallelism = this.getDegreeOfParallelismn(wantedDegreeOfParallelism, remotePeerIDs.size());
		
		// All queryparts over all queries
		final Map<ILogicalQuery, List<QueryPart>> queryPartsMap = Maps.newHashMap();

		for(final ILogicalQuery query : queries) {
			
			// All copies of the query plus the original query
			List<ILogicalQuery> queriesToDistribute = Lists.newArrayList(query);
			queryPartsMap.put(query, new ArrayList<QueryPart>());
			
			// Make copies of the query
			for(int copyNo = 0; copyNo < degreeOfParallelism - 1; copyNo++)
				queriesToDistribute.add(DistributionHelper.copyLogicalQuery(query));
			
			// The local part of this query plus all of it copies
			QueryPart localPart = null;
			
			for(ILogicalQuery queryToDistribute : queriesToDistribute) {
			
				// Get all logical operators of the query and remove the TopAOs
				final List<ILogicalOperator> operators = Lists.newArrayList();
				RestructHelper.collectOperators(queryToDistribute.getLogicalPlan(), operators);
				RestructHelper.removeTopAOs(operators);
				
				// Create the queryparts of this query
				List<QueryPart> parts = this.determineQueryParts(operators);
				LOG.debug("Got {} parts of logical query {}", parts.size(), query);
				queryPartsMap.get(query).addAll(parts);
				
				// Create the local part if it hasn't been done before
				if(localPart == null) {
					
					// Generate a new logical operator which marks that the query result shall return to this instance
					localPart = this.createLocalPart(queryPartsMap.get(query), degreeOfParallelism);
					
				}
				
			}
			
			// Subscribe all copies plus the origin query to the local part
			this.subscribeToLocalPart(queryPartsMap.get(query), localPart);
			queryPartsMap.get(query).add(localPart);
			
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
	 * Creates a new {@link QueryPart} to be executed locally.
	 * @param parts A list of semantically equal {@link QueryPart}s, 
	 * which shall be merged by the local {@link QueryPart}. <br />
	 * <code>parts</code> must not be null and not empty.
	 * @param degreeOfParallelism The degree of parallelism. <br />
	 * <code>degreeOfParallelism</code> must greater than zero.
	 * @return For each sink: <br />
	 * If <code>degreeOfParallelism</code> is <code>1</code>: A {@link RenameAO} with no operation. <br />
	 * If <code>degreeOfParallelism</code> is greater than<code>1</code>: A {@link DistributionMergeAO}.
	 */
	protected QueryPart createLocalPart(List<QueryPart> parts, int degreeOfParallelism) {
		
		Preconditions.checkNotNull(parts, "parts must be not null!");
		Preconditions.checkArgument(parts.size() > 0, "parts must be not empty!");
		Preconditions.checkArgument(degreeOfParallelism > 0, "degreeOfParallelism must be greater than zero!");
		
		final List<ILogicalOperator> operators = Lists.newArrayList();
		
		for(QueryPart part : parts) {
			
			for(ILogicalOperator sink : part.getRealSinks()) {
				
				// Get all sources of any other query part subscribed to the sink
				Map<QueryPart, ILogicalOperator> sourcesMap = DistributionHelper.determineNextQueryParts(
						sink, part, Sets.newHashSet(parts));
				
				if(!sourcesMap.isEmpty()) {
					
					// If there are any sources in other queryParts, there will be no local part created for 
					// the sink
					continue;
					
				} else if(degreeOfParallelism == 1) {
					
					// No merging needed
					final RenameAO renameAO = new RenameAO();
					renameAO.setNoOp(true);
					renameAO.addParameterInfo("isNoOp", "'true'");
					operators.add(renameAO);
					
				} else operators.add(new DistributionMergeAO());
				
			}
			
		}
		
		return new QueryPart(operators, AbstractLoadBalancer.getLocalDestinationName());
		
	}
	
	/**
	 * Subscribes a local {@link QueryPart} to a list of {@link QueryPart}s. <br />
	 * Every sink of each {@link QueryPart} will be subscribed to a different Port of the source of the 
	 * local {@link QueryPart}.
	 * @param queryParts The list of {@link QueryPart}s. <br />
	 * <code>queryParts</code> must not be null.
	 * @param localPart The local {@link QueryPart} to be subscribed. <br />
	 * <code>localPart</code> must not be null.
	 */
	protected void subscribeToLocalPart(List<QueryPart> queryParts, QueryPart localPart) {
		
		Preconditions.checkNotNull(queryParts, "queryParts must be not null!");
		Preconditions.checkNotNull(localPart, "localPart must be not null!");
		
		for(QueryPart part : queryParts) {
			
			Iterator<ILogicalOperator> localSourceIter = localPart.getRelativeSources().iterator();
			Collection<ILogicalOperator> sinks = part.getRealSinks();
			
			for(ILogicalOperator sink : sinks) {
				
				// Get all sources of any other query part subscribed to the sink
				Map<QueryPart, ILogicalOperator> sourcesMap = DistributionHelper.determineNextQueryParts(
						sink, part, Sets.newHashSet(queryParts));
				
				if(sourcesMap.isEmpty()) {
				
					ILogicalOperator localSource = localSourceIter.next();
					localSource.subscribeToSource(sink, localSource.getNumberOfInputs(), 0, sink.getOutputSchema());
					
				}
				
			}
			
		}
		
		// Initialize the operators of the local part
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