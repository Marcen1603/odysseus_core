package de.uniol.inf.is.odysseus.condition.rest.serverresources;

import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.condition.rest.datatypes.ClientConfiguration;
import de.uniol.inf.is.odysseus.condition.rest.datatypes.Configuration;
import de.uniol.inf.is.odysseus.condition.rest.dto.request.RunConfigurationRequestDTO;
import de.uniol.inf.is.odysseus.condition.rest.service.ConfigurationService;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.serverresources.AbstractSessionServerResource;

/**
 * This resource runs the given configuration. Therefore, the server part of the
 * configuration is executed and the client part is send to the client
 * 
 * @author Tobias Brandt
 *
 */
public class RunConfigurationServerResource extends AbstractSessionServerResource {

	public static final String PATH = "RunConfiguration";

	@Post
	public ClientConfiguration runConfiguration(RunConfigurationRequestDTO runRequest) {
		ISession session = loginWithToken(runRequest.getToken());

		Configuration config = ConfigurationService.getConfiguration(runRequest.getConfigurationId());
		ConfigurationService.runConfiguration(config, session);
		return config.getClientConfiguration();
	}

}
