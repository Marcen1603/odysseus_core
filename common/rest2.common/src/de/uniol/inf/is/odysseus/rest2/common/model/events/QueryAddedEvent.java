package de.uniol.inf.is.odysseus.rest2.common.model.events;

import java.util.List;

public class QueryAddedEvent {

	public String query;
	public List<Integer> queryIds;
	public String parserID;

	public QueryAddedEvent(String query, List<Integer> queryIds, String parserID) {
		this.query = query;
		this.queryIds = queryIds;
		this.parserID = parserID;
	}

}
