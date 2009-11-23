package de.uniol.inf.is.odysseus.p2p.queryhandling;

import java.util.Date;

public class Bid {
	private Date date;
	private String peerId;
	private String subplanId;

	public String getSubplanId() {
		return subplanId;
	}

	public void setSubplanId(String subplanId) {
		this.subplanId = subplanId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	


	public String getPeerId() {
		return peerId;
	}

	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}
	
	

}
