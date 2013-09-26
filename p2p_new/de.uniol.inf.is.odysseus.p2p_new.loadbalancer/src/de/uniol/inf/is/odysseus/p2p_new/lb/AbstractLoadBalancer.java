package de.uniol.inf.is.odysseus.p2p_new.lb;

import java.util.ArrayList;
import java.util.Arrays;
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

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.IDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDistributionType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.SimplePlanPrinter;
import de.uniol.inf.is.odysseus.p2p_new.distribute.DistributionHelper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPart;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPartController;
import de.uniol.inf.is.odysseus.p2p_new.lb.fragmentation.FragmentationHelper;
import de.uniol.inf.is.odysseus.p2p_new.lb.fragmentation.Replication;
import de.uniol.inf.is.odysseus.p2p_new.lb.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;

/**
 * An abstract implementation of a load balancer.
 * @author Michael Brand
 */
public abstract class AbstractLoadBalancer implements ILogicalQueryDistributor {

	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(AbstractLoadBalancer.class);
	
	/**
	 * The string representation of {@link #MIN_DEGREE_VALUE}.
	 */
	public static final String MIN_DEGREE_PARAM = "min";
	
	/**
	 * The minimum degree of parallelism.
	 */
	public static final int MIN_DEGREE_VALUE = 1;
	
	/**
	 * The string representation of {@link #MAX_DEGREE_VALUE}.
	 */
	public static final String MAX_DEGREE_PARAM = "max";
	
	/**
	 * The maximum degree of parallelism.
	 */
	public static final int MAX_DEGREE_VALUE = Integer.MAX_VALUE;
	
	/**
	 * The beginning of all names for {@link JxtaReceiverAO}s. <br />
	 * It will be enhanced by identifying numbers.
	 */
	// TODO source out to DistributionHelper
	public static final String ACCESS_BASSE_NAME = "JxtaReceiver_";

	/**
	 * The beginning of all names for {@link JxtaSenderAO}s. <br />
	 * It will be enhanced by identifying numbers.
	 */
	// TODO source out to DistributionHelper
	public static final String SENDER_BASE_NAME = "JxtaSender_";
	
	/**
	 * The name for query parts to be executed locally.
	 * @see QueryPart#getDestinationName()
	 */
	// TODO source out to DistributionHelper
	public static final String LOCAL_DESTINATION_NAME = "local";
	
	/**
	 * The number of the next connection to be generated. <br />
	 * This number will enhance {@link #ACCESS_BASSE_NAME} and {@link #SENDER_BASE_NAME}.
	 */
	// TODO source out to DistributionHelper
	private static int connectionCounter = 0;
	
	/**
	 * The number of the next peer to be assigned (round-robin).
	 */
	// TODO source out to IPeerAssignment
	private static int peerCounter = 0;
	
	@Override
	public abstract String getName();
	
