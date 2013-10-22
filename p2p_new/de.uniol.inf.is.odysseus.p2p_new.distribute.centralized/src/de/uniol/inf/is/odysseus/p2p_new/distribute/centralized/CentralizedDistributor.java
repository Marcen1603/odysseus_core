package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.query.IQueryOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.ResourceUsageUpdateAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph.Graph;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph.GraphNode;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.resourceusage.ResourceUsage;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service.CostModelService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.simulation.PlanIntersection;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.simulation.PlanJunction;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.simulation.QSSimulator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.simulation.SimulationResult;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.simulation.SplitSimulationResult;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;

public class CentralizedDistributor implements ILogicalQueryDistributor {
	private static CentralizedDistributor instance;
	private static final Logger LOG = LoggerFactory.getLogger(CentralizedDistributor.class);
	protected IQueryOptimizer queryOptimizer = null;
	private Map<PeerID,ResourceUsage> resourceUsages = new HashMap<PeerID,ResourceUsage>();
	private Map<PeerID,ICost<IPhysicalOperator>> planCostEstimates =  new HashMap<PeerID,ICost<IPhysicalOperator>>();
	private Map<PeerID,Map<ID, List<Integer>>> opsForQueriesForPeer = new HashMap<>();
	
	// a map containing the physical plans for each peer known to the master
	private Map<PeerID,Map<Integer,IPhysicalOperator>> operatorPlans = new HashMap<PeerID,Map<Integer,IPhysicalOperator>>();
	private CentralizedDistributorAdvertisementManager manager = CentralizedDistributorAdvertisementManager.getInstance();
	public CentralizedDistributor() {
		instance = this;
	}
	
