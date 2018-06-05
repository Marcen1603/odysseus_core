package de.uniol.inf.is.odysseus.processmining.inductiveMiner.models;

import java.util.LinkedList;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.Partition;

public class Cut{
	
	private LinkedList<Partition> cutPartitions;
	private OperatorType operator;
	boolean isLeaf = false;
	
	public Cut(LinkedList<Partition> partitions,Partition cutAssociatedPartition,OperatorType type){
		this.operator = type;
		this.cutPartitions = partitions;
		this.isLeaf = false;
		
	}
	
	public Set<String>getAllNodes(){
		Set<String> allNodesOfCutPartitions = Sets.newHashSet();
		for(Partition p : cutPartitions){
			allNodesOfCutPartitions.addAll(p.getGraph().vertexSet());
		}
		
		return allNodesOfCutPartitions;
	}
	private Cut(){
		//Empty cut
		this.operator = OperatorType.SILENT_ENDING;
		this.cutPartitions = Lists.newLinkedList();
	}
	public OperatorType getOperator() {
		return operator;
	}

	public void setOperator(OperatorType operator) {
		this.operator = operator;
	}

	public LinkedList<Partition> getCutPartitions() {
		return cutPartitions;
	}

	public void setCutPartitions(LinkedList<Partition> cutPartitions) {
		this.cutPartitions = cutPartitions;
	}

	public boolean isLeaf() {
		return isLeaf;
	}
	
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	

	public static Cut createLeaf(Partition p){
		LinkedList<Partition> leafList = Lists.newLinkedList();
		leafList.add(p);
		Cut leaf = new Cut(leafList,p, OperatorType.SILENT);
		leaf.setLeaf(true);
		return leaf;
	}
	
	public static Cut getEmptyCut(){
		Cut done = new Cut();
		return done;
	}

	public void print(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.operator+": ");
		for(Partition p : cutPartitions){
			sb.append("   pGraph: "+p.getGraph()+"edges: "+p.getGraph().edgeSet());
		}
		sb.append("cutEnd;");
		System.out.println(sb.toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cutPartitions == null) ? 0 : cutPartitions.hashCode());
		result = prime * result
				+ ((operator == null) ? 0 : operator.hashCode());
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
		Cut other = (Cut) obj;
		if (cutPartitions == null) {
			if (other.cutPartitions != null)
				return false;
		} else if (!cutPartitions.equals(other.cutPartitions))
			return false;
		if (operator != other.operator)
			return false;
		return true;
	}
	
	@Override public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(operator + " ");
		for(Partition p :cutPartitions){
			sb.append(p.getGraph().vertexSet()+",");	
		}
		return sb.toString();
	}
		
}