	/**
	 * Distributes a list of logical query as follows (for every query): <br />
	 * 1. Make copies of the query. <br />
	 * 2. Use the fragmentation strategy to merge the copies. <br />
	 * 3. Create the part of fragmentation (StreamAO and, if existing, WindowAO of the source to be fragmented plus the operator for fragmentation. <br />
	 * 4. Create the part of data reunion (The operator for data reunion plus FileSinkAO or other operators, which must be executed after the data reunion. <br />
	 * 5. Create the remaining parts of the query according to the concrete load balancer. <br />
	 * 6. Assign the query parts to their peers and distribute them.
	 * @param executor The executor calling.
	 * @param queries The logical queries to be distributed.
	 * @param parameters The {@link QueryBuildConfiguration}.
	 * @return The logical queries to be executed locally.
	 */
	@Override
	public List<ILogicalQuery> distributeLogicalQueries(IExecutor executor,
			List<ILogicalQuery> queries, QueryBuildConfiguration cfg) {
		
		if(queries == null || queries.isEmpty()) {
			
			// Nothing to distribute
			return queries;
			
		}
		Preconditions.checkNotNull(executor);
		Preconditions.checkNotNull(cfg);
		
		// The return value
		List<ILogicalQuery> distributedQueries = Lists.newArrayList();
		
		// A list of all available remote peers
		final Collection<PeerID> remotePeerIDs = P2PDictionaryService.get().getRemotePeerIDs();
		
		// The degree of parallelism and also the number of fragments, if fragmentation is used
		int degreeOfParallelism = 1;
		
		// Check available peers
		if(remotePeerIDs.isEmpty()) {
			
			LOG.debug("Could not find any remote peers to distribute logical query. Executing all locally.");
			return queries;
			
		} else DistributionHelper.logPeerStatus(remotePeerIDs);
		
		// The pair of the source name and the fragmentation strategy or null.
		Optional<Pair<String, IDataFragmentation>> fragmentationStrategy = 
				FragmentationHelper.parseFromConfiguration(cfg, (IServerExecutor) executor);
		if(fragmentationStrategy.isPresent())
			LOG.debug("Using '{}' as fragmentation strategy for the source '{}'.", 
					fragmentationStrategy.get().getE2().getName(), fragmentationStrategy.get().getE1());
		else LOG.debug("Using replication for all sources.");
		
		// Determination of the degree of parallelism
		degreeOfParallelism = determineDegreeOfParallelismn(cfg, remotePeerIDs);
		LOG.debug("Degree of parallelism set to '{}'.", degreeOfParallelism);
		
		// Check the number of fragments if fragmentation is selected
		if(fragmentationStrategy.isPresent() && degreeOfParallelism < 2) {
			
			LOG.warn("Degree of parallelism must be at least 2 to use data fragmentation. Turned off data fragmentation.");
			fragmentationStrategy = Optional.absent();
			
		}
		
		for(final ILogicalQuery originQuery : queries) {
			
			// Distribute the single query
			distributedQueries.add(distributeLogicalQuery(originQuery, cfg, fragmentationStrategy, degreeOfParallelism, remotePeerIDs));
			
		}
		
		return distributedQueries;
		
	}
	
	/**
	 * Determines the degree of parallelism as the minimum of the degree given by the user and the number of available peers.
	 * @param parameters The {@link QueryBuildConfiguration}.
	 * @param remotePeerIDs A list of all available peers.
	 * @return The degree of parallelism.
	 */
	protected int determineDegreeOfParallelismn(QueryBuildConfiguration parameters, Collection<PeerID> remotePeerIDs) {
		
		Preconditions.checkNotNull(parameters);
		Preconditions.checkArgument(parameters.contains(ParameterDistributionType.class));
		Preconditions.checkNotNull(remotePeerIDs);
		Preconditions.checkArgument(!remotePeerIDs.isEmpty());
		
		// The separator for the parameters
		final String separator = " ";
		
		// The arguments of the parameter ParameterDistributionType
		final String[] strParameters = parameters.get(ParameterDistributionType.class).getValue().split(separator);
		
		// The wanted degree of parallelism by the user
		int wantedDegreeOfParallelism = MIN_DEGREE_VALUE;
		
		// Read out the wanted degree of parallelism
		if(strParameters.length > 1) {
			
			if(strParameters[1].toLowerCase().equals(MIN_DEGREE_PARAM))
				wantedDegreeOfParallelism = MIN_DEGREE_VALUE;
			else if(strParameters[1].toLowerCase().equals(MAX_DEGREE_PARAM))
				wantedDegreeOfParallelism = MAX_DEGREE_VALUE;
			else try {
			
				wantedDegreeOfParallelism = Integer.parseInt(strParameters[1]);
				if(wantedDegreeOfParallelism < MIN_DEGREE_VALUE) {
					
					LOG.warn("{} is an invalid degree of parallelism. Degree settet to {}", strParameters[1], MIN_DEGREE_VALUE);
					wantedDegreeOfParallelism = MIN_DEGREE_VALUE;
					
				}
				
			} catch(NumberFormatException e) {
				
				e.printStackTrace();
				LOG.error("Could not parse {} to an integer. Degree settet to {}", strParameters[1], MIN_DEGREE_VALUE);
				
			}
			
		}
		
		return Math.min(wantedDegreeOfParallelism, remotePeerIDs.size());
		
	}
	
