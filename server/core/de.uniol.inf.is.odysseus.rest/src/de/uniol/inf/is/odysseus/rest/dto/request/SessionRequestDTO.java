package de.uniol.inf.is.odysseus.rest.dto.request;


public class SessionRequestDTO extends AbstractRequestDTO{
	private String token;
	
	public SessionRequestDTO() {
		
	}
	
	public SessionRequestDTO(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
