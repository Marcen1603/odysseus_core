package de.uniol.inf.is.odysseus.condition.rest.dto.request;

public class RunConfigurationRequestDTO {

	int configurationId;
	String token;

	public int getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(int configurationId) {
		this.configurationId = configurationId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