	/**
	 * Distributes a logical query as follows: <br />
	 * 1. Make copies of the query. <br />
	 * 2. Use the fragmentation strategy to merge the copies. <br />
	 * 3. Create the part of fragmentation (StreamAO and, if existing, WindowAO of the source to be fragmented plus the operator for fragmentation. <br />
	 * 4. Create the part of data reunion (The operator for data reunion plus FileSinkAO or other operators, which must be executed after the data reunion. <br />
	 * 5. Create the remaining parts of the query according to the concrete load balancer. <br />
	 * 6. Assign the query parts to their peers and distribute them.
	 * @param query The logical query to be distributed.
	 * @param parameters The {@link QueryBuildConfiguration}.
	 * @param fragmentationStrategy The pair of source name and fragmentation strategy if set.
	 * @param degreeOfParallelism The degree of parallelism.
	 * @param remotePeerIDs A collection of all available peers.
	 * @return The logical query to be executed locally.
	 */
	protected ILogicalQuery distributeLogicalQuery(ILogicalQuery query, QueryBuildConfiguration parameters, 
			Optional<Pair<String, IDataFragmentation>> fragmentationStrategy, int degreeOfParallelism, Collection<PeerID> remotePeerIDs) {
		
		Preconditions.checkNotNull(query);
		Preconditions.checkNotNull(parameters);
		Preconditions.checkArgument(degreeOfParallelism > 0);
		Preconditions.checkNotNull(remotePeerIDs);
		Preconditions.checkArgument(!remotePeerIDs.isEmpty());
		
		// A collection of all query parts
		Collection<QueryPart> queryParts = Lists.newArrayList();
		
		// A mapping of peers to the query part to be executed on that peer
		Map<QueryPart, PeerID> peerToQueryPartMap = Maps.newHashMap();
		
		// The number of used connections
		int connectionNo = connectionCounter++;
		
		// A list of all copies of the query
		List<ILogicalQuery> queryCopies = Lists.newArrayList();
		
		// A list of all logical plans
		List<ILogicalOperator> logicalPlans = Lists.newArrayList();
		
		// The new whole logical plan after fragmentation
		ILogicalOperator completeLogicalPlan = null;
		
		// A mapping of all operators the the number of the copy they are part of.
		// key = degreeOfParallelism -> source part
		// key = degreeOfParallelism + 1 -> data reunion part
		Map<Integer, List<ILogicalOperator>> operatorsToCopyNoMap = Maps.newHashMap();
		operatorsToCopyNoMap.put(degreeOfParallelism, new ArrayList<ILogicalOperator>());
		operatorsToCopyNoMap.put(degreeOfParallelism + 1, new ArrayList<ILogicalOperator>());
		
		// Make copies of the query
		for(int copyNo = 0; copyNo < degreeOfParallelism; copyNo++) {
			
			// The copy of the query
			ILogicalQuery queryCopy = DistributionHelper.copyLogicalQuery(query);
			
			// A collection of all operators of the query
			Collection<ILogicalOperator> operators = Lists.newArrayList();
			RestructHelper.collectOperators(queryCopy.getLogicalPlan(), operators);
			
			// Use destination name to identify all operators belonging to that copy.
			// Needed for identification after fragmentation.
			for(ILogicalOperator operator : operators) {
				
				if(operator.getDestinationName() == null || !operator.getDestinationName().equalsIgnoreCase(LOCAL_DESTINATION_NAME))
					operator.setDestinationName(String.valueOf(copyNo));
				
			}
			
			queryCopies.add(queryCopy);
			logicalPlans.add(queryCopy.getLogicalPlan());
			operatorsToCopyNoMap.put(copyNo, new ArrayList<ILogicalOperator>());
			
		}
		
		if(fragmentationStrategy.isPresent())
			completeLogicalPlan = fragmentationStrategy.get().getE2().fragment(logicalPlans, parameters, fragmentationStrategy.get().getE1());
		else if(degreeOfParallelism == 1)
			completeLogicalPlan = logicalPlans.iterator().next();
		else completeLogicalPlan = new Replication().fragment(logicalPlans, parameters, null);
		
		// All operators of the logical plan
		List<ILogicalOperator> operators = Lists.newArrayList();
		RestructHelper.collectOperators(completeLogicalPlan, operators);
		
		// Map each operator to its query
		for(ILogicalOperator operator : operators) {
			
			if(operator.getDestinationName().equals(FragmentationHelper.FRAGMENTATION_DESTINATION_NAME))
				operatorsToCopyNoMap.get(degreeOfParallelism).add(operator);
			else if(operator.getDestinationName().equals(FragmentationHelper.REUNION_DESTINATION_NAME))
				operatorsToCopyNoMap.get(degreeOfParallelism + 1).add(operator);
			else operatorsToCopyNoMap.get(Integer.parseInt(operator.getDestinationName())).add(operator);
			
		}
		
		// Create source part and reunion part
		if(!operatorsToCopyNoMap.get(degreeOfParallelism).isEmpty())
			queryParts.add(new QueryPart(operatorsToCopyNoMap.get(degreeOfParallelism)));
		if(!operatorsToCopyNoMap.get(degreeOfParallelism + 1).isEmpty())
			queryParts.add(new QueryPart(operatorsToCopyNoMap.get(degreeOfParallelism + 1), LOCAL_DESTINATION_NAME));

		for(int copyNo = 0; copyNo < degreeOfParallelism; copyNo++) {
			
			// A collection of the query parts for this copy
			List<QueryPart> partsOfCopy = Lists.newArrayList();
			
			// Determine query parts
			partsOfCopy = determineQueryParts(operatorsToCopyNoMap.get(copyNo));
			LOG.debug("Got '{}' parts of logical query '{}'", partsOfCopy.size(), queryCopies.get(copyNo));
			
			// Split parts if needed
			partsOfCopy = splitQueryParts(partsOfCopy);
			
			// Merge parts if needed
			queryParts.addAll(mergeQueryParts(partsOfCopy));
			
		}
		
		// Assign query parts to peers and generate connections
		peerToQueryPartMap = assignQueryParts(remotePeerIDs, P2PDictionaryService.get().getLocalPeerID(), queryParts);
		DistributionHelper.generatePeerConnections(peerToQueryPartMap, ACCESS_BASSE_NAME + connectionNo + "_", 
				SENDER_BASE_NAME + connectionNo + "_");
		
		// Publish all remote parts and return the local ones
		return distributeAndTransformParts(queryParts, peerToQueryPartMap, parameters, query.toString(), degreeOfParallelism);
		
	}
	
