package de.uniol.inf.is.odysseus.condition.rest.serverresources;


import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.condition.rest.dto.request.GetCMConfigurationListRequestDTO;
import de.uniol.inf.is.odysseus.condition.rest.dto.response.CMConfigurationListResponseDTO;
import de.uniol.inf.is.odysseus.condition.rest.dto.response.ConfigurationDescription;
import de.uniol.inf.is.odysseus.rest.serverresources.AbstractSessionServerResource;

public class GetConfigurationServerResource extends AbstractSessionServerResource {

	public static final String PATH = "CMGetConfigurationList";

	@Post
	public CMConfigurationListResponseDTO getConfigurationList(GetCMConfigurationListRequestDTO request) {
		//ISession session = loginWithToken(request.getToken());
		ConfigurationDescription description = new ConfigurationDescription(0, "test", "no desc");
		CMConfigurationListResponseDTO dto = new CMConfigurationListResponseDTO();
		dto.addConfiguration(description);
		return dto;
	}

}
