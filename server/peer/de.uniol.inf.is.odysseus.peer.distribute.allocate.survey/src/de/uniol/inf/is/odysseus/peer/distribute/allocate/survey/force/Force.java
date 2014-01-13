package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.force;

import com.google.common.base.Preconditions;

final class Force {

	private final ForceNode nodeA;
	private final ForceNode nodeB;
	private final double dataRate;
	
	public Force( ForceNode nodeA, ForceNode nodeB, double dataRate) {
		Preconditions.checkNotNull(nodeA, "First node must not be null!");
		Preconditions.checkNotNull(nodeB, "Second node must not be null!");
		Preconditions.checkArgument(dataRate >= 0, "The data rate for the force must be zero or positive!");
		
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.dataRate = dataRate;
		
		this.nodeA.attachForce(this);
		this.nodeB.attachForce(this);
	}
	
	public ForceNode getNodeA() {
		return nodeA;
	}
	
	public ForceNode getNodeB() {
		return nodeB;
	}
	
	public double getDataRate() {
		return dataRate;
	}
}
