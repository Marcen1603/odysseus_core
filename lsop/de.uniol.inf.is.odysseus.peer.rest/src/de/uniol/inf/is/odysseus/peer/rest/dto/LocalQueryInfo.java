package de.uniol.inf.is.odysseus.peer.rest.dto;

import java.util.List;

public class LocalQueryInfo {
	private int queryId;
	private String name;
	private String queryText;
	private int priority;
	private String parserId;
	private String notice;
	private List<OperatorInfo> operators;
	
	
	public LocalQueryInfo() {
		
	}


	public int getQueryId() {
		return queryId;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}


	public void setNotice(String notice) {
		this.notice = notice;
	}


	public void setParserId(String parserId) {
		this.parserId = parserId;
	}


	public void setPriority(int priority) {
		this.priority = priority;
	}


	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}


	public String getQueryText() {
		return queryText;
	}


	public int getPriority() {
		return priority;
	}


	public String getParserId() {
		return parserId;
	}


	public String getNotice() {
		return notice;
	}


	public void setOperators(List<OperatorInfo> operators) {
		this.operators = operators;
	}


	public List<OperatorInfo> getOperators() {
		return operators;
	}
	
	
}