	/**
	 * Determines the query parts of a query.
	 * @param operators A list of all operators of the query.
	 * @return A list of the query parts.
	 */
	protected abstract List<QueryPart> determineQueryParts(List<ILogicalOperator> operators);
	
	/**
	 * Splits query parts, which have operators within to be executed locally to several parts having these operators an exclusive one.
	 * @param parts A list of all query parts.
	 * @return A list of all parts inclusive the splitted ones.
	 */
	protected List<QueryPart> splitQueryParts(List<QueryPart> parts) {
		
		Preconditions.checkNotNull(parts);
		
		// The return value
		List<QueryPart> splittedParts = Lists.newArrayList();
		
		for(QueryPart part : parts) {
			
			// The iterator for the operators
			Iterator<ILogicalOperator> operatorsIter = part.getOperators().iterator();
			
			// The index for the operators
			int operatorNo = 0;
			
			// The index of the operator where the last split was done
			int lastSplitPoint = -1;
			
			while(operatorsIter.hasNext()) {
				
				// The current operator
				ILogicalOperator operator = operatorsIter.next();
				
				if(operator.getDestinationName().equalsIgnoreCase(LOCAL_DESTINATION_NAME)) {
					
					// Split
					List<ILogicalOperator> operators = Lists.newArrayList();
					Iterator<ILogicalOperator> iter = part.getOperators().iterator();
					int index = 0;
					while(iter.hasNext() && index < operatorNo) {
						
						if(index > lastSplitPoint)
							operators.add(iter.next());
						index++;
						
					}
					if(!operators.isEmpty())
						splittedParts.add(new QueryPart(operators));
					splittedParts.add(new QueryPart(Arrays.asList(operator), LOCAL_DESTINATION_NAME));
					lastSplitPoint = operatorNo;
					
				}
				
				operatorNo++;
				
			}
			
			// Process operators after last split point
			if(lastSplitPoint < part.getOperators().size() - 1) {
				
				List<ILogicalOperator> operators = Lists.newArrayList();
				Iterator<ILogicalOperator> iter = part.getOperators().iterator();
				int index = 0;
				while(iter.hasNext()) {
					
					if(index > lastSplitPoint)
						operators.add(iter.next());
					index++;
					
				}
				if(!operators.isEmpty())
					splittedParts.add(new QueryPart(operators));
				
			}
			
		}
		
		return splittedParts;
		
	}
	
