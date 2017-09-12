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

	public QueryInformation(String name, String parser, String state) {
		super();
		this.name = name;
		this.parser = parser;
		this.state = state;
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
}
