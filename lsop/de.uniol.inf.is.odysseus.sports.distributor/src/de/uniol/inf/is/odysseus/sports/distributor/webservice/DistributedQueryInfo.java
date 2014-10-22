package de.uniol.inf.is.odysseus.sports.distributor.webservice;


/**
 * This class contains information to a distributed query (sharedQueryId, IP and port of peer with top operator)
 * @author Thore Stratmann
 *
 */
public class DistributedQueryInfo {
	private String sharedQueryId;
	private String topOperatorPeerIP;
	private int topOperatorPeerRestPort;
	
	public DistributedQueryInfo() {
		
	}

	public String getSharedQueryId() {
		return sharedQueryId;
	}

	public void setSharedQueryId(String sharedQueryId) {
		this.sharedQueryId = sharedQueryId;
	}

	public String getTopOperatorPeerIP() {
		return topOperatorPeerIP;
	}

	public void setTopOperatorPeerIP(String topOperatorPeerIP) {
		this.topOperatorPeerIP = topOperatorPeerIP;
	}

	
	public int getTopOperatorPeerRestPort() {
		return topOperatorPeerRestPort;
	}

	public void setTopOperatorPeerRestPort(int topOperatorPeerRestPort) {
		this.topOperatorPeerRestPort = topOperatorPeerRestPort;
	}

	
	
	
	

}
