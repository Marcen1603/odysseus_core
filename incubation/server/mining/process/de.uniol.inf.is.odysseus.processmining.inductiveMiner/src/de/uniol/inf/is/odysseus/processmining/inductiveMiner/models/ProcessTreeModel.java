package de.uniol.inf.is.odysseus.processmining.inductiveMiner.models;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class ProcessTreeModel {

	SimpleGraph<Cut, DefaultEdge> processTree;
	
	
	public ProcessTreeModel() {
		processTree = new SimpleGraph<Cut, DefaultEdge>(DefaultEdge.class);
	}
	
	
	public void addCut(Cut cut){
	}
}
