package de.uniol.inf.is.odysseus.rest.dto.response;

/**
 * Object for the rest interface to send query information for a specific, given query id.
 * @author Tobias Brandt
 *
 */
public class QueryInformation {
	
	private String name;
	private String parser;
	private String state;
	private String queryText;

	public QueryInformation(String name, String parser, String state, String queryText) {
		super();
		this.name = name;
		this.parser = parser;
		this.state = state;
		this.queryText = queryText;
	}

	public String getName() {
		return name;
	}

	public String getParser() {
		return parser;
	}
	
	public String getState() {
		return state;
	}
	
	public String getQueryText() {
		return queryText;
	}
}
