package de.uniol.inf.is.odysseus.broker.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;

public class TransactionTS implements Comparable<TransactionTS> {

	private PointInTime time;
	private int outgoingPort;
	
	public TransactionTS(int outgoingPort, PointInTime time){
		this.time = time;
		this.outgoingPort = outgoingPort;
	}
	
	@Override
	public int compareTo(TransactionTS other) {		
		return this.getPointInTime().compareTo(other.getPointInTime());
	}
	
	public PointInTime getPointInTime(){
		return this.time;
	}
	
	public int getOutgoingPort(){
		return this.outgoingPort;		
	}

}
