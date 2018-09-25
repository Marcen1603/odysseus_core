package de.uniol.inf.is.odysseus.rest.serverresources;

import java.util.Set;

import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest.dto.request.GenericSessionRequestDTO;

/**
 * Retrieve all datatypes
 * @author Marco Grawunder
 *
 */

public class GetAggregateFunctionsServerResource extends AbstractSessionServerResource {

	public static final String PATH = "getAggregateFunctions";

	@Post
	public Set<String> getDatatypes(GenericSessionRequestDTO<String> sessionRequestDTO) throws ClassNotFoundException{
		ISession session = this.loginWithToken(sessionRequestDTO.getToken());

		String name = sessionRequestDTO.getValue();
		
		Set<String> ret = ExecutorServiceBinding.getExecutor().getRegisteredAggregateFunctions(name,session) ;
					
		return ret;
	}


}
