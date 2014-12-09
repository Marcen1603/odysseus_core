package de.uniol.inf.is.odysseus.processmining.inductiveMiner.models;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import com.google.common.collect.Multimap;

public class GraphAndClosurePair {
	
	DirectedWeightedPseudograph<String, DefaultWeightedEdge> graph;
	Multimap<String, String> transitiveClosure;
	
	
	public GraphAndClosurePair(){
		
	}
	public GraphAndClosurePair(DirectedWeightedPseudograph<String, DefaultWeightedEdge> g, Multimap<String, String> tc){
		this.graph = g;
		this.transitiveClosure = tc;
	}

	public DirectedWeightedPseudograph<String, DefaultWeightedEdge> getGraph() {
		return graph;
	}

	public void setGraph(
			DirectedWeightedPseudograph<String, DefaultWeightedEdge> graph) {
		this.graph = graph;
	}

	public Multimap<String, String> getTransitiveClosure() {
		return transitiveClosure;
	}

	public void setTransitiveClosure(Multimap<String, String> transitiveClosure) {
		this.transitiveClosure = transitiveClosure;
	}

	
}
