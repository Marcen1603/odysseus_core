package de.uniol.inf.is.odysseus.broker.transaction;

public class CycleSubscription {
	private int outgoingPort;
	private int incomingPort;
	
	public CycleSubscription(int outgoingPort, int incomingPort){
		this.outgoingPort = outgoingPort;
		this.incomingPort = incomingPort;
	}
	
	public int getOutgoingPort() {
		return outgoingPort;
	}
	public void setOutgoingPort(int outgoingPort) {
		this.outgoingPort = outgoingPort;
	}
	public int getIncomingPort() {
		return incomingPort;
	}
	public void setIncomingPort(int incomingPort) {
		this.incomingPort = incomingPort;
	}
	
	@Override
	public String toString(){
		return "Cycle from port "+this.outgoingPort+" to port "+this.incomingPort;
	}
	
	
}
