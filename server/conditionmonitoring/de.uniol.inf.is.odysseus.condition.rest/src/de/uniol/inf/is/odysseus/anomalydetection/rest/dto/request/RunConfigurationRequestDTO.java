package de.uniol.inf.is.odysseus.anomalydetection.rest.dto.request;

import java.util.UUID;

public class RunConfigurationRequestDTO {

	private UUID configurationId;
	private String token;

	public UUID getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(UUID configurationId) {
		this.configurationId = configurationId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
