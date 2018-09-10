package de.uniol.inf.is.odysseus.rest.serverresources;

import org.restlet.data.Status;
import org.restlet.resource.Post;

import org.restlet.resource.ResourceException;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.rest.dto.request.LoginRequestDTO;
import de.uniol.inf.is.odysseus.rest.dto.response.GenericResponseDTO;

public class LoginServerResource extends AbstractServerResource {
	public static final String PATH = "login";

	@Post
	public GenericResponseDTO<String> login(LoginRequestDTO loginRequestDTO) {
		ITenant tenant = UserManagementProvider.instance.getTenant(loginRequestDTO.getTenant());
		ISession user = SessionManagement.instance.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword().getBytes(), tenant);
		if (user != null) {
			String token = user.getToken();
			return new GenericResponseDTO<String>(token);
		}
		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "User data are not correct");
	}

}