	IP2PDictionary dictionary = P2PDictionaryService.get();
	private int maximumConsideredAlternatives = 3;
	private static final String DISTRIBUTION_TYPE = "centralized";
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<ILogicalQuery> distributeLogicalQueries(IExecutor sender,
			List<ILogicalQuery> queriesToDistribute,
			QueryBuildConfiguration parameters) {
		if(!manager.isMaster()) {
			LOG.debug("This instance wasn't designated as the master instance and therefore isn't eligible to distribute queries in a centralized manner");
			return null;
		} else if(this.getExecutor() == null) {
			LOG.debug("The centralized distributor hasn't bound an IServerExecutor-instance");
			return null;
		} else if(this.getQueryOptimizer() == null) {
			LOG.debug("The centralized distributor hasn't bound an IQueryOptimizer-instance");
			return null;
		}

		for(ILogicalQuery q : queriesToDistribute) {

			List<List<IPhysicalOperator>> newOperators = new ArrayList<List<IPhysicalOperator>>();
			
			// convert the operators of the new Queries to physical ones
			List<IPhysicalOperator> originalPlan = new ArrayList<IPhysicalOperator>();
			IPhysicalQuery physQ = this.getQueryOptimizer().optimizeQuery(
					this.getExecutor(),
					q,
					new OptimizationConfiguration(parameters),
					this.getExecutor().getDataDictionary(UserManagementProvider.getDefaultTenant()));
			originalPlan.addAll(physQ.getAllOperators());
			for(IPhysicalOperator o : originalPlan) {
				o.addOwner(physQ);
			}

			// find MetaDataUpdatePOs with sources attached, which are for some reason not part of this query plan
			// (usually because of a previously removed query, which left only the top operator in the dictionary)
			List<IPhysicalOperator> toAdd = new ArrayList<IPhysicalOperator>();
			for(IPhysicalOperator o : originalPlan) {
				if(o instanceof MetadataUpdatePO) {
					MetadataUpdatePO mdupo = (MetadataUpdatePO)o;
					List<PhysicalSubscription<IPhysicalOperator>> sourceSubs = mdupo.getSubscribedToSource();
					if(!sourceSubs.isEmpty()) {
						// there should only be one subscription
						PhysicalSubscription<IPhysicalOperator> sub = sourceSubs.get(0);
						IPhysicalOperator source = sub.getTarget();
						if(!originalPlan.contains(source)) {
							toAdd.add(source);
							if(source.isPipe() && !((IPipe)source).getSubscribedToSource().isEmpty()) {
								sub = (PhysicalSubscription<IPhysicalOperator>) ((IPipe)source).getSubscribedToSource().iterator().next();
								source = sub.getTarget();
								if(!originalPlan.contains(source)) {
									toAdd.add(source);
								}
							}
						}
					}
				}
			}
				
			for(IPhysicalOperator o : toAdd) {
				LOG.debug("Added operator " + o + " to the plan, because it was a source of a MetaDataupdatePO");
				//o.addOwner(physQ);
				originalPlan.add(o);
			}
			newOperators.add(originalPlan);

			
			// after the first run through the queryOptimizer, the logical query should have alternative plans,
			// the first one being the original one
			int bestAlternative = -1;
			List<ILogicalOperator> alternatives = q.getAlternativeLogicalPlans();
			for(int i = 1; i < alternatives.size() && i < maximumConsideredAlternatives ; i++) {
				List<IPhysicalOperator> tmp = new ArrayList<IPhysicalOperator>();
				tmp.addAll(this.getQueryOptimizer().optimizeQuery(
						this.getExecutor(),
						q,
						new OptimizationConfiguration(parameters),
						this.getExecutor().getDataDictionary(UserManagementProvider.getDefaultTenant())).getAllOperators());
				newOperators.add(tmp);
			}
			
			Map<Integer,Graph> baseGraphs = new HashMap<Integer, Graph>();
			List<SimulationResult> results = new ArrayList<SimulationResult>();
			List<PeerID> usedPeers = new ArrayList<PeerID>();
			SimulationResult bestResult = null;
			PipeID chosenMasterPeerConnectionPipeID = null;
			ICost<IPhysicalOperator> bestCost = this.getCostModel().getMaximumCost();
			Map<Integer,PipeID> masterPeerConnectionPipeID = new HashMap<Integer, PipeID>();
			for(int x = 0; x < newOperators.size(); x++) {

				List<IPhysicalOperator> bla = newOperators.get(x);
				Map<Integer,IPhysicalOperator> newOperatorsMap = new HashMap<Integer,IPhysicalOperator>();
				for(IPhysicalOperator o : bla) {
					newOperatorsMap.put(o.hashCode(), o);
				}

				// this is the graph representing the new Plan and its connections "as is",
				// we'll work on clones in order to simulate the query-sharing
				Graph baseGraph = new Graph(null, newOperatorsMap);
				baseGraphs.put(x,baseGraph);
				
				// before we do anything with this graph, let's make sure we get the results back to the masternode.
				// we can do that, by setting JxtaSenderPOs on all the top-operators (in case they are pipes/sources).
				// for now, we only generate the PipeID we intend to use in case this SimulationResult will be chosen
				
				
				PipeID pipeID = IDFactory.newPipeID(P2PDictionaryService.get().getLocalPeerGroupID());
				// at this point, we simulated each of the alternatives once, let's continue with the most
				// promising, i.e. the one who yielded the maximum cost-savings on its initial placement
				List<GraphNode> topNodes = baseGraph.getSinkNodes(true);
				for(GraphNode gn : topNodes) {
					// insert a receive and a send-operator
					JxtaSenderPO senderOp = new JxtaSenderPO(pipeID.toURI().toString(), true);
					GraphNode sendGn = new GraphNode(senderOp, senderOp.hashCode(), false);
					SDFSchema schema = gn.getOperator().getOutputSchema();
					senderOp.addOwner(physQ);
					LOG.debug("Trying to connect a jxtaSender-Node with GraphNode " + gn.getOperatorID() + "of type " + gn.getOperatorType());
					baseGraph.addAdditionalNode(sendGn, gn.getOperatorID(), false, 0, 0, schema.clone());
				}
				masterPeerConnectionPipeID.put(x,pipeID);

				// simulate the whole plan once and determine if its costs are higher
				// or lower than the placement of one of the alternative plans
				SimulationResult result = getMostPromisingPlacement(baseGraph, null, parameters, usedPeers);
				if(result == null) {
					LOG.debug("Couldn't find a non-overloaded peer to place the result");
					return null;
				}
				if(result.getCost().compareTo(bestCost) < 0) {
					LOG.debug("Chose a result, because its determined cost of '" + result.getCost().toString() + "' is lower than the previous best cost of '" + bestCost.toString() + "'");
					bestResult = result;
					bestCost = result.getCost();
					bestAlternative = x;
				}
			}
			// at this point, we simulated each of the alternatives once, let's continue with the most
			// promising, i.e. the one who yielded the maximum cost-savings on its initial placement
			List<GraphNode> topNodes = bestResult.getGraph().getSinkNodes(true);
			chosenMasterPeerConnectionPipeID = masterPeerConnectionPipeID.get(bestAlternative);
			for(GraphNode gn : topNodes) {
				SDFSchema schema = gn.getOperator().getOutputSchema();
				// The receivers for those would be unconnected and thus not transformable in the traditional manner
				// no GraphNode for the receiver, because it goes to the master
				// and thus has to find its way into the resulting query as a logical operator
				JxtaReceiverAO receiverOp = new JxtaReceiverAO();
				receiverOp.setPipeID(chosenMasterPeerConnectionPipeID.toURI().toString());
				receiverOp.setOutputSchema(schema);
				receiverOp.setAssignedSchema(schema);
				
				// Put a selection on top of the receiver, because the query won't start otherwise
				SelectAO selection = new SelectAO();
				selection.setPredicate(new TruePredicate());
				selection.setOutputSchema(schema);
				
				selection.subscribeToSource(receiverOp, 0, 0, schema);
				
				// we can now also set the proper receivers on the master, couldn't do that before
				// it wasn't yet clear which alternative would be chosen
				q.setLogicalPlan(selection,true);
			}
			
			results.add(bestResult);
			usedPeers.add(bestResult.getPeer());

			List<SimulationResult> placeableResults = placeable(results);
			// the first result is placeable and fully identical to another, already placed query.
			// this means, we can try and add the receiving operator again on the master, which should merge because of local QS
			// TODO: Send a message to the peer running the sharedQuery telling it to create an additional query under a different ID
			// and to associate it with the operators already present in the plan
			if(placeableResults.size() == results.size() && bestResult.getFullyIdenticalToSharedQuery() != null) {
				ID pipeID = bestResult.getFullyIdenticalToSharedQuery();
				SDFSchema schema = null;
				// iterate over the list of IDs for operators of the shared Query until we find the sender
				for(int i : this.getOpsForQueriesForPeer(bestResult.getPeer()).get(pipeID)) {
					IPhysicalOperator o = this.getOperatorPlans().get(bestResult.getPeer()).get(i);
					if(o instanceof JxtaSenderPO) {
						schema = ((JxtaSenderPO)o).getOutputSchema();
					}
				}
				JxtaReceiverAO receiverOp = new JxtaReceiverAO();
				receiverOp.setPipeID(pipeID.toURI().toString());
				receiverOp.setOutputSchema(schema);
				receiverOp.setAssignedSchema(schema);
				
				// Put a selection on top of the receiver, because the query won't start otherwise
				SelectAO selection = new SelectAO();
				selection.setPredicate(new TruePredicate());
				selection.setOutputSchema(schema);
				
				selection.subscribeToSource(receiverOp, 0, 0, schema);
				
				// no GraphNode for the receiver, because it goes to the master
				// and thus has to find its way into the resulting query as a logical operator
				q.setLogicalPlan(selection, true);
			}
			
			
			// only stop, if we have successfully placed every result on some peer
			while(placeableResults.size() != results.size()) {
				List<SimulationResult> currentlyUnplaceable = new ArrayList<SimulationResult>();
				for(SimulationResult s : results) {
					if(!placeableResults.contains(s)) {
						currentlyUnplaceable.add(s);
					}
				}
				for(SimulationResult s : currentlyUnplaceable) {
					SplitSimulationResult splitResult = splitGraph(s);
					if(splitResult == null) {
						LOG.debug("Can't place Query anywhere.");
						return null;
					}
					Graph newGraph = splitResult.getSurplusNodes();
					SimulationResult additionalResult = getMostPromisingPlacement(newGraph, splitResult.getJunctions(), parameters, usedPeers);

					// currently handled via the splitGraph-method
					//				// we have to update the junctions and create the jxta-send-operators in the source-graph of the "old" simulationResult
					//				for(PlanJunction j : junctions) {
					//					PeerID targetPeer = s.getPeer();
					//					JxtaSenderAO jxtaSenderLOp = new JxtaSenderAO();
					//					PipeID pipeID = IDFactory.newPipeID(P2PDictionaryService.get().getLocalPeerGroupID());
					//					jxtaSenderLOp.setPipeID(pipeID.toString());
					//					jxtaSenderLOp.setOutputSchema(j.getJunctionSchema());
					//					
					//					JxtaSenderPO jxtaOp = new JxtaSenderPO(jxtaSenderLOp);
					//					GraphNode jxtaSendNode = new GraphNode(jxtaOp, jxtaOp.hashCode(), false);
					//					s.getGraph().addAdditionalNode(jxtaSendNode,j.getFlowOriginNode().getOperatorID(), false, j.getJunctionSinkInPort(), j.getJunctionSourceOutPort(), j.getJunctionSchema());
					//					j.setSendingNode(jxtaSendNode);
					//					j.setFlowTargetResult(additionalResult);
					//				}
					results.add(additionalResult);
					usedPeers.add(additionalResult.getPeer());
				}
				// we now have additional results in the results-list,
				// as well as the former non-placeable results which should be placeable now that they've been cut down a little
				placeableResults = placeable(results);
				// continuing means, that all the results were placeable,
				// looping means that there were still some results who couldn't be realized
			}
			// if we reached this point, we have one or more results waiting to get placed on their respective peers.
			ID sharedQueryID = IDFactory.newContentID(P2PDictionaryService.get().getLocalPeerGroupID(), false, String.valueOf(System.currentTimeMillis()).getBytes());
			for(SimulationResult sr : placeableResults) {
				// up until now, we only shuffled the GraphNodes around. But we have to reconnect the underlying operators accordingly,
				// in order to generate the subscription-statements properly.
				sr.getGraph().reconnectAssociatedOperatorsAccordingToGraphLayout(false);
				this.addOperators(sr.getPeer(), sr.getPlan(true));
				this.manager.sendPhysicalPlanToPeer(sr, sharedQueryID);
			}
			PhysicalQueryPartController.getInstance().registerAsMaster(q, sharedQueryID);
		}
		return queriesToDistribute;
	}
	
	
	
