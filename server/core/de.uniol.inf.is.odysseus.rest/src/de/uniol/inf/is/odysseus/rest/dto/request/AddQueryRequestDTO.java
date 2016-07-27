package de.uniol.inf.is.odysseus.rest.dto.request;




public class AddQueryRequestDTO extends SessionRequestDTO {


	private String query;
	
	private String parser;
	
	private String transformationConfig;
	
	public AddQueryRequestDTO() {

	}
	
	public AddQueryRequestDTO(String token, String query,String parser, String transformationConfig) {
		super(token);
		this.query = query;
		this.parser = parser;
		this.transformationConfig = transformationConfig;
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
	public String getTransformationConfig() {
		return transformationConfig;
	}
	public void setTransformationConfig(String transformationConfig) {
		this.transformationConfig = transformationConfig;
	}

	
	
	
	
}
