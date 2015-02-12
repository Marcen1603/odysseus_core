package de.uniol.inf.is.odysseus.rest.serverresources;



import java.util.Collection;

import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest.dto.request.AddQueryRequestDTO;
import de.uniol.inf.is.odysseus.rest.dto.response.GenericResponseDTO;

public class AddQueryServerResource extends AbstractSessionServerResource  {

	public static final String PATH = "addQuery";

	@Post
	public GenericResponseDTO<Collection<Integer>> addQuery(AddQueryRequestDTO addQueryRequestDTO) {
		ISession session = this.loginWithToken(addQueryRequestDTO.getToken());
		Collection<Integer> result = ExecutorServiceBinding.getExecutor().addQuery(addQueryRequestDTO.getQuery(),addQueryRequestDTO.getParser(), session,  new Context());
		return new GenericResponseDTO<Collection<Integer>>(result);
	}

	

}
