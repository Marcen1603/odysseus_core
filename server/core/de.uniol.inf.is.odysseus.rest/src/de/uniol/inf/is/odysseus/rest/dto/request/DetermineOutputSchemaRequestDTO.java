package de.uniol.inf.is.odysseus.rest.dto.request;




public class DetermineOutputSchemaRequestDTO extends SessionRequestDTO {


	private String query;
	
	private String parser;
	
	private String port;
	
	public DetermineOutputSchemaRequestDTO() {

	}
	
	public DetermineOutputSchemaRequestDTO(String token, String query, String parser, String port) {
		super(token);
		this.query = query;
		this.parser = parser;
		this.port = port;
	}
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getParser() {
		return parser;
	}
	public void setParser(String parser) {
		this.parser = parser;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}	
	
}
