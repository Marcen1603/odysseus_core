package de.uniol.inf.is.odysseus.anomalydetection.rest.dto.response;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes.ConfigurationDescription;

public class CMConfigurationListResponseDTO {
	
	private List<ConfigurationDescription> configurations;

	public CMConfigurationListResponseDTO() {
		this.configurations = new ArrayList<ConfigurationDescription>();
	}
	
	public List<ConfigurationDescription> getConfigurations() {
		return configurations;
	}
	
	public void addConfiguration(ConfigurationDescription configuration) {
		this.configurations.add(configuration);
	}

}
