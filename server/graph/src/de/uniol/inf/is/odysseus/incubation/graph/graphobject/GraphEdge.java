package de.uniol.inf.is.odysseus.incubation.graph.graphobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphEdge {

	private String label;
	private Map<String, String> props = new HashMap<String, String>();
	private List<GraphNode> startingNodes = new ArrayList<GraphNode>();
	private List<GraphNode> endingNodes = new ArrayList<GraphNode>();
	
	public GraphEdge(String label, List<GraphNode> startingNodes, List<GraphNode> endingNodes) {
		this.label = label;
		this.startingNodes = startingNodes;
		this.endingNodes = endingNodes;
	}
	
	public GraphEdge(String label, List<GraphNode> startingNodes, List<GraphNode> endingNodes, Map<String, String> props) {
		this.label = label;
		this.props = props;
		this.startingNodes = startingNodes;
		this.endingNodes = endingNodes;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public void setProps(Map<String, String> props) {
		this.props = props;
	}
	
	public Map<String, String> getProps() {
		return this.props;
	}
	 
	public void addStartingNode(GraphNode node) {
		this.startingNodes.add(node);
	}
	
	public void addEndingNode(GraphNode node) {
		this.endingNodes.add(node);
	}
	
	public void removeStartingNode(GraphNode node) {
		this.startingNodes.remove(node);
	}
	
	public void removeEndingNode(GraphNode node) {
		this.endingNodes.remove(node);
	}
	
	public List<GraphNode> getStartingNodes() {
		return this.startingNodes;
	}
	
	public List<GraphNode> getEndingNotes() {
		return this.endingNodes;
	}
	
	public void clearStartingNodes() {
		this.startingNodes = new ArrayList<GraphNode>();
	}
	
	public void clearEndingNodes() {
		this.endingNodes = new ArrayList<GraphNode>();
	}

	@Override
	public String toString() {
		return "GraphEdge[" + startingNodes.get(0).getId() + " --" + label + "--> " + endingNodes.get(0).getId() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraphEdge other = (GraphEdge) obj;
		if (endingNodes == null) {
			if (other.endingNodes != null)
				return false;
		} else if (!endingNodes.equals(other.endingNodes))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (props == null) {
			if (other.props != null)
				return false;
		} else if (!props.equals(other.props))
			return false;
		if (startingNodes == null) {
			if (other.startingNodes != null)
				return false;
		} else if (!startingNodes.equals(other.startingNodes))
			return false;
		return true;
	}
	
	

}
