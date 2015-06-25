package de.uniol.inf.is.odysseus.condition.rest.serverresources;

import java.util.List;

import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

import de.uniol.inf.is.odysseus.condition.rest.datatypes.Configuration;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.ConfigurationDescription;
import de.uniol.inf.is.odysseus.condition.rest.dto.request.GetCMConfigurationListRequestDTO;
import de.uniol.inf.is.odysseus.condition.rest.dto.response.CMConfigurationListResponseDTO;
import de.uniol.inf.is.odysseus.condition.rest.service.ConfigurationService;
import de.uniol.inf.is.odysseus.rest.serverresources.AbstractSessionServerResource;

public class GetConfigurationServerResource extends AbstractSessionServerResource {

	public static final String PATH = "CMGetConfigurationList";

	@Post
	public CMConfigurationListResponseDTO getConfigurationList(GetCMConfigurationListRequestDTO request) {
		// Try to login
		try {
		loginWithToken(request.getToken());
		} catch (ResourceException e) {
			// Login data was wrong
			return null;
		}
		List<Configuration> configs = ConfigurationService.getAvailableConfigurations();
		CMConfigurationListResponseDTO dto = new CMConfigurationListResponseDTO();
		for (Configuration config : configs) {
			ConfigurationDescription description = new ConfigurationDescription(config.getIdentifier(),
					config.getName(), config.getDescription());
			dto.addConfiguration(description);
		}
		return dto;
	}

}