package de.uniol.inf.is.odysseus.rest.serverresources;

import java.util.List;

import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest.dto.request.GenericSessionRequestDTO;

public class GetAllOperatorInformationServerResource extends AbstractSessionServerResource {

	public static final String PATH = "getAllOperatorInformation";

	@Post
	public List<LogicalOperatorInformation> getAllOperatorInformation(GenericSessionRequestDTO<String> command){
		ISession session = this.loginWithToken(command.getToken());

		List<LogicalOperatorInformation> res = ExecutorServiceBinding.getExecutor().getOperatorInformations(session);

		return res;
	}

}
