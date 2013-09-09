package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph.Graph;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph.GraphNode;


/**
 * This class simulates the query-sharing-mechanisms which would be applied,
 * if the Queries in question were to be added to a given running plan.
 * Since the actual QS-mechanism operates on the operators themselves and modifies the DataDictionary in the process,
 * the simulation uses a {@link Graph} consisting of {@link GraphNode}s,
 * which can be cloned and reconnected at will without touching the operators or their respective subscriptions/query-ownerships
 */
public class QSSimulator implements IQuerySharingSimulator {
	private static QSSimulator instance;
	
	private QSSimulator() {
		
	}
	
	public SimulationResult simulateQuerySharing(Graph graph, OptimizationConfiguration conf) {

		
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
		while (removeIdenticalGraphNodes(graph, restructuringAllowed)
				|| (parameterShareSimilarOperators && reconnectSimilarGraphNodes(graph, restructuringAllowed))) {

		}
		return new SimulationResult(graph);
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
						if (gn1.getOperator().isSemanticallyEqual(gn2.getOperator())) {
							// the first operator is old, the second is though and a restructuring of the old plan is prohibited
							if (!restructuringAllowed && gn1.isOld()) {
								GraphNode temp = gn1;
								gn1 = gn2;
								gn2 = temp;
							}

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
							&& !(gn1.getOperatorID() == gn2.getOperatorID())) {
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
		// replace the old source of op1 with op2
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
