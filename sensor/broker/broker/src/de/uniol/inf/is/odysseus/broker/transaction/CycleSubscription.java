package de.uniol.inf.is.odysseus.broker.transaction;

/**
 * This class represents a cyclic subscription.
 * The outgoing port represents the start of the cyclic subplan and
 * the incoming port represents the end of the cyclic subplan.
 * 
 * @author Dennis Geesen
 */
public class CycleSubscription extends AbstractPortTuple{
	
	/**
	 * Instantiates a new cyclic subscription.
	 *
	 * @param outgoingPort the outgoing port
	 * @param incomingPort the incoming port
	 */
	public CycleSubscription(int outgoingPort, int incomingPort){
		super(2);
		super.setPort(0, outgoingPort);
		super.setPort(1, incomingPort);
	}
	
	/**
	 * Gets the outgoing port.
	 *
	 * @return the outgoing port
	 */
	public int getOutgoingPort() {
		return super.getPort(0);
	}
	
	/**
	 * Gets the incoming port.
	 *
	 * @return the incoming port
	 */
	public int getIncomingPort() {
		return super.getPort(1);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return "Cycle from port "+this.getOutgoingPort()+" to port "+this.getIncomingPort();
	}
	
	
}
