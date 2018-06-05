package de.uniol.inf.is.odysseus.condition.rest.serverresources;

import java.util.List;

import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

import de.uniol.inf.is.odysseus.condition.rest.datatypes.Configuration;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.ConfigurationDescription;
import de.uniol.inf.is.odysseus.condition.rest.dto.request.GetCMConfigurationListRequestDTO;
import de.uniol.inf.is.odysseus.condition.rest.dto.response.CMConfigurationListResponseDTO;
import de.uniol.inf.is.odysseus.condition.rest.service.ConfigurationService;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.serverresources.AbstractSessionServerResource;

/**
 * This resource returns a list of configurations which are available for the
 * user given in the request
 * 
 * @author Tobias Brandt
 *
 */
public class GetConfigurationServerResource extends AbstractSessionServerResource {

	public static final String PATH = "CMGetConfigurationList";

	@Post
	public CMConfigurationListResponseDTO getConfigurationList(GetCMConfigurationListRequestDTO request) {
		// Try to login
		try {
			ISession session = loginWithToken(request.getToken());
			String username = session.getUser().getName();
			List<Configuration> configs = ConfigurationService.getAvailableConfigurations(username);
			CMConfigurationListResponseDTO dto = new CMConfigurationListResponseDTO();
			for (Configuration config : configs) {
				ConfigurationDescription description = new ConfigurationDescription(config.getIdentifier(),
						config.getName(), config.getDescription());
				dto.addConfiguration(description);
			}
			return dto;
		} catch (ResourceException e) {
			// Login data was wrong
			return null;
		}
	}
}
