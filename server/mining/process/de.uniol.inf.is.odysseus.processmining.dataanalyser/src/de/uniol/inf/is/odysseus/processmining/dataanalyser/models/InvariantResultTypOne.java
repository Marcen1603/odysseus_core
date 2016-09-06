package de.uniol.inf.is.odysseus.processmining.dataanalyser.models;

public class InvariantResultTypOne implements IInvariantResult{
	boolean almostEqualStartNodes;
	boolean almostEqualEndNodes;
	double nodeCountDistinction;
	double edgeCountDistinction;
	double degreeDistinction;
	int mostRecentNodesCount;
	int currentNodesCount;
	int mostRecentEdgeCount;
	int currentEdgeCount;
	
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
	
	public int getMostRecentNodesCount() {
		return mostRecentNodesCount;
	}
	public void setMostRecentNodesCount(int mostRecentNodesCount) {
		this.mostRecentNodesCount = mostRecentNodesCount;
	}
	public int getCurrentNodesCount() {
		return currentNodesCount;
	}
	public void setCurrentNodesCount(int currentNodesCount) {
		this.currentNodesCount = currentNodesCount;
	}
	public int getMostRecentEdgeCount() {
		return mostRecentEdgeCount;
	}
	public void setMostRecentEdgeCount(int mostRecentEdgeCount) {
		this.mostRecentEdgeCount = mostRecentEdgeCount;
	}
	public int getCurrentEdgeCount() {
		return currentEdgeCount;
	}
	public void setCurrentEdgeCount(int currentEdgeCount) {
		this.currentEdgeCount = currentEdgeCount;
	}

	
	
	public void print(){
		StringBuilder sb = new StringBuilder();
		sb.append("Invariantenanalyse: \n");
		sb.append("Startknotenmengen sind ähnlich: "+ almostEqualStartNodes +"\n");
		sb.append("Endknotenmengen sind ähnlich: "+ almostEqualStartNodes +"\n");
		sb.append("Untschied in der Knotenanzahl: "+ nodeCountDistinction + "\n");
		sb.append("Untschied in der Kantenanzahl: "+ edgeCountDistinction + "\n");
		sb.append("Untschied in den Knotengraden: "+ nodeCountDistinction + "\n");
		sb.append("MRecent Knotenanzahl: "+mostRecentNodesCount + "\n");
		sb.append("MRecent Kantenanzahl: "+mostRecentEdgeCount + "\n");
		sb.append("Current Knotenanzahl: "+currentNodesCount + "\n");
		sb.append("Current Kantenanzahl: "+currentEdgeCount + "\n");
		System.out.println(sb.toString());
	}
	@Override
	public InvariantResultStrategyEnum getStrategy() {
		if(!almostEqualEndNodes || !almostEqualStartNodes){
			return InvariantResultStrategyEnum.REBUILD;
		} 
		if(nodeCountDistinction < 1.0 || edgeCountDistinction < 1.0 || degreeDistinction < 1.0 ||
				mostRecentNodesCount != currentNodesCount || mostRecentEdgeCount != currentEdgeCount){
			return InvariantResultStrategyEnum.UPDATE;
		}
		return InvariantResultStrategyEnum.DELAY;
	}
	
	
	
	

}