	// this method will find a splitpoint within a graph of a SimulationResult,
	// so that the result would henceforth be placeable on the peer. It cuts the graph at the appropriate points and
	// returns the pruned SimulationResult as well as Graph containing the surplus operators as well as a list of the junctions between the
	// old graph and the new Graph/SimulationResult
	@SuppressWarnings("rawtypes")
	public SplitSimulationResult splitGraph(SimulationResult r) {

		Graph oldGraph = r.getGraph();
		List<PlanJunction> junctions = new ArrayList<PlanJunction>();
		// check, if the SimulationResult in question yielded some shareable nodes on the peer, which would make for a nice cutoff-point
		if(!r.getShareableIdenticalNodes().isEmpty()) {
			Map<Integer,Integer> identNodes = r.getShareableIdenticalNodes();
			// key = cutoffoperator-ID, value = depth of the operator in the graph
			Map<Integer,Integer> cutoffPoints =  new HashMap<Integer,Integer>();

			// we iterate over all the shareable nodes found by the simulation
			for(Entry<Integer, Integer> e : identNodes.entrySet()) {
				// this is an (old) node which could be shared with the new plan
				GraphNode currentShareableNode = oldGraph.getGraphNode(e.getValue());
				// its depth in the graph, i.e. the farthest distance from the operator to a source
				int nodeDepth = currentShareableNode.getDepth();
				// we have to determine, if it is a valid cutoff-point
				boolean validCutoff = true;
				// for that, we iterate over all the other cutoff-points
				for(Entry<Integer,Integer> otherCutoff : cutoffPoints.entrySet()) {
					GraphNode otherCutoffNode = oldGraph.getGraphNode(otherCutoff.getKey());
					// let's check, if the currentShareableNode is part of the path to the otherCutoffNode
					// AND
					// if the currentShareableNode has no other sinks in the new plan, which would mean, that its results are used
					// independently and thus we'd have to share this one as well!
					boolean isSubsumedByOther = otherCutoffNode.getIDsOfAllSources().contains(currentShareableNode.getOperatorID());

					if(isSubsumedByOther) {
						validCutoff = false;
						for(Subscription<GraphNode> sub :currentShareableNode.getSinkSubscriptions()) {
							if(!sub.getTarget().isOld()) {
								validCutoff = true;
							}
						}
					}
				}
				if(validCutoff) {
					// maybe the addition of this point would make others obsolete, so let's check
					List<Integer> toDelete = new ArrayList<Integer>();
					// for every other cutoffPoint...
					for(Entry<Integer, Integer> cutoff : cutoffPoints.entrySet()) {
						// retrieve the matching GraphNode
						GraphNode otherCutoffNode = oldGraph.getGraphNode(cutoff.getKey());
						boolean delete = false;
						// check if the other GraphNode lies in the path of the soon-to-add-GraphNode
						boolean otherIsSubsumedByCurrent = currentShareableNode.getIDsOfAllSources().contains(otherCutoffNode.getOperatorID());
						
						if(otherIsSubsumedByCurrent) {
							// if it does, we should delete it
							delete = true;
							// ...unless it's streaming its data to other operators in the new plan
							for(Subscription<GraphNode> sub : otherCutoffNode.getSinkSubscriptions()) {
								if(!sub.getTarget().isOld()) {
									// don't delete the other node, if it has news sinks attached!
									delete = false;
								}
							}
						}
						if(delete) {
							toDelete.add(cutoff.getKey());
						}
					}
					for(int keyToDelete : toDelete) {
						cutoffPoints.remove(keyToDelete);
					}
					// add the point to the list of valid cutoffs
					cutoffPoints.put(e.getValue(), nodeDepth);
				}
			}
			
			// We should have found all the shareable nodes farthest away from the source(s) and could cut it off right there.
			// This would mean, that all we had to do was attach a send-operator to the shared operators
			// and leave the rest of the plan for another peer
			
			Graph detachedGraph = new Graph();
			for(Entry<Integer,Integer> e : cutoffPoints.entrySet()) {
				// find out which old operator we're dealing with and get its sinksubscriptions
				GraphNode oldShareableNode = oldGraph.getGraphNode(e.getKey());
				List<Subscription<GraphNode>> subsToNewOps = new ArrayList<Subscription<GraphNode>>();
				for(Subscription<GraphNode> sub : oldShareableNode.getSinkSubscriptions()) {
					if(!sub.getTarget().isOld()) {
						subsToNewOps.add(sub);
					}
				}
				// insert a receive and a send-operator
				PipeID pipeID = IDFactory.newPipeID(P2PDictionaryService.get().getLocalPeerGroupID());
				JxtaSenderPO senderOp = new JxtaSenderPO(pipeID.toURI().toString(), true);
				GraphNode sendGn = new GraphNode(senderOp, senderOp.hashCode(), true);
				// ports fixed for now, could handle different ports for different outputs
				// via different JxtaSenderPOs, but let's keep things "simple" for now.
				SDFSchema schema = subsToNewOps.get(0).getSchema();
				oldGraph.addAdditionalNode(sendGn, oldShareableNode.getOperatorID(), false, 0, 0, schema.clone());
				
				JxtaReceiverPO receiverOp = new JxtaReceiverPO(pipeID.toURI().toString(),schema);
				GraphNode receiverGn = new GraphNode(receiverOp, receiverOp.hashCode(),true);
				detachedGraph.addNode(receiverGn);
				
				List<GraphNode> flowTargetNodes = new ArrayList<GraphNode>();
				// at this point we have the sender-operator connected to the old shareable operator,
				// put the receiver in the new graph and should now reconnect the connected (new) sinks of this old operator
				// with the receiver instead. They have to reside in different graph-objects for the time being,
				// because an operator might be using results of two different shareableOperators
				for(Subscription<GraphNode> sub : subsToNewOps) {
					int sinkInPort = sub.getSinkInPort();
					int sourceOutPort = sub.getSourceOutPort();
					SDFSchema s = sub.getSchema();
					GraphNode sink = sub.getTarget();
					oldShareableNode.unsubscribeSink(sub);
					receiverGn.subscribeSink(sink, sinkInPort, sourceOutPort, s);
					flowTargetNodes.add(sink);
				}
				
				// all this information above constitutes a PlanJunction
				PlanJunction pj = new PlanJunction(r, oldShareableNode, receiverGn, sendGn, flowTargetNodes, schema);
				junctions.add(pj);
				
			}
			// Now, every new operator which used the results of an old operator is connected with the receivernode instead.
			// Every new operator relying on other, new sources is still only reliant on those.
			// we can now begin to copy all the NEW GraphNodes from the oldGraph into the detachedGraph and should have some
			// independent graphs, where the only (virtual) connections are the pairs of senders and receivers.
			List<GraphNode> toMove = new ArrayList<GraphNode>();
			for(PlanJunction pj : junctions) {
				// the flowTargetNodes are the nodes in the new plan who receive the data sent via a jxta-connection.
				// They and every other node connected to them should be moved to the new Graph
				for(GraphNode gn : pj.getFlowTargetNodes()) {
					gn.collectConnectedGraphNodes(toMove);
				}
			}
			for(GraphNode gn : toMove) {
				oldGraph.removeNode(gn);
				detachedGraph.addNode(gn);
			}

			
			SplitSimulationResult result = new SplitSimulationResult();
			// the junctions involved in this split
			result.setJunctions(junctions);
			// the nodes which were cut from the old Graph and put into a new one, including the new receiver-nodes
			result.setSurplusNodes(detachedGraph);
			// the old Result, which should be perfectly placeable now without
			// all the new Operators and only the additional sender-nodes to worry about
			result.setPlaceableResult(r);
			
			return result;
		} else {
			// no shareableNodes yet, don't split
			return null;
		}
	}
	
