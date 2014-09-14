package de.uniol.inf.is.odysseus.sports.rest.dto;

import java.util.ArrayList;

public class QueryInfo {
	int queryId;
	String queryName;
	ArrayList<AttributeInformation> attributeList;

	public QueryInfo() {
		
	}
	
	public QueryInfo(int queryId, String queryName, ArrayList<AttributeInformation> attributeList) {
		this.queryId = queryId;
		this.queryName = queryName;
		this.attributeList = attributeList;
	}
	
	
	
	public ArrayList<AttributeInformation> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(ArrayList<AttributeInformation> attributeList) {
		this.attributeList = attributeList;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public int getQueryId() {
		return queryId;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}
	
	
}

