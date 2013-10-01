package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.PlanIntersection;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph.Graph;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph.GraphNode;

public class SimulationResult {
	private PeerID peer;
	private ICost<IPhysicalOperator> cost;
	private Graph graph;
	
	// Map of the form: replacedNode of the new plan as key, replacementNode of the old plan as value
	private Map<Integer,Integer> shareableIdenticalNodes = new HashMap<Integer, Integer>();
	// Map of the form: Node of the new plan, which had its input changed as key, sourceNode of the old plan as value
	private Map<Integer,Integer> shareableSimilarNodes = new HashMap<Integer, Integer>();
	
	public SimulationResult(Graph g) {
		this.graph = g;
	}
	
	public Map<Integer,IPhysicalOperator> getPlan(boolean onlyNew) {
		Map<Integer,IPhysicalOperator> plan = new HashMap<Integer, IPhysicalOperator>();
		for(String opType : graph.getNodesGroupedByOpType().keySet()) {
			for(GraphNode gn : graph.getNodesGroupedByOpType().get(opType)) {
				if(!(onlyNew && gn.isOld())) {
					int id = gn.getOperatorID();
					IPhysicalOperator op = gn.getOperator();
					plan.put(id, op);
				}
			}
		}
		return plan;
	}
	
	public ICost<IPhysicalOperator> getCost() {
		return cost;
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public void setCost(ICost<IPhysicalOperator> cost) {
		this.cost = cost;
	}

	public Map<Integer, Integer> getShareableIdenticalNodes() {
		return shareableIdenticalNodes;
	}

	public void setShareableIdenticalNodes(Map<Integer, Integer> shareableIdenticalNodes) {
		this.shareableIdenticalNodes = shareableIdenticalNodes;
	}

	public Map<Integer,Integer> getShareableSimilarNodes() {
		return shareableSimilarNodes;
	}

	public void setShareableSimilarNodes(Map<Integer,Integer> shareableSimilarNodes) {
		this.shareableSimilarNodes = shareableSimilarNodes;
	}

	public PeerID getPeer() {
		return peer;
	}

	public void setPeer(PeerID peer) {
		this.peer = peer;
	}
	
	public List<PlanIntersection> getIntersections() {
		List<PlanIntersection> result = new ArrayList<PlanIntersection>();
		for(GraphNode gn : this.getGraph().getGraphNodesUngrouped(true)) {
			if(gn.isSink()) {
				for(Subscription<GraphNode> sub : gn.getSubscribedToSource()) {
					// we have found a new GraphNode, which has a subscription to an old one.
					if(sub.getTarget().isOld()) {
						PlanIntersection pi = new PlanIntersection(sub.getTarget().getOperatorID(),
								gn.getOperatorID(),
								sub.getSinkInPort(),
								sub.getSourceOutPort(),
								sub.getSchema());
						result.add(pi);
					}
				}
			}
		}
		return result;
	}
	
}
