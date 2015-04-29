package de.uniol.inf.is.odysseus.condition.rest.dto.request;

import de.uniol.inf.is.odysseus.rest.dto.request.AbstractRequestDTO;

public class ExecuteConditionQLRequestDTO extends AbstractRequestDTO {

	private String username;
	private String password;
	private String conditionQL;
	private String token;
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getConditionQL() {
		return conditionQL;
	}
	
	public String getToken() {
		return token;
	}

}
