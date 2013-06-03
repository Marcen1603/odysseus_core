package de.uniol.inf.is.odysseus.p2p_new.lb;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.SimplePlanPrinter;
import de.uniol.inf.is.odysseus.p2p_new.distribute.DistributionHelper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPart;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPartController;
import de.uniol.inf.is.odysseus.p2p_new.lb.service.P2PDictionaryService;

// TODO Preconditions in allen Klassen
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
	
	// TODO neu
	private static int CONNECTION_COUNTER = 0;
	private static int getNextConnectionNo() {
		
		return CONNECTION_COUNTER++;
		
	}
	
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
	
	@Override
	public List<ILogicalQuery> distributeLogicalQueries(IExecutor sender,
			List<ILogicalQuery> queries, String cfgName) {

		if(queries == null || queries.isEmpty()) {
			
			// Nothing to distribute
			return queries;
			
		}
		
		// Get all available peers
		final Collection<PeerID> remotePeerIDs = P2PDictionaryService.get().getRemotePeerIDs();
		if(remotePeerIDs.isEmpty()) {
			
			LOG.debug("Could not find any remote peers to distribute logical query. Executing all locally.");
			return queries;
			
		} else DistributionHelper.logPeerStatus(remotePeerIDs);
		
		// All queries including copies
		final List<ILogicalQuery> distributedQueries = Lists.newArrayList();
		
		// All queryparts over all queries
		final Map<ILogicalQuery, List<QueryPart>> queryPartsMap = Maps.newHashMap();

		for(final ILogicalQuery query : queries) {
			
			// Generate a new logical operator which marks that the query result shall return to this instance
			QueryPart localPart = this.createLocalPart();
			
			this.determineQueryParts(query, queryPartsMap, localPart);
			distributedQueries.add(query);
			
			// TODO wantedDegree als Parameter
			final List<ILogicalQuery> copies = this.copyLogicalQuery(query, this.getDegreeOfParallelismn(0, remotePeerIDs.size()) - 1);
			for(ILogicalQuery copy : copies)
				this.determineQueryParts(copy, queryPartsMap, localPart);
			distributedQueries.addAll(copies);
			
			// Initialize the operators of the local part
			for(ILogicalOperator operator : localPart.getOperators())
				operator.initialize();
			
		}
		
		// Assign query parts to peers
		final Map<QueryPart, PeerID> queryPartDistributionMap = 
				this.assignQueryParts(remotePeerIDs, P2PDictionaryService.get().getLocalPeerID(), queryPartsMap.values());
		int connectionNo =  + getNextConnectionNo();
		DistributionHelper.generatePeerConnections(queryPartDistributionMap, getAccessName() + connectionNo + "_", 
				getSenderName() + connectionNo + "_");
		
		// The queries to be executed locally
		final List<ILogicalQuery> localQueries = Lists.newArrayList();
		
		for(ILogicalQuery query : queryPartsMap.keySet())
			localQueries.add(this.shareParts(queryPartsMap.get(query), queryPartDistributionMap, cfgName, query.toString()));
		
		return localQueries;
		
	}
	
	/**
	 * Splits an {@link ILogicalQuery} into a list of {@link QueryPart}s.
	 * @param operators The list of {@link ILogicalOperator}s representing the{@link ILogicalQuery}.
	 * @return The list of {@link QueryPart}s.
	 */
	protected abstract List<QueryPart> determineQueryParts(List<ILogicalOperator> operators);
	
	/**
	 * Maps each {@link QueryPart} to a peer except the local peer via round robin.
	 * @param remotePeerIDs The collection of all peer IDs.
	 * @param localPeerID The ID of the local peer.
	 * @param queryParts The list of all {@link QueryPart}s.
	 * @return The mapping of the {@link QueryPart}s and the IDs of the the peers, where each {@link QueryPart} shall be stored.
	 */
	protected Map<QueryPart, PeerID> assignQueryParts(Collection<PeerID> remotePeerIDs, 
			PeerID localPeerID, Collection<List<QueryPart>> queryParts) {
		
		final Map<QueryPart, PeerID> distributed = Maps.newHashMap();
		int peerCounter = 0;
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
					
					// Skip local peer for distributed part
					while(peerID == localPeerID) {
						
						// Round-Robin
						peerCounter = (peerCounter++) % remotePeerIDs.size();
						
					}
					
					distributed.put(part, peerID);
					
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
	 */
	protected abstract QueryPart createLocalPart();
	
	/**
	 * Determines the {@link QueryPart}s of an {@link ILogicalQuery} and stores it in a map.
	 * @param query The {@link ILogicalQuery}.
	 * @param queryPartsMap The mapping of {@link ILogicalQuery}s and their {@link QueryPart}s.
	 * @param localPart The {@link QueryPart} executed locally putting the distributed {@link QueryPart}s together.
	 */
	protected void determineQueryParts(ILogicalQuery query, Map<ILogicalQuery, List<QueryPart>> queryPartsMap, QueryPart localPart) {
		
		// Get all logical operators of the query and remove the TopAOs
		final List<ILogicalOperator> operators = Lists.newArrayList();
		RestructHelper.collectOperators(query.getLogicalPlan(), operators);
		RestructHelper.removeTopAOs(operators);
		
		// Split the query into parts
		final List<QueryPart> queryParts = this.determineQueryParts(operators);
		LOG.debug("Got {} parts of logical query {}", queryParts.size(), query);
		
		// Subscribe the local part
		ILogicalOperator localSource = localPart.getRelativeSources().iterator().next();
		Collection<ILogicalOperator> sinks = queryParts.get(queryParts.size() - 1).getRealSinks();
		for(ILogicalOperator sink : sinks)
			localSource.subscribeToSource(sink, localSource.getNumberOfInputs(), 0, sink.getOutputSchema());
			
		queryParts.add(localPart);
		queryPartsMap.put(query, queryParts);
		
	}
	
	/**
	 * Determines which <code>QueryPart</code>s shall be executed locally and publishes the rest on the mapped peers.
	 * @see InterqueryLoadBalancer#publish(QueryPart, String, ID, String)
	 * @param queryPartDistributionMap The mapping of the {@link QueryPart}s and the IDs of the the peers, where each {@link QueryPart} 
	 * shall be stored.
	 * @param sharedQueryID The {@link ID} for query sharing.
	 * @param transCfgName The name of the transport configuration.
	 */
	protected List<QueryPart> shareParts(Map<QueryPart, PeerID> queryPartDistributionMap, ID sharedQueryID, String transCfgName) {
		
		final List<QueryPart> localParts = Lists.newArrayList();
		final PeerID ownPeerID = P2PDictionaryService.get().getLocalPeerID();

		for(final QueryPart part : queryPartDistributionMap.keySet()) {
			
			final PeerID assignedPeerID = queryPartDistributionMap.get(part);
			if(assignedPeerID.equals(ownPeerID)) {
				
				localParts.add(part);
				LOG.debug("QueryPart {} locally stored", part);
				
			} else {
				
				QueryPart partToPublish = DistributionHelper.replaceStreamAOs(part);
				LOG.debug("Plan of the querypart to publish: {}", this.printer.createString(partToPublish.getOperators().iterator().next()));
				DistributionHelper.publish(partToPublish, assignedPeerID, sharedQueryID, transCfgName);
				
			}

		}

		return localParts;
		
	}
	
	/**
	 * Shares all {@link QueryPart} of an {@link ILogicalQuery}, which shall be published and transforms all locally {@link QueryPart}s 
	 * into an {@link ILogicalQuery}.
	 * @param queryParts All {@link QueryPart}s of an {@link ILogicalQuery}.
	 * @param queryPartDistributionMap The mapping of the {@link QueryPart}s and the {@link PeerID}s, where they shall be executed.
	 * @param transCfgName The name of the transfer configuration.
	 * @param queryName The name of the {@link ILogicalQuery}.
	 * @return The new {@link ILogicalQuery} created from all {@link QueryPart}s, which shall be executed locally.
	 */
	protected ILogicalQuery shareParts(List<QueryPart> queryParts, Map<QueryPart, PeerID> queryPartDistributionMap, String transCfgName, 
			String queryName) {
		
		// Generate an ID for the shared query
		final ID sharedQueryID = DistributionHelper.generateSharedQueryID();
		
		// Get all queryParts of this query mapped with the executing peer
		final Map<QueryPart, PeerID> queryPartPeerMap = Maps.newHashMap();
		for(QueryPart part : queryParts)
			queryPartPeerMap.put(part, queryPartDistributionMap.get(part));
		
		// publish the queryparts and transform the parts which shall be executed locally into a query
		ILogicalQuery localQuery = DistributionHelper.transformToQuery(this.shareParts(queryPartPeerMap, sharedQueryID, transCfgName), queryName);
		
		// Registers an sharedQueryID as a master to resolve removed query parts
		QueryPartController.getInstance().registerAsMaster(localQuery, sharedQueryID);
		
		return localQuery;
		
	}
	
	/**
	 * Creates copies of an {@link ILogicalQuery}.
	 * @param query The {@link ILogicalQuery} to be copied.
	 * @param numCopies The number of copies to be made.
	 * @return The list of copies excluding the original {@link ILogicalQuery}.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List<ILogicalQuery> copyLogicalQuery(ILogicalQuery query, int numCopies) {
		
		final List<ILogicalQuery> copies = Lists.newArrayList();
		
		for(int copyNo = 0; copyNo < numCopies; copyNo++) {
			
			CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(null);
			GenericGraphWalker walker = new GenericGraphWalker();
			walker.prefixWalk(query.getLogicalPlan(), copyVisitor);
			System.err.println(copyVisitor.getResult());
			ILogicalQuery copy = new LogicalQuery(DistributionHelper.getPQLParserID(), copyVisitor.getResult(), query.getPriority());
			copy.setName(query.getName() + "_Copy" + copyNo);
			copy.setUser(query.getUser());
			copies.add(copy);
			
		}
		
		return copies;
		
	}
	
	/**
	 * Sets the degree of parallelism for the {@link ILogicalQuery}, e.g. <code>1</code> for not parallize the {@link ILogicalQuery}.
	 * @param wantedDegree The wanted degree by the user.
	 * @param maxDegree The maximum possibleDegree.
	 * @return The degree of parallelism which is determined by the two parameters and the load balancer.
	 */
	protected abstract int getDegreeOfParallelismn(int wantedDegree, int maxDegree);

}