package de.uniol.inf.is.odysseus.p2p.administrationpeer.queryAdministration;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;

public abstract class Query {
	
	//Denied = ThinPeer hat die Anfrage abgelehnt
	//Bidding = OperatorPeers können sich auf die Anfrage bewerben
	//NoBidding = Keine Bewerbungen werden mehr angenommen
	//Failed = Anfragedurchführung ist gescheitert
	//Run = Die Anfrage wird zur Zeit ausgeführt
	//Open = Die Anfrageausschreibung zu dieser Anfrage wurde im Netzwerk gefunden
	//Granted = Zusage
	public enum Status 
	{ 
	  DENIED, BIDDING, NOBIDDING, FAILED, RUN, OPEN, APPLY, GRANTED
	}
	
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
	
	protected ArrayList<Bid> biddings = new ArrayList<Bid>();
	
	private Status status;
	
	protected ArrayList<Subplan> subPlans = new ArrayList<Subplan>();
	

	public int getRetries() {
		return retries;
	}
	
	public void addRetry(){
		retries++;
	}

	public ArrayList<Bid> getBiddings() {
		return biddings;
	}

	public void setBiddings(ArrayList<Bid> biddings) {
		this.biddings = biddings;
	}

	public ArrayList<Subplan> getSubPlans() {
		return subPlans;
	}

	public void addSubPlan(String id, AbstractLogicalOperator ao) {
		this.subPlans.add(new Subplan(id,ao));
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
}
