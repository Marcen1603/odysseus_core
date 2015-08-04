package de.uniol.inf.is.odysseus.rest.dto.request;


public abstract class AbstractSessionRequestDTO extends AbstractRequestDTO{
	private String token;
	
	public AbstractSessionRequestDTO() {
		
	}
	
	public AbstractSessionRequestDTO(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