	public SimulationResult getMostPromisingPlacement(Graph baseGraph, List<PlanJunction> junctions, QueryBuildConfiguration parameters, List<PeerID> usedPeers) {
		List<SimulationResult> bestResults = new ArrayList<SimulationResult>();
		ICost<IPhysicalOperator> bestCost = this.getCostModel().getMaximumCost();
		for(PeerID peer : getNonOverloadedPeers()) {
			// skip peers, if they're already supposed to host a part of the query or if it's the master
			if(usedPeers.contains(peer) || peer.toString().equals(manager.getMasterID().toString())) {
				continue;
			}
			Map<Integer,IPhysicalOperator> operatorsOnPeer = operatorPlans.get(peer);
			if(operatorsOnPeer == null) {
				operatorsOnPeer = new HashMap<Integer, IPhysicalOperator>();
			}
			if(this.getCostModel() == null) {
				LOG.debug("Centralized distribution not possible without a bound costmodel");
				return null;
			}
			// this is the graph representing BOTH the old plan, the plan of the new query and their connections "as is",
			// we have to copy the baseGraph of the new Query and add the operators of the current Plan to it
			Graph graphCopy = baseGraph.clone();
			graphCopy.addPlan(operatorsOnPeer, false);
			SimulationResult res = null;
			// only simulate on peers who actually have operators running on them
			if(operatorsOnPeer.isEmpty()) {
				res = new SimulationResult(graphCopy);
			} else {
				res = this.getQuerySharingSimulator().simulateQuerySharing(graphCopy, new OptimizationConfiguration(parameters));
			}
			res.setPeer(peer);
			Map<Integer,IPhysicalOperator> mergedOps = res.getPlan(true);
			res.setCost(this.getCostModel().estimateCost(new ArrayList<IPhysicalOperator>(mergedOps.values()), false));
			if(res.getCost().compareTo(bestCost) < 0) {
				bestCost = res.getCost();
				bestResults.add(res);
			}
		}
		// if we have two or more best results with the same estimated costs,
		// we have to choose the peer by considering their current usage,
		// preferring those with lower usages.
		double minimumUsage = Double.MAX_VALUE;
		SimulationResult bestResult = null;
		for(SimulationResult r : bestResults) {
			double peerUsage = getResourceUsageForPeer(r.getPeer()).getOverallUsage();
			if(peerUsage < minimumUsage) {
				minimumUsage = peerUsage;
				bestResult = r;
			}
		}
		// if we have a result and got a list of planjunctions before, we have to change the graphnodes and targetsimulationresults accordingly
		if(bestResult != null && junctions != null && !junctions.isEmpty()) {
			for(PlanJunction pj : junctions) {
				// check if the previous GraphNodes in the junction actually have a counterpart in this graph, and only set those
				List<Integer> flowTargetNodeIDs = new ArrayList<Integer>();
				int receiverNodeID = pj.getReceivingNode().getOperatorID();
				GraphNode receiverNode = bestResult.getGraph().getGraphNode(receiverNodeID);
				for(GraphNode gn : pj.getFlowTargetNodes()) {
					flowTargetNodeIDs.add(gn.getOperatorID()); 
				}
				List<GraphNode> flowTargetNodes = new ArrayList<GraphNode>();
				for(int i : flowTargetNodeIDs) {
					GraphNode replacementNode = bestResult.getGraph().getGraphNode(i);
					if(replacementNode != null) {
						flowTargetNodes.add(replacementNode);
					}
				}
				pj.setReceivingNode(receiverNode);
				pj.setFlowTargetNodes(flowTargetNodes);
				pj.setFlowTargetResult(bestResult);
				// sendNode and flowOriginResult/Node don't change, since they are already fixed.
			}
		}
		return bestResult;

	}
	
