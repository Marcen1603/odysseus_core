package de.uniol.inf.is.odysseus.anomalydetection.rest.dto.request;

public class GetCMConfigurationListRequestDTO {

	private String token;

	public GetCMConfigurationListRequestDTO() {
		
	}
	
	public GetCMConfigurationListRequestDTO(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
