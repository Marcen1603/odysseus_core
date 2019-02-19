package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.List;

public class QueryAddedEvent {

	private String query;
	private List<Integer> queryIds;
	private String parserID;

	public QueryAddedEvent(String query, List<Integer> queryIds, String parserID) {
		this.query = query;
		this.queryIds = queryIds;
		this.parserID = parserID;
	}

}
