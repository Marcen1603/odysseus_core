package de.uniol.inf.is.odysseus.mining.model;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;

public class KnowledgeDiscoveryProcess {

	private List<IPhase> phases = new ArrayList<IPhase>();
	private String name;
	
	public KnowledgeDiscoveryProcess(String name) {
		this.name = name;
	}


	public List<ILogicalOperator> buildLogicalPlan(){
		List<ILogicalOperator> topOps = new ArrayList<ILogicalOperator>();
		for(IPhase phase : this.phases){
			topOps.addAll(phase.buildLogicalPlan());
		}		
		return topOps;
	}


	public void setPhases(List<IPhase> phases) {
		this.phases = phases;		
	}
	
	public String getName(){
		return this.name;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Knowledge Discovery Process with name \""+this.name+"\"\n");
		for(IPhase p : this.phases){
			sb.append("  "+p.toString()+"\n");
		}
		return sb.toString();
	}
		
}