	// From a list of SimulationResults, return only those whose costs don't exceed the capacity of their peers
	public List<SimulationResult> placeable(List<SimulationResult> results) {
		List<SimulationResult> r = new ArrayList<SimulationResult>();
		for(SimulationResult res : results) {
			// Despite having the best result in terms of cost savings,
			// the cost of placing this (partial) plan as-is might still be too much given the resources of the peer
			// just add it to the list of placeable results if it doesn't exceed the peer's resources
			if(res.getCost().compareTo(determineBearableCost(res.getPeer())) <= 0 ) {
				r.add(res);
			}
		}
		return r;
	}
	
	// function to determine the load a peer could potentially bear, represented as ICost
	public ICost<IPhysicalOperator> determineBearableCost(PeerID peer) {
		ResourceUsage ru = resourceUsages.get(peer);
		double weightedUsage = ru.getOverallUsage();
		ICost<IPhysicalOperator> costOfCurrentPlan = planCostEstimates.get(peer);
		return CostConverter.calculateBearableCost(costOfCurrentPlan, weightedUsage, ru.getCOMBINED_THRESHOLD(), this.getCostModel());
	}
	
	public boolean isPlaceable(SimulationResult r) {
		if(r.getCost().compareTo(determineBearableCost(r.getPeer())) <= 0 ) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String getName() {
		return DISTRIBUTION_TYPE;
	}
	
	public void setPhysicalPlan(PeerID peer, Map<Integer,IPhysicalOperator> operators) {
		this.operatorPlans.put(peer,operators);
	}
	
	public static CentralizedDistributor getInstance() {
		return instance;
	}
	
	public ICostModel<IPhysicalOperator> getCostModel() {
		return CostModelService.getCostModel();
	}
	
	private IServerExecutor getExecutor() {
		return ServerExecutorService.getServerExecutor();
	}
	
	public void bindQueryOptimizer(IQueryOptimizer queryOptimizer) {
		this.queryOptimizer = queryOptimizer;
		LOG.debug("QueryOptimizer bound: " + queryOptimizer);
	}

	public void unbindQueryOptimizer(IQueryOptimizer queryOptimizer) {
		this.queryOptimizer = null;
		LOG.debug("QueryOptimizer unbound: " + queryOptimizer);
	}
	
	private IQueryOptimizer getQueryOptimizer() {
		return this.queryOptimizer;
	}
	
	private QSSimulator getQuerySharingSimulator() {
		return QSSimulator.getInstance();
	}

	public Map<PeerID, Map<Integer, IPhysicalOperator>> getOperatorPlans() {
		return operatorPlans;
	}
	
	public void addOperators(PeerID peerID, Map<Integer, IPhysicalOperator> newOperators) {
		Map<Integer, IPhysicalOperator> oldOperators = this.getOperatorPlans().get(peerID);
		if(oldOperators == null) {
			this.setPhysicalPlan(peerID, newOperators);
		} else {
			for(int key : newOperators.keySet()) {
				oldOperators.put(key, newOperators.get(key));
			}
		}
		updatePlanCostEstimateForPeer(peerID, this.getOperatorPlans().get(peerID));
	}

	public void updatePlanCostEstimateForPeer(PeerID peerID, Map<Integer, IPhysicalOperator> o) {
		List<IPhysicalOperator> operators = new ArrayList<IPhysicalOperator>(o.values());
		ICost<IPhysicalOperator> newCost = this.getCostModel().estimateCost(operators, false);
		planCostEstimates.put(peerID,newCost);
	}



	public void updateResourceUsage(ResourceUsageUpdateAdvertisement adv) {
		if(resourceUsages.keySet().contains((adv.getPeerID()))
				&& getResourceUsageForPeer(adv.getPeerID()).getTimestamp() >= adv.getTimestamp()) {
			// don't update, if the arrived advertisement is for some reason older than the currently available information
			return;
		}
		ResourceUsage ru = new ResourceUsage();
		ru.setPeerID(adv.getPeerID());
		ru.setCpu_usage(adv.getCpu_usage());
		ru.setMem_free(adv.getMem_free());
		ru.setMem_used(adv.getMem_used());
		ru.setMem_total(adv.getMem_total());
		ru.setTimestamp(adv.getTimestamp());
		ru.setNetworkUsage(adv.getNetworkUsage());
		resourceUsages.put(adv.getPeerID(), ru);
	}
	
	public void setInitialResourceUsageForPeer(PeerID peerID) {
		ResourceUsage ru = new ResourceUsage();
		ru.setPeerID(peerID);
		ru.setCpu_usage(0.0);
		ru.setMem_free(Double.MAX_VALUE);
		ru.setMem_used(0.0);
		ru.setMem_total(Double.MAX_VALUE);
		ru.setTimestamp(System.currentTimeMillis());
		ru.setNetworkUsage(0.0);
		resourceUsages.put(peerID, ru);
	}
	
	public ResourceUsage getResourceUsageForPeer(PeerID peerID) {
		return resourceUsages.get(peerID);
	}
	
	/**
	 * Retrieves a list of peers eligible for adding more plans,
	 * i.e. peers whose memory- or cpu-usage-thresholds haven't yet been reached
	 * Only includes peers for which resource-information is available
	 */
	private List<PeerID> getNonOverloadedPeers() {
		List<PeerID> result = new ArrayList<PeerID>();
		for(PeerID peerID : operatorPlans.keySet()) {
			if(!(getResourceUsageForPeer(peerID) == null) && !getResourceUsageForPeer(peerID).isOverLoaded()) {
				result.add(peerID);
			}
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void processIntersections(PeerID peerID, List<PlanIntersection> intersections) {
		Map<Integer,IPhysicalOperator> operators = this.operatorPlans.get(peerID);
		for(PlanIntersection pi : intersections) {
			// The old operator is a source, the new one a sink
			ISource oldOp = (ISource)operators.get(pi.getOldOperatorID());
			ISink newOp = (ISink)operators.get(pi.getNewOperatorID());
			oldOp.subscribeSink(newOp, pi.getSinkInPort(), pi.getSourceOutPort(), pi.getSchema());
		}
	}



	public Map<ID, List<Integer>> getOpsForQueriesForPeer(PeerID peerID) {
		return opsForQueriesForPeer.get(peerID);
	}

	public void setOpsForQueryForPeer(PeerID peerID, ID sharedQueryID, List<Integer> opsForQuery) {
		
		Map<ID,List<Integer>> map = getOpsForQueriesForPeer(peerID);
		if(map == null) {
			map = new HashMap<ID,List<Integer>>();
		}
		map.put(sharedQueryID, opsForQuery);
		this.opsForQueriesForPeer.put(peerID, map);
	}
	
	public void removeQueryFromPeer(PeerID peerID, ID sharedQueryID) {
		Map<ID,List<Integer>> queriesOnThatPeer = this.getOpsForQueriesForPeer(peerID);
		if(queriesOnThatPeer == null) {
			return;
		}
		List<Integer> queryOperators = queriesOnThatPeer.get(sharedQueryID);
		if(queryOperators == null) {
			return;
		}
		removeOperatorsOfQueryFromOperatorPlansForPeer(peerID, sharedQueryID, queryOperators);
		this.getOpsForQueriesForPeer(peerID).remove(sharedQueryID);
	}
	
	private void removeOperatorsOfQueryFromOperatorPlansForPeer(PeerID peerID, ID sharedQueryID, List<Integer> queryOperators) {
		// only remove those operators, who aren't used by any other query anymore
		for(int i : queryOperators) {
			if(!usedByAnotherQueryThan(peerID, i, sharedQueryID)) {
				removeOperator(peerID,i);
			}
		}
	}
	
	public void removeQuery(ID sharedQueryID) {
		for(PeerID peerID : this.getPeersInvolvedInQuery(sharedQueryID)) {
			removeQueryFromPeer(peerID, sharedQueryID);
		}
	}
	
	public List<PeerID> getPeersInvolvedInQuery(ID sharedQueryID) {
		List<PeerID> result = new ArrayList<PeerID>();
		for(PeerID peerID : this.opsForQueriesForPeer.keySet()) {
			if(this.getOpsForQueriesForPeer(peerID).containsKey(sharedQueryID)) {
				result.add(peerID);
			}
		}
		return result;
	}


	/**
	 * determines, whether or not an operator is used by a query other than the one specified
	 */
	private boolean usedByAnotherQueryThan(PeerID peerID, int operatorID, ID sharedQueryID) {
		Map<ID, List<Integer>> operatorsForQueries = this.getOpsForQueriesForPeer(peerID);
		for(Entry<ID,List<Integer>> query : operatorsForQueries.entrySet()) {
			if(query.getKey() != sharedQueryID && query.getValue().contains(operatorID)) {
				return true;
			}
		}
		return false;
	}

	private void removeOperator(PeerID peerID, int operatorID) {
		this.operatorPlans.get(peerID).remove(operatorID);
	}



	public CentralizedDistributorAdvertisementManager getManager() {
		return manager;
	}
	
	public boolean isSharedQueryID(ID id) {
		for(Map<ID,List<Integer>> sharedQueries : this.opsForQueriesForPeer.values()) {
			for(ID sharedID : sharedQueries.keySet()) {
				if(sharedID.equals(id)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int getNumberOfRunningQueriesForPeer(PeerID peerID) {
		return this.opsForQueriesForPeer.get(peerID).keySet().size();
	}
	
	public int getNumberOfRunningQueries() {
		return getNumberOfRunningQueriesForPeer(this.getManager().getLocalID());
	}
	
}
