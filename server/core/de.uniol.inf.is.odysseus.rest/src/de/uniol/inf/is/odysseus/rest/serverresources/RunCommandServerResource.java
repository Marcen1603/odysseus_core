package de.uniol.inf.is.odysseus.rest.serverresources;

import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest.dto.request.GenericSessionRequestDTO;
import de.uniol.inf.is.odysseus.rest.dto.response.GenericResponseDTO;

public class RunCommandServerResource extends AbstractSessionServerResource {

	public static final String PATH = "runCommand";

	@Post
	public GenericResponseDTO<Boolean> runCommand(GenericSessionRequestDTO<String> command){
		ISession session = this.loginWithToken(command.getToken());

		ExecutorServiceBinding.getExecutor().runCommand(command.getValue(), session);

		return new GenericResponseDTO<Boolean>(true);
	}

}
