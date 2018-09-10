package de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils;

import java.util.HashMap;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

public class Partition {
	private DirectedWeightedPseudograph<String, DefaultWeightedEdge> graph;
	private HashMap<String, Integer> startNodes;
	private HashMap<String, Integer> endNodes;
	private Set<String> shortLoops;

	public Partition(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> graph,
			HashMap<String, Integer> startNodes,
			HashMap<String, Integer> endNodes, Set<String> shortLoops) {
		this.graph = graph;
		this.startNodes = startNodes;
		this.endNodes = endNodes;
		this.shortLoops = shortLoops;
	}

	public DirectedWeightedPseudograph<String, DefaultWeightedEdge> getGraph() {
		return graph;
	}

	public void setGraph(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> graph) {
		this.graph = graph;
	}

	public HashMap<String, Integer> getStartNodes() {
		return startNodes;
	}

	public void setStartNodes(HashMap<String, Integer> startNodes) {
		this.startNodes = startNodes;
	}

	public HashMap<String, Integer> getEndNodes() {
		return endNodes;
	}

	public void setEndNodes(HashMap<String, Integer> endNodes) {
		this.endNodes = endNodes;
	}

	public Set<String> getShortLoops() {
		return shortLoops;
	}

	public void setShortLoops(Set<String> shortLoops) {
		this.shortLoops = shortLoops;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((endNodes == null) ? 0 : endNodes.hashCode());
		result = prime * result + ((graph == null) ? 0 : graph.vertexSet().hashCode());
		result = prime * result
				+ ((shortLoops == null) ? 0 : shortLoops.hashCode());
		result = prime * result
				+ ((startNodes == null) ? 0 : startNodes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Partition other = (Partition) obj;

		if (graph == null) {
			if (other.graph != null)
				return false;
		} else if (!(graph.vertexSet()).equals(other.graph.vertexSet()))
			return false;

		return true;
	}
	
	@Override public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(graph.vertexSet());
		return sb.toString();
	}
		



	

}
