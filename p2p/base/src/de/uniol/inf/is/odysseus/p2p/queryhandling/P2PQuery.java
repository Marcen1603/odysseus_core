/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.p2p.queryhandling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public abstract class P2PQuery implements Serializable{

	private static final long serialVersionUID = -5425656893540775498L;
	private Lifecycle currStatus;
	private String id;
	private String declarativeQuery;
	private String language;
	private List<ILogicalOperator> logicalOperatorPlan = new ArrayList<ILogicalOperator>();
	protected Map<String,Subplan> subPlans = new HashMap<String, Subplan>();
	private ArrayList<Lifecycle> history = new ArrayList<Lifecycle>();
	private ISession user;
	transient private IDataDictionary dataDictionary;
	private Subplan topSink;

	public P2PQuery(){
	}
	
//	public P2PQuery(String query, String queryID, ISession user) {
//		setDeclarativeQuery(queryID);
//		setId(queryID);
//		this.user = user;
//	}

	public void updateWith(P2PQuery query) {
		this.declarativeQuery = query.getDeclarativeQuery();
		this.language = query.language;
		this.subPlans = new HashMap<String, Subplan>(query.getSubPlans());
		this.topSink = query.topSink;
		this.user = query.user;
	}

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public ISession getUser() {
		return user;
	}
	
	public void setUser(ISession user) {
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

	public boolean addSubPlan(Subplan subplan, boolean isTopSink) {
		if (isTopSink){
			topSink = subplan;
		}
		String id = subplan.getId();
		if(this.subPlans.get(id)==null) {
			this.subPlans.put(id, subplan);
			return true;
		}
		return false;
	}
	
	public Subplan getTopSink() {
		return topSink;
	}
	
	public Lifecycle getStatus() {
		return currStatus;
	}
	
	public void setStatus(Lifecycle status) {

		this.currStatus = status;
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

	
	public List<ILogicalOperator> getLogicalOperatorplan() {
		return this.logicalOperatorPlan;
	}
	
	public void addLogicalOperatorplan (ILogicalOperator plan) {
		this.logicalOperatorPlan.add(plan);
	}

	public IDataDictionary getDataDictionary() {
		return dataDictionary;
	}
	
	public void setDataDictionary(IDataDictionary dataDictionary){
		this.dataDictionary = dataDictionary;
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
		P2PQuery other = (P2PQuery) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}




}