	/**
	 * Merges consecutive query parts, which shall be executed locally to one query part.
	 * @param parts A list of all query parts.
	 * @return A list of all parts inclusive the merged ones.
	 */
	protected List<QueryPart> mergeQueryParts(List<QueryPart> parts) {
		
		Preconditions.checkNotNull(parts);
		if(parts.size() < 2)
			return parts;	
		
		// The return value
		List<QueryPart> mergedParts = Lists.newArrayList();
		
		for(int partNo = 0; partNo < parts.size() - 1; partNo++) {
			
			if(parts.get(partNo).getDestinationName().isPresent() && parts.get(partNo).getDestinationName().get().equals(LOCAL_DESTINATION_NAME)) {
				
				if(parts.get(partNo + 1).getDestinationName().isPresent() && 
						parts.get(partNo + 1).getDestinationName().get().equals(LOCAL_DESTINATION_NAME)) {
					
					// Merge
					Collection<ILogicalOperator> mergedOperators = parts.get(partNo).getOperators();
					mergedOperators.addAll(parts.get(partNo + 1).getOperators());
					mergedParts.add(new QueryPart(mergedOperators, LOCAL_DESTINATION_NAME));
					
					// skip merged part
					partNo++;
					
				} else mergedParts.add(parts.get(partNo));
				
			} else mergedParts.add(parts.get(partNo));
			
		}
		
		return mergedParts;
		
	}
	
