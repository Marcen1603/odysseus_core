package de.uniol.inf.is.odysseus.p2p.queryhandling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.usermanagement.User;

public abstract class Query implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5425656893540775498L;

	private Lifecycle status;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String id;

	private String declarativeQuery;
	
	private String language;
	
	private ILogicalOperator logicalOperatorPlan;
	
	protected Map<String,Subplan> subPlans = new HashMap<String, Subplan>();
	
	private ArrayList<Lifecycle> history = new ArrayList<Lifecycle>();

	private User user;

	transient private IDataDictionary dataDictionary;
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public ArrayList<Lifecycle> getHistory() {
		return history;
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

	public boolean addSubPlan(String id, Subplan subplan) {
		if(this.subPlans.get(id)==null) {
			this.subPlans.put(id, subplan);
			return true;
		}
		return false;
	}
	

	public Query(){
	}

	public Lifecycle getStatus() {
		return status;
	}

	public void setStatus(Lifecycle status) {
		this.status = status;
		getHistory().add(status);
	}
	
	public String getDeclarativeQuery() {
		return declarativeQuery;
	}

	public void setDeclarativeQuery(String query) {
		this.declarativeQuery = query;
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
	
	public Query(String query, String queryID, User user) {
		setDeclarativeQuery(queryID);
		setId(queryID);
		this.user = user;
	}
	
	public ILogicalOperator getLogicalOperatorplan() {
		return this.logicalOperatorPlan;
	}
	
	public void setLogicalOperatorplan (ILogicalOperator plan) {
		if(this.declarativeQuery!=null) {
			this.logicalOperatorPlan = plan;
		}
	}
//	public void setSubplans(ArrayList<ILogicalOperator> list) {
//		int planCounter = 1;
//		for(ILogicalOperator op : list) {
//			addSubPlan(""+planCounter, new Subplan(""+planCounter, op));
//			planCounter++;
//		}
//		
//	}

	public IDataDictionary getDataDictionary() {
		return dataDictionary;
	}
	
	public void setDataDictionary(IDataDictionary dataDictionary){
		this.dataDictionary = dataDictionary;
	}
}
