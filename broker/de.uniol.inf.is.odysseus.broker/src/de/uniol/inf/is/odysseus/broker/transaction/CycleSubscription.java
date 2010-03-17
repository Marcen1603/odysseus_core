package de.uniol.inf.is.odysseus.broker.transaction;

public class CycleSubscription extends AbstractPortTuple{
	
	
	public CycleSubscription(int outgoingPort, int incomingPort){
		super(2);
		super.setPort(0, outgoingPort);
		super.setPort(1, incomingPort);
	}
	
	public int getOutgoingPort() {
		return super.getPort(0);
	}
	
	public int getIncomingPort() {
		return super.getPort(1);
	}
	
	@Override
	public String toString(){
		return "Cycle from port "+this.getOutgoingPort()+" to port "+this.getIncomingPort();
	}
	
	
}
