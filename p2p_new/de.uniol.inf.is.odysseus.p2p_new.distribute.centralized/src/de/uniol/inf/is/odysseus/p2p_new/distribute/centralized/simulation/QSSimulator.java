package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.jxta.id.ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.CentralizedDistributor;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph.Graph;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph.GraphNode;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;


/**
 * This class simulates the query-sharing-mechanisms which would be applied,
 * if the Queries in question were to be added to a given running plan.
 * Since the actual QS-mechanism operates on the operators themselves and modifies the DataDictionary in the process,
 * the simulation uses a {@link Graph} consisting of {@link GraphNode}s,
 * which can be cloned and reconnected at will without touching the operators or their respective subscriptions/query-ownerships
 */
public class QSSimulator implements IQuerySharingSimulator {
	private static final Logger LOG = LoggerFactory.getLogger(QSSimulator.class);
	private static QSSimulator instance;
	// Map of the form: replacedNode of the new plan as key, replacementNode of the old plan as value
	private Map<Integer, Integer> shareableIdenticalNodes = new HashMap<Integer,Integer>();
	// Map of the form: Node of the new plan, which had its input changed as key, sourceNode of the old plan as value
	private Map<Integer, Integer> shareableSimilarNodes = new HashMap<Integer,Integer>();
	
	private QSSimulator() {
		
	}
	
	public SimulationResult simulateQuerySharing(Graph graph, OptimizationConfiguration conf) {
		shareableIdenticalNodes.clear();
		shareableSimilarNodes.clear();
		
		boolean restructuringAllowed;
		if (conf.getParameterAllowRestructuringOfCurrentPlan() != null) {
			restructuringAllowed = conf
					.getParameterAllowRestructuringOfCurrentPlan().getValue();
		} else {
			restructuringAllowed = false;
		}

		boolean parameterShareSimilarOperators;
		if (conf.getParameterShareSimilarOperators() != null) {
			parameterShareSimilarOperators = conf.getParameterShareSimilarOperators().getValue();
		} else {
			parameterShareSimilarOperators = false;
		}
		
		// FIXME: TESTING
		parameterShareSimilarOperators = true;
		
		graph.mergeNodesWithIdenticalOperatorID();
		while (removeIdenticalGraphNodes(graph, restructuringAllowed)
				|| (parameterShareSimilarOperators && reconnectSimilarGraphNodes(graph, restructuringAllowed))) {

		}
		SimulationResult simRes = new SimulationResult(graph);
		
		Map<Integer,Integer> shareableIdenticalNodesCopy = new HashMap<Integer,Integer>();
		Map<Integer,Integer> tmp = new HashMap<Integer,Integer>(this.shareableIdenticalNodes);
		tmp.keySet().removeAll(shareableIdenticalNodesCopy.keySet());
		shareableIdenticalNodesCopy.putAll(tmp);

		simRes.setShareableIdenticalNodes(shareableIdenticalNodesCopy);
		
		Map<Integer,Integer> shareableSimilarNodesCopy = new HashMap<Integer,Integer>();
		tmp = new HashMap<Integer,Integer>(this.shareableSimilarNodes);
		tmp.keySet().removeAll(shareableSimilarNodesCopy.keySet());
		shareableSimilarNodesCopy.putAll(tmp);
		simRes.setShareableSimilarNodes(shareableSimilarNodesCopy);
		// After sharing operators with other queries, there's only one operator left which happens to be the attached sender
		// Maybe, the source-operator already feeds its results to another one, in which case we can re-use it
		List<GraphNode> newNodes = simRes.getGraphNodes(true);
		if(newNodes.size() == 1 && newNodes.get(0).getOperatorType().equals(JxtaSenderPO.class.getName())) {
			GraphNode jxtaSenderNew = newNodes.get(0);
			GraphNode sourceOfNewJxtaSender = null;
			// A JxtaSenderPO can only have one source
			for(Subscription<GraphNode> sourceSub : jxtaSenderNew.getSubscribedToSource()) {
				sourceOfNewJxtaSender = sourceSub.getTarget();
			}
			if(sourceOfNewJxtaSender != null) {
				// iterate over its sinks
				for(Subscription<GraphNode> sinkSub : sourceOfNewJxtaSender.getSinkSubscriptions()) {
					GraphNode target = sinkSub.getTarget();
					// we have found another jxta-sender
					if(target.getOperatorType().equals(JxtaSenderPO.class.getName())
							&& target.getOperatorID() != jxtaSenderNew.getOperatorID()) {
						ID pipeID = ((JxtaSenderPO<?>)target.getOperator()).getPipeID();
						// we have to check, if the pipe the sender is writing in is actually a sharedQueryID and not just a planjunction
						if(CentralizedDistributor.getInstance().isSharedQueryID(pipeID)) {
							// TODO: deactivated until the cross-peer-query-management for identical queries is properly implemented
							//replaceNode(jxtaSenderNew, target);
							//simRes.setFullyIdenticalToSharedQuery(pipeID);
						}
					}
				}
			}
		}
		return simRes;
	}

