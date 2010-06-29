package de.uniol.inf.is.odysseus.util;

import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.Pair;

public class PrintLogicalGraphVisitor<T extends ILogicalOperator> implements IGraphNodeVisitor<T,String>{

	private ArrayList<Pair<T,T>> visited;
	private String graph;
	
	public PrintLogicalGraphVisitor(){
		this.visited = new ArrayList<Pair<T,T>>();
		this.graph = "";
	}
	
	public void clear(){
		this.visited = new ArrayList<Pair<T, T>>();
		this.graph = "";
	}
	
	@Override
	public void afterFromSinkToSourceAction(T sink, T source) {
		
	}

	@Override
	public void afterFromSourceToSinkAction(T source, T sink) {		
	}

	@Override
	public void beforeFromSinkToSourceAction(T sink, T source) {
		if(!this.visited.contains(new Pair<T,T>(source, sink))){
			graph += sink.toString() + " <-- " + source.toString() + "\n";
		}
		
		this.visited.add(new Pair<T,T>(source,sink));
	}

	@Override
	public void beforeFromSourceToSinkAction(T source, T sink) {
		if(!this.visited.contains(new Pair<T,T>(source, sink))){
			graph += source.toString() + " --> " + sink.toString() + "\n";
		}
		
		this.visited.add(new Pair<T,T>(source,sink));
	}

	@Override
	public String getResult() {
		// TODO Auto-generated method stub
		return this.graph;
	}

	@Override
	public void nodeAction(T node) {
		// TODO Auto-generated method stub
		
	}

}
