package de.uniol.inf.is.odysseus.p2p;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;

public abstract class Query {
	
	
	//Denied = ThinPeer hat die Anfrage abgelehnt
	//Bidding = OperatorPeers können sich auf die Anfrage bewerben
	//NoBidding = Keine Bewerbungen werden mehr angenommen
	//Failed = Anfragedurchführung ist gescheitert
	//Run = Die Anfrage wird zur Zeit ausgeführt
	//Open = Die Anfrageausschreibung zu dieser Anfrage wurde im Netzwerk gefunden
	//Granted = Zusage
	//Direct = kommt vom Thin-Peer, um zu signalisieren, dass er einem Admin-Peer eine Anfrage direkt zugewiese hat.
	//Canceled = Abbruch durch Thin-Peer
	public enum Status 
	{ 
	  DENIED, BIDDING, NOBIDDING, FAILED, RUN, OPEN, APPLY, GRANTED, DIRECT, CANCELED, HOTQUERY
	}
	
	private Status status;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private int retries=0;
	
	private String id;

	private String query;
	
	private String language;
	
	protected Map<String,Subplan> subPlans = new HashMap<String, Subplan>();

	public int getRetries() {
		return retries;
	}
	
	public void addRetry(){
		retries++;
	}


	public Map<String,Subplan> getSubPlans() {
		return subPlans;
	}
	
	/**
	 * Prüft, ob alle Subpläne mindestens ein Gebot besitzen
	 * @return
	 */
	public boolean containsAllBidding() {
		
		for(Subplan s : getSubPlans().values()) {
			if(s.getBiddings().isEmpty())
				return false;
		}
		return true;
		
	}

	public boolean addSubPlan(String id, AbstractLogicalOperator ao) {
		if(this.subPlans.get(id)==null) {
			this.subPlans.put(id, new Subplan(id, ao));
			return true;
		}
		return false;
	}
	
	public abstract void setSubplans(ArrayList<AbstractLogicalOperator> list);

	public Query(){
		this.status = Status.OPEN;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Query other = (Query) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	/**
	 * Relikt aus der alten Implementierung, damit der Thin-Peer mit Query arbeiten kann
	 * 
	 * @param query
	 * @param queryID
	 */
	public Query(String query, String queryID) {

		setQuery(queryID);
		setId(queryID);
	}
}
