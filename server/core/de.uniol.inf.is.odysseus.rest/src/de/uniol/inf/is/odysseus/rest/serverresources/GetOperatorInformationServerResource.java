package de.uniol.inf.is.odysseus.rest.serverresources;

import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest.dto.request.GenericSessionRequestDTO;

public class GetOperatorInformationServerResource extends AbstractSessionServerResource {

	public static final String PATH = "getOperatorInformation";

	@Post
	public LogicalOperatorInformation getOperatorInformation(GenericSessionRequestDTO<String> command){
		ISession session = this.loginWithToken(command.getToken());

		String name = command.getValue();

		LogicalOperatorInformation res = ExecutorServiceBinding.getExecutor().getOperatorInformation(name, session);

		return res;
	}

}
