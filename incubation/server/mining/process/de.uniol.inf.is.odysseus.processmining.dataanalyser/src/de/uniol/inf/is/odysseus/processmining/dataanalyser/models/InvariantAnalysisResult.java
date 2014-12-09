package de.uniol.inf.is.odysseus.processmining.dataanalyser.models;

public class InvariantAnalysisResult {
	boolean almostEqualStartNodes;
	boolean almostEqualEndNodes;
	double nodeCountDistinction;
	double edgeCountDistinction;
	double degreeDistinction;
	public boolean isAlmostEqualStartNodes() {
		return almostEqualStartNodes;
	}
	public void setAlmostEqualStartNodes(boolean almostEqualStartNodes) {
		this.almostEqualStartNodes = almostEqualStartNodes;
	}
	public boolean isAlmostEqualEndNodes() {
		return almostEqualEndNodes;
	}
	public void setAlmostEqualEndNodes(boolean almostEqualEndNodes) {
		this.almostEqualEndNodes = almostEqualEndNodes;
	}
	public double getNodeCountDistinction() {
		return nodeCountDistinction;
	}
	public void setNodeCountDistinction(double nodeCountDistinction) {
		this.nodeCountDistinction = nodeCountDistinction;
	}
	public double getEdgeCountDistinction() {
		return edgeCountDistinction;
	}
	public void setEdgeCountDistinction(double edgeCountDistinction) {
		this.edgeCountDistinction = edgeCountDistinction;
	}
	public double getDegreeDistinction() {
		return degreeDistinction;
	}
	public void setDegreeDistinction(double degreeDistinction) {
		this.degreeDistinction = degreeDistinction;
	}
	
	public void print(){
		StringBuilder sb = new StringBuilder();
		sb.append("Invariantenanalyse: \n");
		sb.append("Startknotenmengen sind ähnlich: "+ almostEqualStartNodes +"\n");
		sb.append("Endknotenmengen sind ähnlich: "+ almostEqualStartNodes +"\n");
		sb.append("Untschied in der Knotenanzahl: "+ nodeCountDistinction + "\n");
		sb.append("Untschied in der Kantenanzahl: "+ edgeCountDistinction + "\n");
		sb.append("Untschied in den Knotengraden: "+ nodeCountDistinction + "\n");
		System.out.println(sb.toString());
	}
	
	
	
	

}
