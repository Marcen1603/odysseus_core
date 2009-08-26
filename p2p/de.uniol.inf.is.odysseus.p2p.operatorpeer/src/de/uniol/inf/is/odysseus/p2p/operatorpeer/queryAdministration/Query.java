package de.uniol.inf.is.odysseus.p2p.operatorpeer.queryAdministration;

import java.io.Serializable;
import java.util.HashMap;

import net.jxta.protocol.PipeAdvertisement;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;

public class Query implements Serializable {
	
	private static final long serialVersionUID = 1L;

	//Granted = Erlaubnis für die Durchführung bekommen
	//Denied = Absage für die Durchführung bekommen
	//Open = Ausschreibung wurde gefunden
	//Translated = Anfrage ist in physischen Plan überführt
	//Failed = Durchführung gescheitert
	//Run = Anfrage wird ausgeführt
	public enum Status 
	{ 
	  GRANTED, DENIED, OPEN, TRANSLATED, FAILED, RUN, HOTQUERY
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String id;
	
	private String language;

	private Status status;
	
	private PipeAdvertisement adminPeerPipeAdvertisement;
	

	public PipeAdvertisement getAdminPeerPipeAdvertisement() {
		return adminPeerPipeAdvertisement;
	}

	public void setAdminPeerPipeAdvertisement(
			PipeAdvertisement adminPeerPipeAdvertisement) {
		this.adminPeerPipeAdvertisement = adminPeerPipeAdvertisement;
	}

	private HashMap<String, AbstractLogicalOperator> subPlans = new HashMap<String, AbstractLogicalOperator>();

	public HashMap<String, AbstractLogicalOperator> getSubPlans() {
		return subPlans;
	}

	public void setSubPlans(HashMap<String, AbstractLogicalOperator> subPlans) {
		this.subPlans = subPlans;
	}

	public Query(){
		this.status = Status.OPEN;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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
