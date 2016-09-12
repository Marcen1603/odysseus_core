package de.uniol.inf.is.odysseus.incubation.graph.graphobject;

import java.util.HashMap;
import java.util.Map;

public class GraphNode {

	private String id;
	private String label;
	private Map<String, Object> props = new HashMap<String, Object>();
	private Map<GraphEdge, GraphNode> outgoingEdges = new HashMap<GraphEdge, GraphNode>();
	private Map<GraphEdge, GraphNode> incomingEdges = new HashMap<GraphEdge, GraphNode>();
	
	public GraphNode(String id, String label) {
		this.id = id;
		this.label = label;
	}
	
	public GraphNode(String id, String label, Map<String, Object> props) {
		this.id = id;
		this.label = label;
		this.props = props;
	}
	
	public GraphNode(String id, String label, Map<String, Object> props, Map<GraphEdge, GraphNode> incomingEdges, Map<GraphEdge, GraphNode> outgoingEdges) {
		this.id = id;
		this.label = label;
		this.props = props;
		this.incomingEdges = incomingEdges;
		this.outgoingEdges = outgoingEdges;
	}
	
	public String  getId() {
		return this.id;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public void setProps(Map<String, Object> props) {
		this.props = props;
	}
	
	public Map<String, Object> getProps() {
		return this.props;
	}
	
	public void clearOutgoingEdges() {
		this.outgoingEdges = new HashMap<GraphEdge, GraphNode>();
	}
	
	public void clearIncomingEdges() {
		this.incomingEdges = new HashMap<GraphEdge, GraphNode>();
	}
	
	public Map<GraphEdge, GraphNode> getIncomingEdges() {
		return this.incomingEdges;
	}
	
	public Map<GraphEdge, GraphNode> getOutgoingEdges() {
		return this.outgoingEdges;
	}
	
	public void setOutgoingEdges(Map<GraphEdge, GraphNode> newEdges) {
		this.outgoingEdges = newEdges;
	}
	
	public void setIncomingEdges(Map<GraphEdge, GraphNode> newEdges) {
		this.incomingEdges = newEdges;
	}
	
	public void addOutgoingEdge(GraphEdge edge, GraphNode other) {
		this.outgoingEdges.put(edge, other);
	}
	
	public void addIncomingEdge(GraphEdge edge, GraphNode other) {
		this.incomingEdges.put(edge, other);
	}
	
	public void clearEdges() {
		this.incomingEdges = new HashMap<GraphEdge, GraphNode>();
		this.outgoingEdges = new HashMap<GraphEdge, GraphNode>();
	}

}
