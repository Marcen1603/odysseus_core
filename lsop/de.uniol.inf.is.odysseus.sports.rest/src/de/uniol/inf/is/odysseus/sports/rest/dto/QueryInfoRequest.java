package de.uniol.inf.is.odysseus.sports.rest.dto;


public class QueryInfoRequest {

	
	private String parser;
	private String query;
	private String queryBuildConfigurationName;
	

	
	public QueryInfoRequest() {

	}
	
	public QueryInfoRequest(String parser, String query,String queryBuildConfigurationName) {
		this.parser = parser;
		this.query = query;
		this.queryBuildConfigurationName = queryBuildConfigurationName;
	}

	public String getParser() {
		return parser;
	}

	public void setParser(String parser) {
		this.parser = parser;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQueryBuildConfigurationName() {
		return queryBuildConfigurationName;
	}

	public void setQueryBuildConfigurationName(String queryBuildConfigurationName) {
		this.queryBuildConfigurationName = queryBuildConfigurationName;
	}
	
	

}
