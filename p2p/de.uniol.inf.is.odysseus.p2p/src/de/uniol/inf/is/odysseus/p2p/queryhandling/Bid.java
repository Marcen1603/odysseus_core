package de.uniol.inf.is.odysseus.p2p.queryhandling;

import java.io.Serializable;
import java.util.Date;

public abstract class Bid implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6960627612625664351L;
	private Date date;
	private String peerId;
	private String subplanId;
	private String queryId;
	private String bid;
	
	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

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

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getQueryId() {
		return queryId;
	}
	
	

}
