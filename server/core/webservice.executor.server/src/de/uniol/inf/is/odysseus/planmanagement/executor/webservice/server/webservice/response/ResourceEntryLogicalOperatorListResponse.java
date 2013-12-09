package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class ResourceEntryLogicalOperatorListResponse extends Response {

	private List<Entry<Resource, ILogicalOperator>> responseValue = new ArrayList<Entry<Resource, ILogicalOperator>>();

	public ResourceEntryLogicalOperatorListResponse() {
		super();
	}

	public ResourceEntryLogicalOperatorListResponse(boolean success) {
		super(success);		
	}
	
	public ResourceEntryLogicalOperatorListResponse(List<Entry<Resource, ILogicalOperator>> response, boolean success){
		super(success);
		this.responseValue = response;
	}
	
	@SuppressWarnings("unchecked")
	public ResourceEntryLogicalOperatorListResponse(Collection<Entry<Resource, ILogicalOperator>> response, boolean success){
		super(success);
		this.setResponseValue(response.toArray(new Entry[0]));
	}

	public String[] getResponseValue() {
		return responseValue.toArray(new String[0]);
	}

	public void setResponseValue(Entry<Resource, ILogicalOperator>[] responseValue) {
		this.responseValue.clear();
		for(Entry<Resource, ILogicalOperator> s : responseValue){
			this.responseValue.add(s);
		}
	}
	
	public void addResponseValue(Entry<Resource, ILogicalOperator> s){
		this.responseValue.add(s);
	}
	
	public void removeResponseValue(Entry<Resource, ILogicalOperator> s){
		this.responseValue.remove(s);
	}
	
}
