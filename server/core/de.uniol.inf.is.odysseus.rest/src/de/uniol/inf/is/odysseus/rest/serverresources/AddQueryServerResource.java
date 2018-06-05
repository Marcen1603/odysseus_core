package de.uniol.inf.is.odysseus.rest.serverresources;

import java.util.Collection;

import org.restlet.resource.Post;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest.dto.request.AddQueryRequestDTO;
import de.uniol.inf.is.odysseus.rest.dto.response.GenericResponseDTO;
import de.uniol.inf.is.odysseus.rest.exception.OdysseusExeption;

public class AddQueryServerResource extends AbstractSessionServerResource {

	public static final String PATH = "addQuery";

	@Post
	public GenericResponseDTO<Collection<Integer>> addQuery(AddQueryRequestDTO addQueryRequestDTO) throws OdysseusExeption{
		ISession session = this.loginWithToken(addQueryRequestDTO.getToken());
		Collection<Integer> result = null;
		try {
			result = ExecutorServiceBinding.getExecutor().addQuery(addQueryRequestDTO.getQuery(),
					addQueryRequestDTO.getParser(), session, new Context());
		} catch (Exception e) {
			throw new OdysseusExeption("Error adding query", e);
		}
		return new GenericResponseDTO<Collection<Integer>>(result);
	}

}
