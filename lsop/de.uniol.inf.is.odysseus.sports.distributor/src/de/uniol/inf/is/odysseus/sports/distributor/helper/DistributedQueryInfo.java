package de.uniol.inf.is.odysseus.sports.distributor.helper;

public class DistributedQueryInfo {
	private String sharedQueryId;
	private String topOperatorIP;
	private boolean queryDistributed;
	private int topOperatorPeerWebservicePort;
	
	public DistributedQueryInfo() {
		
	}

	public String getSharedQueryId() {
		return sharedQueryId;
	}

	public void setSharedQueryId(String sharedQueryId) {
		this.sharedQueryId = sharedQueryId;
	}

	public String getTopOperatorIP() {
		return topOperatorIP;
	}

	public void setTopOperatorIP(String topOperatorIP) {
		this.topOperatorIP = topOperatorIP;
	}

	public boolean isQueryDistributed() {
		return queryDistributed;
	}

	public void setQueryDistributed(boolean queryDistributed) {
		this.queryDistributed = queryDistributed;
	}

	public int getTopOperatorPeerWebservicePort() {
		return topOperatorPeerWebservicePort;
	}

	public void setTopOperatorPeerWebservicePort(int topOperatorPeerWebservicePort) {
		this.topOperatorPeerWebservicePort = topOperatorPeerWebservicePort;
	}

	
	
	
	

}
