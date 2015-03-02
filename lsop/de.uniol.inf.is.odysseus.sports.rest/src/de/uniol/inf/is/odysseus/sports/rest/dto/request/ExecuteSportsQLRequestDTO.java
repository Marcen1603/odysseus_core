package de.uniol.inf.is.odysseus.sports.rest.dto.request;

import de.uniol.inf.is.odysseus.rest.dto.request.AbstractRequestDTO;




public class ExecuteSportsQLRequestDTO extends AbstractRequestDTO {

	private String sportsQL;
	private String token;
	private String transformationConfig;
	private String username;
	private String password;
	private String distributor;
	private int timeToWait;
	private boolean addQuery;
	private boolean startQuery;

	public ExecuteSportsQLRequestDTO() {

	}
		
	public ExecuteSportsQLRequestDTO(String sportsQL, String transformationConfig, String username, String password, String token, String distributor, int timeToWait, boolean addQuery, boolean startQuery) {
		this.sportsQL = sportsQL;
		this.token = token;
		this.transformationConfig = transformationConfig;
		this.username = username;
		this.password = password;
		this.distributor = distributor;
		this.timeToWait = timeToWait;
		this.addQuery = addQuery;
		this.startQuery = startQuery;


	}

	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSportsQL() {
		return sportsQL;
	}

	public void setSportsQL(String sportsQL) {
		this.sportsQL = sportsQL;
	}
	
	public String getTransformationConfig() {
		return transformationConfig;
	}

	public void setTransformationConfig(String transformationConfig) {
		this.transformationConfig = transformationConfig;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDistributor() {
		return this.distributor;
	}

	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}

	public int getTimeToWait() {
		return timeToWait;
	}

	public void setTimeToWait(int timeToWait) {
		this.timeToWait = timeToWait;
	}

	public boolean isAddQuery() {
		return addQuery;
	}

	public void setAddQuery(boolean addQuery) {
		this.addQuery = addQuery;
	}

	public boolean isStartQuery() {
		return startQuery;
	}

	public void setStartQuery(boolean startQuery) {
		this.startQuery = startQuery;
	}
	
	
	
	
	
	
	
	
	
}
