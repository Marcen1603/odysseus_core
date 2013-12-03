package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.simulation;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph.GraphNode;

public class PlanJunction {
	// The SimulationResult, from which this junction originates.
	// A junction will only be created for a plaeceable result that can be realized on a peer,
	// so this should be fixed once it's set.
	SimulationResult flowOriginResult = null;
	// The SimulationResult containing the "rest" of the plan
	// that was cut off the graph from the flowOriginResult.
	// This will change over the course of the simulation, with the junction being updated every time a new placement is considered.
	SimulationResult flowTargetResult = null;
	
	// This is the source of the flowTargetNode in the original Graph
	GraphNode flowOriginNode = null;
	// Those are the (new) sinks of the flowOriginNode in the original Graph.
	// The connection between those and the flowOriginNode was the cutoff-point
	List<GraphNode> flowTargetNodes = null;
	// This is an additional node in the Graph of the flowOriginResult containing a jxtasend-operator,
	// which receives the stream of the flowOriginNode
	// and sends it to the receivingNode of the Graph from the flowTargetResult
	GraphNode sendingNode = null;
	// This is an additional node in the Graph of the flowTargetResult containing a jxtareceive-operator,
	// which receives the stream of the flowOriginResult's sendingNode and streams it
	// to the flowTargetNode.
	GraphNode receivingNode = null;
	// The schema of the subscription between the flowOriginNode and the flowTargetNode,
	// useful for the creation of the jxta-operators and the integration of their respective GraphNodes in the Graphs
	SDFSchema junctionSchema = null;
	
	public PlanJunction(SimulationResult flowOriginResult, GraphNode flowOriginNode, GraphNode receivingNode, GraphNode sendingNode, List<GraphNode> flowTargetNodes, SDFSchema junctionSchema) {
		this.flowOriginResult = flowOriginResult;
		this.flowOriginNode = flowOriginNode;
		this.receivingNode = receivingNode;
		this.flowTargetNodes = flowTargetNodes;
		this.junctionSchema = junctionSchema;
		this.sendingNode = sendingNode;
		// can't set the flowtargetResult yet, because it will vary over the simulation-process on different peers,
	}

	public SimulationResult getFlowOriginResult() {
		return flowOriginResult;
	}

	public void setFlowOriginResult(SimulationResult flowOriginResult) {
		this.flowOriginResult = flowOriginResult;
	}

	public SimulationResult getFlowTargetResult() {
		return flowTargetResult;
	}

	public void setFlowTargetResult(SimulationResult flowTargetResult) {
		this.flowTargetResult = flowTargetResult;
	}

	public GraphNode getFlowOriginNode() {
		return flowOriginNode;
	}

	public void setFlowOriginNode(GraphNode flowOriginNode) {
		this.flowOriginNode = flowOriginNode;
	}

	public List<GraphNode> getFlowTargetNodes() {
		return flowTargetNodes;
	}

	public void setFlowTargetNodes(List<GraphNode> flowTargetNodes) {
		this.flowTargetNodes = flowTargetNodes;
	}

	public GraphNode getSendingNode() {
		return sendingNode;
	}

	public void setSendingNode(GraphNode sendingNode) {
		this.sendingNode = sendingNode;
	}

	public GraphNode getReceivingNode() {
		return receivingNode;
	}

	public void setReceivingNode(GraphNode receivingNode) {
		this.receivingNode = receivingNode;
	}

	public SDFSchema getJunctionSchema() {
		return junctionSchema;
	}

	public void setJunctionSchema(SDFSchema junctionSchema) {
		this.junctionSchema = junctionSchema;
	}
}
