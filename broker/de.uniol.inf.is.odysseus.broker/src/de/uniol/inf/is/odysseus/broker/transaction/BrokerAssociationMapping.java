package de.uniol.inf.is.odysseus.broker.transaction;


/**
 * This class represents a mapping of an association
 * selection operator to a port of the broker. An
 * assocation selection operator always reads a broker.
 * 
 * @author Andre Bolles
 *
 */
public class BrokerAssociationMapping {

	private String associationName;
	private String brokerName;
	private int outPort;
	
	public BrokerAssociationMapping(){
	}
	
	public BrokerAssociationMapping(String associationName, String brokerName, int outPort){
		this.associationName = associationName;
		this.brokerName = brokerName;
		this.outPort = outPort;
	}

	public String getAssociationName() {
		return associationName;
	}

	public void setAssociationName(String associationName) {
		this.associationName = associationName;
	}

	public String getBrokerName() {
		return brokerName;
	}

	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}

	public int getOutPort() {
		return outPort;
	}

	public void setOutPort(int outPort) {
		this.outPort = outPort;
	}
	
}