	private boolean removeIdenticalGraphNodes(Graph graph, boolean restructuringAllowed) {
		// consider only operators within the same groups, operators with a different type could hardly be identical
		for(String opType : graph.getNodesGroupedByOpType().keySet()) {
			List<GraphNode> nodes = graph.getNodesGroupedByOpType().get(opType);
			int size = nodes.size();
			for (int i = 0; i < size - 1; i++) {
				for (int j = i + 1; j < size; j++) {
					if (nodes.size() <= 1) {
						return false;
					}
					GraphNode gn1 = nodes.get(i);
					GraphNode gn2 = nodes.get(j);
					if (!gn1.isOld() || !gn2.isOld() || restructuringAllowed) {

						if (gn1.equals(gn2)) {
							continue;
						}
						// IE step 1: Removal of identical operators 
						
						// the operators of the GraphNodes are semantically equal
						if (gn1.hasSameSources(gn2) && gn1.getOperator().isSemanticallyEqual(gn2.getOperator())) {
							// the first operator is old, the second is though and a restructuring of the old plan is prohibited
							if (!restructuringAllowed && gn1.isOld()) {
								GraphNode temp = gn1;
								gn1 = gn2;
								gn2 = temp;
							}
							// put the if of the to-be-removed node in a list along with the id of the operator which replaces it.
							shareableIdenticalNodes.put(gn1.getOperatorID(),gn2.getOperatorID());
							
							// Weed out the unnecessary shares later, when assembling the Physical-Query-Part
							// If an operator is truly dismissible can be told by looking at its dependant sinks
							// so operators may be below this shareable one, but if they fed into another branch of the tree as well,
							// we have to retain this information and can't throw it away,
							// just because one branch has a shareable point somewhere higher up
							
//							// check, if this new shareable node subsumes any already saved node (those superfluous when sharing this one)
//							List<Integer> keysToRemove = new ArrayList<Integer>();
//							for(Entry<Integer,Integer> e : shareableIdenticalNodes.entrySet()) {
//								// this operator was previously shared but is one of the sources of this currently shared operator,
//								// thus this one is higher up so that we may remove the other one
//								if(e.getValue() != gn2.getOperatorID() && gn2.getSourceIDs().contains(e.getValue())) {
//									keysToRemove.add(e.getKey());
//								}
//							}
//							for(int key : keysToRemove) {
//								shareableIdenticalNodes.remove(key);
//							}
							replaceNode(gn1, gn2);
							// Remove the replaced GraphNode from the Graph
							graph.removeNode(gn1);
							// Re-iterate (there could be new identical operators or possibilities to share similar operators)
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean reconnectSimilarGraphNodes(Graph graph, boolean restructuringAllowed) {
		for(String opType : graph.getNodesGroupedByOpType().keySet()) {
			List<GraphNode> nodes = graph.getNodesGroupedByOpType().get(opType);
			int size = nodes.size();
			for (int i = 0; i < size - 1; i++) {
				for (int j = i + 1; j < size; j++) {
					if (nodes.size() <= 1) {
						return false;
					}
					GraphNode gn1 = nodes.get(i);
					GraphNode gn2 = nodes.get(j);
					if ((!gn1.isOld() || !gn2.isOld() || restructuringAllowed)
							&& !(gn1.getOperatorID() == gn2.getOperatorID())
							&& gn1.hasSameSources(gn2)) {
						// IE step 2: Switching of input-Nodes for Nodes, whose Operators can use the results of another GraphNode's operator as a source

						// the operator of GraphNode gn1 is contained in the one of GraphNode gn2
						if (gn1.getOperator() instanceof AbstractPipe
								&& gn2.getOperator() instanceof IPipe
								&& ((AbstractPipe<?, ?>) gn1.getOperator())
								.isContainedIn((IPipe) gn2.getOperator())
								&& (!gn1.isOld() || restructuringAllowed)) {
							replaceInput(gn1, gn2);
							// Re-iterate (there could be new identical operators or possibilities to share similar operators)
							return true;
							// the operator of GraphNode gn2 is contained in the one of GraphNode gn1
						} else if (gn1.getOperator() instanceof AbstractPipe
								&& gn2.getOperator() instanceof IPipe
								&& ((AbstractPipe<?, ?>) gn2.getOperator())
								.isContainedIn((IPipe) gn1.getOperator())
								&& (!gn2.isOld() || restructuringAllowed)) {
							replaceInput(gn2, gn1);
							// Re-iterate (there could be new identical operators or possibilities to share similar operators)
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private void replaceNode(GraphNode toReplace, GraphNode replacement) {
		LOG.debug("Replacing GraphNode " + toReplace.getOperatorID() + " with GraphNode " + replacement.getOperatorID());
		// Fetch all sink-subscriptions
		Collection<Subscription<GraphNode>> sinkSubs = new ArrayList<Subscription<GraphNode>>(toReplace.getSinkSubscriptions());

		// replace the input-nodes of all connected sinks
		for (Subscription<GraphNode> sub : sinkSubs) {
			GraphNode sinkNode = sub.getTarget();
			SDFSchema schema = sub.getSchema();
			int sinkInPort = sub.getSinkInPort();
			int sourceOutPort = sub.getSourceOutPort();

			// delete Subscription
			toReplace.unsubscribeSink(sub);
			// connect the replacement to the sink using the same settings than the old subscription
			replacement.subscribeSink(sinkNode, sinkInPort, sourceOutPort, schema);

		}
		// Fetch sources of the to-be-replaced GraphNode (same as the inputs of replacement)
		Collection<Subscription<GraphNode>> sources = new ArrayList<Subscription<GraphNode>>(toReplace.getSubscribedToSource());
		for (Subscription<GraphNode> sourceSub : sources) {
			GraphNode sourceNode = sourceSub.getTarget();
			sourceNode.unsubscribeSink(sourceSub);
			toReplace.unsubscribeFromSource(sourceSub);
		}
	}
	
	private void replaceInput(GraphNode gn1, GraphNode gn2) {
		// putting a node of the new plan in this map means, that all of its old sources could be discarded
		// EXCEPT if one of those fed into other new operators, in which case it was placed in the map of shareable identical operators
		shareableSimilarNodes.put(gn1.getOperatorID(),gn2.getOperatorID());
		// replace the old source of gn1 with gn2
		Collection<Subscription<GraphNode>> sources = new ArrayList<Subscription<GraphNode>>(gn1.getSubscribedToSource());
		for (Subscription<GraphNode> sub : sources) {
			GraphNode sourceNode = sub.getTarget();
			sourceNode.unsubscribeSink(sub);
			gn1.unsubscribeFromSource(sub);
			gn2.subscribeSink(gn1, sub.getSinkInPort(),sub.getSourceOutPort(), sub.getSchema());
		}
	}
	
	public static QSSimulator getInstance() {
		if(instance == null) {
			instance = new QSSimulator();
		}
		return instance;
	}
	
	
	
}
