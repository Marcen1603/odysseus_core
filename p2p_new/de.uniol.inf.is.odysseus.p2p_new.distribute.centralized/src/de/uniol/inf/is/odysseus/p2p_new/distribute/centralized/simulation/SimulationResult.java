package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.simulation;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph.Graph;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph.GraphNode;

public class SimulationResult {
	ICost<IPhysicalOperator> cost;
	Graph graph;
	
	public SimulationResult(Graph g) {
		this.graph = g;
	}
	
	public Map<Integer,IPhysicalOperator> getPlan() {
		Map<Integer,IPhysicalOperator> plan = new HashMap<Integer, IPhysicalOperator>();
		for(String opType : graph.getNodesGroupedByOpType().keySet()) {
			for(GraphNode gn : graph.getNodesGroupedByOpType().get(opType)) {
				int id = gn.getOperatorID();
				IPhysicalOperator op = gn.getOperator();
				plan.put(id, op);
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
}
