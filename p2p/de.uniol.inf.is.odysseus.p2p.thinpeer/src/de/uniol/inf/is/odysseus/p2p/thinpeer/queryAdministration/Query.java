package de.uniol.inf.is.odysseus.p2p.thinpeer.queryAdministration;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.p2p.utils.jxta.Bid;

public class Query{
	
	public enum Status 
	{ 
	  OPEN, STOPBIDDING, CLOSED, CANCELED, DIRECT
	}
	
	protected String id;
	
	protected String query;
	
	protected Status status;
	
	protected int retries;
	
	protected int failed;
	
	
	
	public int getFailed() {
		return failed;
	}

	public void resetFailed() {
		this.failed = 0;
	}
	
	public void incFailed(){
		failed++;
	}

	protected HashMap<String, Bid> biddings = new HashMap<String, Bid>();
	
	public Query(String query, String id){
		this.id = id;
		status = Status.OPEN;
		this.query = query;
		this.retries = 0;
	}

	public void addRetry(){
		this.retries++;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	

	
	
	public HashMap<String, Bid> getBiddings() {
		return biddings;
	}

	public void setBiddings(HashMap<String, Bid> biddings) {
		this.biddings = biddings;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}
	
	
}
