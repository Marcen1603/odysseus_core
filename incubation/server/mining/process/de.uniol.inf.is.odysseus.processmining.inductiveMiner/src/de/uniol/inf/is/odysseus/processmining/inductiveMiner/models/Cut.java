package de.uniol.inf.is.odysseus.processmining.inductiveMiner.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.processmining.inductiveMiner.utils.Partition;

public class Cut{
	
	private LinkedList<Partition> cutPartitions;
	private ArrayList<Partition> cutArrayListPartitions;
	private OperatorType operator;
	boolean isLeaf = false;
	
	public Cut(LinkedList<Partition> partitions,OperatorType type){
		this.operator = type;
		this.cutPartitions = partitions;
		this.cutArrayListPartitions = Lists.newArrayList(partitions);
		this.isLeaf = false;
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
		Cut leaf = new Cut(leafList, OperatorType.SILENT);
		leaf.setLeaf(true);
		return leaf;
	}

	public Set<String> getSetofCutPartitionNodes(){
		Set<String> partitionNodes = Sets.newHashSet();
		for(Partition p : cutArrayListPartitions){
			partitionNodes.addAll(p.getGraph().vertexSet());
		}
		return partitionNodes;
	}

	public String print(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.operator+": ");
		for(Partition p : cutPartitions){
			sb.append("   pGraph: "+p.getGraph()+"edges: "+p.getGraph().edgeSet());
		}
		sb.append("cutEnd;\n");
		return sb.toString();
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
	
	
	
}
