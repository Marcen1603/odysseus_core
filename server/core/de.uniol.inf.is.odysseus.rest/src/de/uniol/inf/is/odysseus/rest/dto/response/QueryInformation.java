package de.uniol.inf.is.odysseus.rest.dto.response;

/**
 * Object for the rest interface to send query information for a specific, given query id.
 * @author Tobias Brandt
 *
 */
public class QueryInformation {
	
	private String name;
	private String parser;

	public QueryInformation(String name, String parser) {
		super();
		this.name = name;
		this.parser = parser;
	}

	public String getName() {
		return name;
	}

	public String getParser() {
		return parser;
	}
}