	/**
	 * Assigns query parts to peers, where they shall be executed.
	 * @param remotePeerIDs A collection of all available peers.
	 * @param localPeerID The ID of the local peer.
	 * @param queryParts 
	 * @return A collection of query parts to be assigned.
	 */
	// TODO source out to IPeerAssignment
	protected Map<QueryPart, PeerID> assignQueryParts(Collection<PeerID> remotePeerIDs, PeerID localPeerID, Collection<QueryPart> queryParts) { 
		
		Preconditions.checkNotNull(remotePeerIDs, "remotePeerIDs must be not null!");
		Preconditions.checkArgument(remotePeerIDs.size() > 0, "remotePeerIDs must be not empty!");
		Preconditions.checkNotNull(localPeerID, "localPeerID must be not null!");
		Preconditions.checkNotNull(queryParts, "queryParts must be not null!");
		
		// The return value
		final Map<QueryPart, PeerID> distributed = Maps.newHashMap();
		
		// The iterator for the query parts.
		final Iterator<QueryPart> partsIter = queryParts.iterator();
		
		while(partsIter.hasNext()) {
			
			// The current query part
			QueryPart part = partsIter.next();
			
			// The name of the assigned peer if present
			Optional<String> peerName;
			
			// The ID of the assigned peer
			PeerID peerID = null;
				
			if(part.getDestinationName().isPresent() && part.getDestinationName().get().equals(LOCAL_DESTINATION_NAME)) {
					
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
	 * Distributes query parts to their assigned peer and transforms all query parts to be executed locally into a logical query.
	 * @param queryParts A collection of query parts to be distributed.
	 * @param queryPartDistributionMap The mapping of assigned peer IDs to query parts.
	 * @param parameters The {@link QueryBuildConfiguration}.
	 * @param queryName The name of the origin query for transformation.
	 * @param degreeOfParallelism The degree of parallelism.
	 * @return The logical query to be executed locally.
	 */
	// TODO source out to DistributionHelper
	protected ILogicalQuery distributeAndTransformParts(Collection<QueryPart> queryParts, Map<QueryPart, 
			PeerID> queryPartDistributionMap, QueryBuildConfiguration parameters, String queryName, int degreeOfParallelism) {
		
		Preconditions.checkNotNull(queryPartDistributionMap, "queryPartDistributionMap must be not null!");
		Preconditions.checkNotNull(queryParts, "queryParts must be not null!");
		Preconditions.checkNotNull(parameters, "transCfg must be not null!");
		
		// Generate an ID for the shared query
		final ID sharedQueryID = DistributionHelper.generateSharedQueryID();
		
		// Get all queryParts of this query mapped with the executing peer
		final Map<QueryPart, PeerID> queryPartPeerMap = Maps.newHashMap();
		for(QueryPart part : queryParts) {
			
			QueryPart preparedPart = DistributionHelper.replaceStreamAOs(part);
			queryPartPeerMap.put(preparedPart, queryPartDistributionMap.get(part));
			
		}
		
		// publish the queryparts and transform the parts which shall be executed locally into a query
		ILogicalQuery localQuery = DistributionHelper.transformToQuery(this.distributeParts(queryPartPeerMap, sharedQueryID, parameters), queryName);
		
		// Registers an sharedQueryID as a master to resolve removed query parts
		QueryPartController.getInstance().registerAsMaster(localQuery, sharedQueryID);
		
		return localQuery;
		
	}
	
	/**
	 * Distributes query parts to their assigned peer.
	 * @param queryPartDistributionMap The mapping of assigned peer IDs to query parts.
	 * @param sharedQueryID The shared query id.
	 * @param parameters The {@link QueryBuildConfiguration}.
	 * @return A list of all query parts to be executed locally.
	 */
	// TODO source out to DistributionHelper
	protected List<QueryPart> distributeParts(Map<QueryPart, PeerID> queryPartDistributionMap, ID sharedQueryID, QueryBuildConfiguration parameters) {
		
		Preconditions.checkNotNull(queryPartDistributionMap, "queryPartDistributionMap must be not null!");
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must be not null!");
		Preconditions.checkNotNull(parameters, "transCfg must be not null!");
		
		// The query parts to be executed locally
		final List<QueryPart> localParts = Lists.newArrayList();
		
		// The ID of the local peer
		final PeerID ownPeerID = P2PDictionaryService.get().getLocalPeerID();

		for(final QueryPart part : queryPartDistributionMap.keySet()) {
			
			// The ID of the assigned peer for that query part
			final PeerID assignedPeerID = queryPartDistributionMap.get(part);
			
			if(assignedPeerID.equals(ownPeerID)) {
				
				localParts.add(part);
				LOG.debug("QueryPart {} locally stored", part);
				
			} else {
				
				SimplePlanPrinter<ILogicalOperator> printer = new SimplePlanPrinter<ILogicalOperator>();
				LOG.debug("Plan of the querypart to publish:\n{}", printer.createString(part.getLogicalPlan()));
				DistributionHelper.publish(part, assignedPeerID, sharedQueryID, parameters);
				
			}

		}

		return localParts;
		
	}

}